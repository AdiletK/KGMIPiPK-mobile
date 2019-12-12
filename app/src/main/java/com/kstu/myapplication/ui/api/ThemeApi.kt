package com.kstu.myapplication.ui.api

import com.kstu.myapplication.model.ThemeNavigation
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header

interface ThemeApi {
    @GET("api/theme")
    fun getThemes(): Call<List<ThemeNavigation>>

}