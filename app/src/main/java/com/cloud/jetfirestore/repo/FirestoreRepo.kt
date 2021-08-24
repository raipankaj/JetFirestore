package com.cloud.jetfirestore.repo

import androidx.compose.runtime.mutableStateOf
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.flow.MutableStateFlow

object FirestoreRepo {

    fun addSnapshot(url: String): MutableStateFlow<Pair<QuerySnapshot?, FirebaseFirestoreException?>> {
        val firestoreSnapMutableFlow = MutableStateFlow<Pair<QuerySnapshot?, FirebaseFirestoreException?>>(
            Pair(null,null)
        )
        FirebaseFirestore.getInstance().collection(url).addSnapshotListener { value, error ->
            firestoreSnapMutableFlow.value = Pair(value, error)
        }

        return firestoreSnapMutableFlow
    }
}