package com.example.myfootballlifeserver.data.models.youtubeapi.videos

import com.google.gson.annotations.SerializedName

/** https://developers.google.com/youtube/v3/docs/videos?hl=ko */
data class Statistics(
    @SerializedName("viewCount") val viewCount:Long
)