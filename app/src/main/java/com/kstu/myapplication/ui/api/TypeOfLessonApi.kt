package com.kstu.myapplication.ui.api

import com.kstu.myapplication.model.TypeOfLesson
import retrofit2.Call
import retrofit2.http.GET

interface TypeOfLessonApi {
    @GET("api/typeoflesson")
    fun getTypesOfLesson(): Call<List<TypeOfLesson>>

}