package com.example.myfootballlifeserver.repositories

import org.springframework.stereotype.Repository
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.util.*

@Repository
class LocalPropertiesRepository {
    fun checkFile(fileName: String) {
        val propertiesFile = File(fileName)
        if(!propertiesFile.exists()) {
            try {
                propertiesFile.createNewFile()
            } catch (e: IOException){
                e.printStackTrace()
            }
        }
    }

    fun insertProperty(fileName: String, key:String, value:String) {
        val properties = Properties()
        try {
            checkFile(fileName)
            val inputStream = FileInputStream(fileName)
            properties.load(inputStream)
            properties.setProperty(key, value)
            val outputStream = FileOutputStream(fileName)
            properties.store(outputStream, null)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun getProperty(fileName: String, key:String):String? {
        val properties = Properties()
        try {
            checkFile(fileName)
            val inputStream = FileInputStream(fileName)
            properties.load(inputStream)
            return properties.getProperty(key)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }
}