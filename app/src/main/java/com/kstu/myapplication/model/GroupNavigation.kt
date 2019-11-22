package com.kstu.myapplication.model

import java.io.Serializable

data class GroupNavigation(
    val code:Int,
    val grup:String,
    val course:Int,
    val courseNavigation:CourseModel?
):Serializable {
    override fun toString(): String = grup
}
