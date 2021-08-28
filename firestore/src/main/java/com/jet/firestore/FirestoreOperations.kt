package com.jet.firestore

import androidx.compose.runtime.*
import com.google.firebase.firestore.*

@Composable
fun JetFirestore(
    path: FirebaseFirestore.() -> Any,
    limitOnSingleTimeCollectionFetch: Long = 0,
    queryOnCollection: (CollectionReference.() -> Query)? = null,
    onSingleTimeCollectionFetch: ((QuerySnapshot?, Exception?) -> Unit)? = null,
    onSingleTimeDocumentFetch: ((DocumentSnapshot?, Exception?) -> Unit)? = null,
    onRealtimeCollectionFetch: ((QuerySnapshot?, FirebaseFirestoreException?) -> Unit)? = null,
    onRealtimeDocumentFetch: ((DocumentSnapshot?, FirebaseFirestoreException?) -> Unit)? = null,
    content: @Composable (Pagination) -> Unit
) {
    val instance = remember {
        FirebaseFirestore.getInstance()
    }

    var lastFetchedDocument by remember {
        mutableStateOf<DocumentSnapshot?>(null)
    }

    //Setup realtime listener
    RealtimeSync(
        onRealtimeCollectionFetch,
        onRealtimeDocumentFetch,
        instance,
        path,
        queryOnCollection
    )

    //Setup one time listener
    singleSync(instance,
        path,
        queryOnCollection,
        limitOnSingleTimeCollectionFetch,
        null,
        onSingleTimeCollectionFetch,
        onSingleTimeDocumentFetch,
        onLastFetchedDocument = {
            lastFetchedDocument = it
        })

    content(
        object : Pagination {
            override fun loadNextPage() {
                singleSync(instance,
                    path,
                    queryOnCollection,
                    limitOnSingleTimeCollectionFetch,
                    lastFetchedDocument,
                    onSingleTimeCollectionFetch,
                    onSingleTimeDocumentFetch,
                    onLastFetchedDocument = {
                        lastFetchedDocument = it
                    })
            }
        }
    )
}

private fun singleSync(
    instance: FirebaseFirestore,
    path: FirebaseFirestore.() -> Any,
    query: (CollectionReference.() -> Query)?,
    limit: Long,
    lastDocumentSnapshot: DocumentSnapshot?,
    onSingleTimeCollectionFetch: ((QuerySnapshot?, Exception?) -> Unit)?,
    onSingleTimeDocumentFetch: ((DocumentSnapshot?, Exception?) -> Unit)?,
    onLastFetchedDocument: (DocumentSnapshot) -> Unit
) {
    if (instance.path() is CollectionReference) {

        var collectionReference = if (query != null) {
            (instance.path() as CollectionReference).query()
        } else {
            (instance.path() as CollectionReference)
        }

        if (limit != 0L) {
            collectionReference = if (lastDocumentSnapshot != null) {
                collectionReference.limit(limit).startAfter(lastDocumentSnapshot)
            } else {
                collectionReference.limit(limit)
            }
        }

        collectionReference.get()
            .addOnSuccessListener {
                if (it.documents.isEmpty().not()) {
                    onLastFetchedDocument(it.documents[it.documents.size - 1])
                }
                onSingleTimeCollectionFetch?.invoke(it, null)
            }.addOnFailureListener {
                /**When the result is ended in failure
                then send exception back as value**/
                onSingleTimeCollectionFetch?.invoke(null, it)
            }
    } else {
        (instance.path() as DocumentReference).get()
            .addOnSuccessListener {
                onSingleTimeDocumentFetch?.invoke(it, null)
            }.addOnFailureListener {
                /**When the result is ended in failure
                then send exception back as value**/
                onSingleTimeDocumentFetch?.invoke(null, it)
            }
    }
}

@Composable
private fun RealtimeSync(
    onRealtimeCollectionFetch: ((QuerySnapshot?, FirebaseFirestoreException?) -> Unit)?,
    onRealtimeDocumentFetch: ((DocumentSnapshot?, FirebaseFirestoreException?) -> Unit)?,
    instance: FirebaseFirestore,
    path: FirebaseFirestore.() -> Any,
    query: (CollectionReference.() -> Query)?
) {
    if (onRealtimeCollectionFetch != null || onRealtimeDocumentFetch != null) {
        DisposableEffect(key1 = Unit) {

            val listener = if (instance.path() is CollectionReference) {

                val collectionReference = if (query != null) {
                    (instance.path() as CollectionReference).query()
                } else {
                    (instance.path() as CollectionReference)
                }

                collectionReference
                    .addSnapshotListener { value, error ->
                        onRealtimeCollectionFetch?.invoke(value, error)
                    }
            } else {
                (instance.path() as DocumentReference).addSnapshotListener { value, error ->
                    onRealtimeDocumentFetch?.invoke(value, error)
                }
            }

            onDispose {
                listener.remove()
            }
        }
    }
}
