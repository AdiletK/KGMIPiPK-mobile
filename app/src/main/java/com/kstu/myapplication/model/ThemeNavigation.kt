package com.kstu.myapplication.model

import java.io.Serializable

data class ThemeNavigation(
    val nom:Short,
    val name:String
):Serializable{
    override fun toString(): String = name
}
