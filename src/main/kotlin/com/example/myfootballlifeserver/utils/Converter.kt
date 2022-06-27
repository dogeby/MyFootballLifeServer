package com.example.myfootballlifeserver.utils

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
}