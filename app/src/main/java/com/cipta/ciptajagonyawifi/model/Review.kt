package com.cipta.ciptajagonyawifi.model

data class Review(
    val id: String = "",
    val packageId: Int = 0,
    val name: String = "",
    val comment: String = "",
    val rating: Float = 0f,
    val timestamp: Long = System.currentTimeMillis()
)
