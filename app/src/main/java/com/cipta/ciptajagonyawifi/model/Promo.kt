package com.cipta.ciptajagonyawifi.model

import com.google.firebase.Timestamp

data class Promo(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val imageUrl: String = "",
    val isActive: Boolean = false,
    val timestamp: Timestamp? = null
)
