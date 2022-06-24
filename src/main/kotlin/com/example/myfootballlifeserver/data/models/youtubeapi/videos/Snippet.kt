package com.example.myfootballlifeserver.data.models.youtubeapi.videos

import com.example.myfootballlifeserver.data.models.youtubeapi.thumbnails.Thumbnails
import com.google.gson.annotations.SerializedName

/** https://developers.google.com/youtube/v3/docs/videos?hl=ko */
data class Snippet(
    @SerializedName("publishedAt") val publishedAt:String,
    @SerializedName("channelId") val channelId:String,
    @SerializedName("title") val title:String,
    @SerializedName("description") val description:String,
    @SerializedName("thumbnails") val thumbnails:Map<String, Thumbnails>,
    @SerializedName("channelTitle") val channelTitle:String
)