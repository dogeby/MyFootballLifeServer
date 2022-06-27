package com.example.myfootballlifeserver.data.api

import com.example.myfootballlifeserver.ApiKey
import com.example.myfootballlifeserver.data.models.youtubeapi.channels.ChannelsResponseBody
import com.example.myfootballlifeserver.data.models.youtubeapi.playlistitems.PlaylistItemsResponseBody
import com.example.myfootballlifeserver.data.models.youtubeapi.playlists.PlaylistsResponseBody
import com.example.myfootballlifeserver.data.models.youtubeapi.videos.VideosResponseBody
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * YoutubeDataApi
 * https://developers.google.com/youtube/v3/docs
 */
interface YoutubeDataApiService {

    companion object {
        private const val YOUTUBE_DATA_API_KEY = ApiKey.YOUTUBE_DATA_API_KEY
    }

    /** https://developers.google.com/youtube/v3/docs/channels/list **/
    @GET("channels")
    suspend fun requestListChannels(@Query("forUsername") forUsername:String? = null, @Query("id") id:String? = null, @Query("pageToken") pageToken: String? = null, @Query("part")part: String = "snippet, contentDetails", @Query("key") key: String = YOUTUBE_DATA_API_KEY): ChannelsResponseBody

    /** https://developers.google.com/youtube/v3/docs/playlists/list */
    @GET("playlists")
    suspend fun requestListPlaylists(@Query("id") id:String? = null, @Query("channelId") channelId:String? = null, @Query("part")part: String = "snippet", @Query("key") key: String = YOUTUBE_DATA_API_KEY): PlaylistsResponseBody

    /** https://developers.google.com/youtube/v3/docs/playlistItems/list?hl=ko */
    @GET("playlistItems")
    suspend fun requestListPlaylistItems(@Query("id") id:String? = null, @Query("playlistId") playlistId:String? = null, @Query("pageToken") pageToken:String? = null, @Query("part")part: String = "snippet, contentDetails", @Query("key") key: String = YOUTUBE_DATA_API_KEY): PlaylistItemsResponseBody

    /** https://developers.google.com/youtube/v3/docs/videos/list?hl=ko */
    @GET("videos")
    suspend fun requestListVideos(@Query("id") id:String? = null, @Query("pageToken") pageToken: String? = null, @Query("part")part: String = "snippet, contentDetails, statistics", @Query("key") key: String = YOUTUBE_DATA_API_KEY): VideosResponseBody
}