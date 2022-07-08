package com.example.myfootballlifeserver.utils

import com.google.gson.Gson
import java.io.FileReader

object Converter {
    fun stringListToCommaSeparatedString(strList:List<String>): String {
        return StringBuffer().apply {
            strList.forEach { str ->
                this.append(str)
                this.append(',')
            }
            if (this.isNotBlank()) {
                this.deleteCharAt(this.length - 1)
            }
        }.toString()
    }

    fun <T> jsonFileToObject(fileName:String, objectType:Class<T>): T {
        val reader = FileReader(fileName)
        return Gson().fromJson(reader, objectType)
    }
}