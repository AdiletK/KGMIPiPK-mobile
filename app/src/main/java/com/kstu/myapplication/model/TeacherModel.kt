package com.kstu.myapplication.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class TeacherModel (
    @SerializedName("nom")
    val id:Short,
    @SerializedName("fio")
    val name:String,
    @SerializedName("faculty")
    val faculty:Int?,
    @SerializedName("department")
    val department:Int?,
    @SerializedName("telefon")
    val phoneNumber:String?,
    @SerializedName("prim")
    val prim:String?,
    @SerializedName("departmentNavigation")
    val departmentNavigation:String?
//    @SerializedName("facultyNavigation")
//    val facultyNavigation:String?
    ):Serializable{

    override fun toString(): String = name
}