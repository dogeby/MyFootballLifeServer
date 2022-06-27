package com.example.myfootballlifeserver.repositories

import com.google.firebase.database.FirebaseDatabase
import org.springframework.stereotype.Repository

@Repository
class FirebaseRepository {

    fun insertData(path:String, value:Any) {
        val database = FirebaseDatabase.getInstance()
        val ref = database.getReference(path)
        ref.setValueAsync(value)
    }
}