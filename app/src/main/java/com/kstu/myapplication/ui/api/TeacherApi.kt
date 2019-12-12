package com.kstu.myapplication.ui.api

import com.kstu.myapplication.model.TeacherModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Path


interface TeacherApi {
    @Headers( "Content-Type: application/json;charset=UTF-8")
    @GET("api/teachers/{id}")
    fun getTeachers(@Path("id") id:Int): Call<List<TeacherModel>>
}
