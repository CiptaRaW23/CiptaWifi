package com.cipta.ciptajagonyawifi.model

data class WifiPackage(
    val id: Int = 0,
    val name: String = "",
    val speed: String = "",
    val price: String = "",
    val description: String = "",
    val promo: String = "",
    val docId: String = "",
    val imageUrls: List<String> = emptyList()
)

