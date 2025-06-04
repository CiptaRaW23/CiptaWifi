package com.cipta.ciptajagonyawifi.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object RetrofitInstance {
    val api: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl("https://script.google.com/macros/s/AKfycbzO8cqoRE24u4oDPu_FPj86SczyyfOdThwolAwFJDNobeVIXp4j6V_W7cpxzlww5Jdg/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}
