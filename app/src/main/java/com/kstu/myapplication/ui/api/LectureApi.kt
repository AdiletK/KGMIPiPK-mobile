package com.kstu.myapplication.ui.api

import com.kstu.myapplication.model.LectureModel
import com.kstu.myapplication.model.InsertLectureDataModel
import com.kstu.myapplication.model.StudentModel
import retrofit2.Call
import retrofit2.http.*

interface LectureApi {
    @GET("api/lections/{group}/{teacher}")
    fun getLextures(@Path("group")group: Int, @Path("teacher")teacher:Short): Call<List<LectureModel>>

    @GET("api/lections/{lectionId}")
    fun getStudentsInLecture(@Path("lectionId")lectureId: Int): Call<List<StudentModel>>

    @Headers("Content-Type: application/json")
    @POST("api/lections")
    fun insertLecture(@Body lextures: InsertLectureDataModel):Call<Int>

    @PUT("api/lections/{id}")
    fun updateLecture(@Path("id") id:Int, @Body objects: StudentModel) :Call<Boolean>
}