package com.kstu.myapplication.ui.api

import com.kstu.myapplication.model.CourseModel
import retrofit2.Call
import retrofit2.http.GET


interface CourseApi {
    @GET("api/course")
    fun getCourses():Call<List<CourseModel>>
}
