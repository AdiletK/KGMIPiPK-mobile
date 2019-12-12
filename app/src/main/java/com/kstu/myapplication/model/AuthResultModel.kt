package com.kstu.myapplication.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class AuthResultModel (
    val token:String,
    @SerializedName("id")
    val dep_id :Int,
    val department:String
):Serializable