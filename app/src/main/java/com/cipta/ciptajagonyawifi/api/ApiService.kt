package com.cipta.ciptajagonyawifi.api

import com.cipta.ciptajagonyawifi.model.FormData
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("exec") // ganti nanti di retrofit
    fun submitForm(@Body data: FormData): Call<ResponseBody>
}