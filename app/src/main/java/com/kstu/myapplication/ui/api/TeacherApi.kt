package com.kstu.myapplication.ui.api

import com.kstu.myapplication.model.TeacherModel
import retrofit2.Call
import retrofit2.http.GET


interface TeacherApi {
    @GET("api/teachers")
    fun getTeachers(): Call<List<TeacherModel>>
}
