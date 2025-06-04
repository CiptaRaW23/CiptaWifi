package com.cipta.ciptajagonyawifi.api

import com.cipta.ciptajagonyawifi.model.FormData
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("exec") // Google Apps Script endpoint
    suspend fun sendForm(@Body formData: FormData): Response<Void>
}
