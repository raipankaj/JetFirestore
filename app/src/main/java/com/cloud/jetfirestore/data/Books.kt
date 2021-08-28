package com.cloud.jetfirestore.data

data class Books(
    val name: String, val author: String, val description: String, val image: String, val cost: Int
) {
    constructor(): this("", "","","", 0)
}