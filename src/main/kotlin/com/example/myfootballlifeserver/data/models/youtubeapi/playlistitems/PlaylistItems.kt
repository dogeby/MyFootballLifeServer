package com.example.myfootballlifeserver.data.models.youtubeapi.playlistitems

import com.google.gson.annotations.SerializedName

/** https://developers.google.com/youtube/v3/docs/playlistItems?hl=ko */
data class PlaylistItems(
    @SerializedName("id") val id:String,
    @SerializedName("snippet") val snippet: Snippet,
    @SerializedName("contentDetails") val contentDetails: ContentDetails
)