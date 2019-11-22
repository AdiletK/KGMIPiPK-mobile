package com.kstu.myapplication.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class LectureModel (
    @SerializedName("nom")
    val Nom:Int,
    @SerializedName("day")
    val Day: String,
    @SerializedName("group")
    val Group: String,
    @SerializedName("tema")
    val Tema: String,
    @SerializedName("teacher")
    val Teacher: String,
    @SerializedName("hours")
    val Hours:String ,
    @SerializedName("groupNavigation")
    val groupNav:GroupNavigation,
    @SerializedName("teacherNavigation")
    val teacher: TeacherModel,
    @SerializedName("temaNavigation")
    val themeNav:ThemeNavigation,
    @SerializedName("vidZanNavigation")
    val typeOfLesson:TypeOfLesson
): Serializable

