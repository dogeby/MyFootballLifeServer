package com.example.myfootballlifeserver.data.models.team

data class Team(
    val name: String,
    val officialTwitterUserId: String?,
    val officialYoutubeChannelId: String?,
    val reporterTwitterUserId: List<String>?
)