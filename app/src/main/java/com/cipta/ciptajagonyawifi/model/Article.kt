package com.cipta.ciptajagonyawifi.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.ServerTimestamp

data class Article(
    val id: String = "",
    val title: String = "",
    val content: String = "",
    val imageUrl: String = "",
    @ServerTimestamp
    val timestamp: Timestamp = Timestamp.now()
)