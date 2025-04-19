package com.cipta.ciptajagonyawifi.data

import com.cipta.ciptajagonyawifi.model.WifiPackage

val dummyPackages = listOf(
    WifiPackage(
        id = 1,
        name = "Paket Basic",
        speed = "20 Mbps",
        price = "Rp150.000",
        description = "Cocok untuk rumah tangga kecil.",
        promo = "Diskon 10% di bulan Ramadhan!"
    ),
    WifiPackage(
        id = 2,
        name = "Paket Family",
        speed = "50 Mbps",
        price = "Rp250.000",
        description = "Streaming dan meeting tanpa hambatan.",
        promo = "Gratis 1 bulan untuk pelanggan baru!"
    )
)

