package com.cipta.ciptajagonyawifi.model

import com.google.firebase.Timestamp

data class Article(
    val id: String = "",
    val title: String = "",
    val content: String = "",
    val imageUrl: String = "",
    val timestamp: Timestamp = Timestamp.now()
)
