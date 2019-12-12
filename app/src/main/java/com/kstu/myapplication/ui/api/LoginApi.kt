package com.kstu.myapplication.ui.api

import com.kstu.myapplication.model.AuthResultModel
import retrofit2.Call
import retrofit2.http.*

interface LoginApi {
    @Headers("Content-Type: application/json")
    @GET("api/login")
    fun login(@Query("login") login: String,@Query("psw")psw:String): Call<AuthResultModel>

}