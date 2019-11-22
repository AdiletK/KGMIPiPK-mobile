package com.kstu.myapplication.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class StudentModel(
    @SerializedName("nom")
    val id:Int,
    @SerializedName("student")
    val Name: String,
    var prisutstvie:String,
    var prim:String
):Serializable {
    override fun toString(): String = Name
}