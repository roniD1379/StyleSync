package com.application.stylesync
import retrofit2.Call
import retrofit2.http.GET

interface ApiService {
    @GET("colors")
    fun getColors(): Call<ApiResponse>
}