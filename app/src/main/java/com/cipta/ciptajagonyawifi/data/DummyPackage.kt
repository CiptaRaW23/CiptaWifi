package com.cipta.ciptajagonyawifi.data

import com.cipta.ciptajagonyawifi.model.WifiPackage

val dummyPackages = listOf(
    WifiPackage(
        id = 1,
        name = "Paket Basic",
        speed = "20 Mbps",
        price = "Rp150.000",
        description = """
            Paket Basic adalah solusi internet hemat untuk keluarga kecil yang menginginkan koneksi stabil tanpa menguras dompet. 
            Dengan kecepatan hingga 20 Mbps, Anda bisa menikmati streaming YouTube, video call, dan browsing ringan tanpa gangguan.

            Cocok untuk 2–4 perangkat aktif secara bersamaan, seperti HP, laptop, dan smart TV. Paket ini sangat ideal untuk orang tua yang bekerja dari rumah dan anak-anak yang belajar online.

            Didukung dengan layanan teknis 24 jam dan instalasi yang cepat!

            Spesifikasi:
            - Kecepatan hingga 20 Mbps
            - Cocok untuk rumah tangga kecil
            - Unlimited kuota (no FUP)
            - Gratis modem dan instalasi
        """.trimIndent(),
        promo = """
            🎉 Promo Spesial Ramadhan: Diskon 10% untuk bulan pertama!  
            💡 Hemat lebih banyak, tetap terkoneksi dengan nyaman!
        """.trimIndent()
    ),
    WifiPackage(
        id = 2,
        name = "Paket Family",
        speed = "50 Mbps",
        price = "Rp250.000",
        description = """
            Paket Family dirancang khusus untuk keluarga aktif yang membutuhkan internet super cepat dan stabil setiap saat. 
            Dengan kecepatan hingga 50 Mbps, Anda bisa melakukan banyak aktivitas bersamaan seperti streaming 4K, main game online, meeting Zoom, dan belajar online tanpa buffering!

            Paket ini mampu mendukung lebih dari 6 perangkat sekaligus. Baik itu laptop, HP, smart TV, atau konsol game – semua tetap lancar!

            Spesifikasi:
            - Kecepatan hingga 50 Mbps
            - Cocok untuk keluarga besar & kebutuhan multitasking
            - Unlimited kuota (tanpa batasan penggunaan)
            - Gratis modem dual-band dan instalasi

            Dipercaya oleh ratusan keluarga karena kestabilan dan kecepatan koneksinya.
        """.trimIndent(),
        promo = """
            🎁 Gratis 1 Bulan untuk pelanggan baru!  
            🌟 Buruan daftar sekarang dan nikmati internet tanpa batas untuk seluruh keluarga!
        """.trimIndent()
    ),
    WifiPackage(
        id = 3,
        name = "Paket Pro",
        speed = "100 Mbps",
        price = "Rp400.000",
        description = """
            Paket Pro adalah pilihan tepat untuk profesional yang memerlukan koneksi internet cepat dan stabil untuk bekerja dari rumah. 
            Dengan kecepatan hingga 100 Mbps, Anda dapat melakukan konferensi video berkualitas tinggi, mengupload dan mendownload file besar, serta bekerja secara efisien tanpa gangguan.

            Paket ini mendukung lebih dari 10 perangkat sekaligus, ideal untuk rumah tangga dengan banyak perangkat aktif.

            Spesifikasi:
            - Kecepatan hingga 100 Mbps
            - Cocok untuk kebutuhan profesional dan streaming berat
            - Unlimited kuota (no FUP)
            - Gratis modem WiFi 6 dan instalasi

            Menjamin pengalaman internet tanpa batas dengan teknologi terbaru.
        """.trimIndent(),
        promo = """
            🏷️ Diskon 20% untuk pelanggan pertama!  
            ⚡ Nikmati kecepatan super cepat dengan harga terjangkau!
        """.trimIndent()
    ),
    WifiPackage(
        id = 4,
        name = "Paket Ultra",
        speed = "200 Mbps",
        price = "Rp600.000",
        description = """
            Paket Ultra adalah paket internet tercepat untuk kebutuhan streaming, gaming, dan bekerja dengan kualitas terbaik. 
            Dengan kecepatan hingga 200 Mbps, Anda bisa menikmati pengalaman online tanpa buffering, bermain game online tanpa lag, dan menonton streaming 4K tanpa gangguan.

            Cocok untuk keluarga besar dan rumah tangga dengan banyak perangkat yang terhubung sekaligus.

            Spesifikasi:
            - Kecepatan hingga 200 Mbps
            - Ideal untuk kebutuhan bandwidth tinggi
            - Unlimited kuota (no FUP)
            - Gratis modem premium dan instalasi

            Pilihan terbaik untuk rumah tangga yang membutuhkan koneksi internet super cepat.
        """.trimIndent(),
        promo = """
            🎉 Promo Spesial: Dapatkan gratis 1 bulan untuk pelanggan baru!  
            🚀 Rasakan kecepatan internet tak terbatas!
        """.trimIndent()
    ),
    WifiPackage(
        id = 5,
        name = "Paket Gamer",
        speed = "150 Mbps",
        price = "Rp500.000",
        description = """
            Paket Gamer dirancang untuk para gamer sejati yang membutuhkan koneksi internet ultra-stabil dan cepat. 
            Dengan kecepatan hingga 150 Mbps, Anda bisa bermain game online tanpa lag, streaming dengan kualitas tinggi, dan download game dalam waktu singkat.

            Paket ini juga cocok untuk keluarga dengan anak-anak yang aktif bermain game online.

            Spesifikasi:
            - Kecepatan hingga 150 Mbps
            - Prioritas jaringan untuk gaming dan streaming
            - Unlimited kuota (no FUP)
            - Gratis modem gaming dan instalasi

            Ideal untuk para gamer yang menginginkan pengalaman bermain yang tak tertandingi.
        """.trimIndent(),
        promo = """
            🕹️ Promo Khusus Gamer: Diskon 15% untuk langganan pertama!  
            🎮 Bergabunglah sekarang dan nikmati koneksi stabil untuk game favoritmu!
        """.trimIndent()
    ),
    WifiPackage(
        id = 6,
        name = "Paket Bisnis",
        speed = "500 Mbps",
        price = "Rp1.000.000",
        description = """
            Paket Bisnis adalah solusi terbaik untuk perusahaan atau kantor yang membutuhkan koneksi internet dengan kecepatan sangat tinggi dan stabil. 
            Dengan kecepatan hingga 500 Mbps, Anda dapat melakukan video konferensi tanpa gangguan, mengirimkan data dalam jumlah besar, dan menjaga kelancaran operasional bisnis Anda.

            Paket ini mendukung penggunaan lebih dari 20 perangkat sekaligus, ideal untuk kantor yang memiliki banyak karyawan.

            Spesifikasi:
            - Kecepatan hingga 500 Mbps
            - Ideal untuk bisnis dan kantor besar
            - Unlimited kuota (no FUP)
            - Gratis instalasi dan dukungan teknis 24/7

            Menjamin koneksi internet yang andal untuk keperluan bisnis Anda.
        """.trimIndent(),
        promo = """
            🏢 Promo Khusus Bisnis: Diskon 30% untuk pelanggan baru!  
            💼 Dapatkan koneksi cepat dan stabil untuk mendukung produktivitas bisnis Anda!
        """.trimIndent()
    )
)
