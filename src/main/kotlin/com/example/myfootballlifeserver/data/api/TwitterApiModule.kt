package com.example.myfootballlifeserver.data.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object TwitterApiModule {
    private const val baseUrl = "https://api.twitter.com/"

    fun provideTwitterApiService(): TwitterApiService {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TwitterApiService::class.java)
    }
}