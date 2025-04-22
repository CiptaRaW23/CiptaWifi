package com.cipta.ciptajagonyawifi.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://script.google.com/macros/s/AKfycbygipwCZy8GGVjjE8xMz9_ogqSBYv-kZk1ctVE4ElUjZR91fctjL500fMdtvDRXDu8nTA/") // base sampai sebelum "/exec"
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val api: ApiService = retrofit.create(ApiService::class.java)
}
