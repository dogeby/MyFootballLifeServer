package com.example.myfootballlifeserver.config

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import java.io.FileInputStream
import javax.annotation.PostConstruct

@Configuration
class FirebaseConfiguration {
    @Value("\${FIREBASE_ADMIN_SDK_JSON_PATH}")
    private lateinit var firebaseAdminSdkJsonPath:String
    @Value("\${FIREBASE_REALTIME_DATABASE_URL}")
    private lateinit var firebaseRealtimeDatabaseUrl:String

    @PostConstruct
    fun init() {
        try {
            val serviceAccount = FileInputStream(firebaseAdminSdkJsonPath)
            val options:FirebaseOptions = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setDatabaseUrl(firebaseRealtimeDatabaseUrl)
                .build()
            FirebaseApp.initializeApp(options)
        }catch (e:Exception){
            e.printStackTrace()
        }
    }
}