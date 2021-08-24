package com.jet.firestore

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot

inline fun <reified T> QuerySnapshot?.getListOfObjects(): MutableList<T>? {
    return this?.toObjects(T::class.java)
}

inline fun <reified T> DocumentSnapshot?.getObject(): T? {
    return this?.toObject(T::class.java)
}