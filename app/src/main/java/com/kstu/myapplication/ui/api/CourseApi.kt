package com.kstu.myapplication.ui.api

import com.kstu.myapplication.model.CourseModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Path


interface CourseApi {
    @Headers( "Content-Type: application/json;charset=UTF-8")
    @GET("api/course/by_id/{id}")
    fun getCourses( @Path("id") id:Int):Call<List<CourseModel>>
}
