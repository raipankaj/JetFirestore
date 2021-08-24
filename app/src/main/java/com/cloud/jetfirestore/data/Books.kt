package com.cloud.jetfirestore.data

data class BookList(val books: List<Books>) {
    constructor(): this(emptyList())
}

data class Books(
    val name: String, val author: String, val description: String, val image: String
) {
    constructor(): this("", "","","")
}