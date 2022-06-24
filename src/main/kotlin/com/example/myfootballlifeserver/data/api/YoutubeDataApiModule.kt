package com.example.myfootballlifeserver.data.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object YoutubeDataApiModule {
    private const val baseUrl = "https://www.googleapis.com/youtube/v3/"

    fun provideYoutubeDataService(): YoutubeDataApiService {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(YoutubeDataApiService::class.java)
    }
}