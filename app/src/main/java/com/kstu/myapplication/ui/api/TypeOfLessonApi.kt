package com.kstu.myapplication.ui.api

import com.kstu.myapplication.model.TypeOfLesson
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface TypeOfLessonApi {
    @GET("api/typeoflesson")
    fun getTypesOfLesson(): Call<List<TypeOfLesson>>

    @POST("api/typeoflesson")
    fun postQuery():Call<String>

}