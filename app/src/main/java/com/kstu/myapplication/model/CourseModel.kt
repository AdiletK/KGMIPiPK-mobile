package com.kstu.myapplication.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class CourseModel(
    @SerializedName("nom")
    val  id:Int,
    val name:String,
    val fullName:String,
    val plan:Int?,
    val groups:List<GroupNavigation>?
) :Serializable{
    override fun toString(): String  = fullName
}