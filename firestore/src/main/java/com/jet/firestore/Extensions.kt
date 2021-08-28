package com.jet.firestore

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot

inline infix fun <reified T>List<T>.append(list: List<T>): List<T> {
    return this + list
}

inline fun <reified T> QuerySnapshot?.getListOfObjects(): List<T> {
    return this?.toObjects(T::class.java) ?: emptyList()
}

inline fun <reified T> DocumentSnapshot?.getObject(): T? {
    return this?.toObject(T::class.java)
}