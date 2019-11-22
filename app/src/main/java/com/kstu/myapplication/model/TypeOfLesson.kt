package com.kstu.myapplication.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class TypeOfLesson(
    val nom:Byte,
    @SerializedName("vidZan1")
    val shortName:String,
    val name:String
):Serializable{
    override fun toString(): String = name
}
