package com.example.myfootballlifeserver.repositories

import com.example.myfootballlifeserver.data.api.YoutubeDataApiModule
import com.example.myfootballlifeserver.data.models.youtubeapi.ChannelsBody
import com.example.myfootballlifeserver.data.models.youtubeapi.VideosBody
import com.example.myfootballlifeserver.data.models.youtubeapi.channels.Channels
import com.example.myfootballlifeserver.data.models.youtubeapi.channels.RelatedPlaylistsKey
import com.example.myfootballlifeserver.data.models.youtubeapi.playlistitems.PlaylistItems
import com.example.myfootballlifeserver.data.models.youtubeapi.videos.Videos
import com.example.myfootballlifeserver.utils.Converter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository

@Repository
class YoutubeRepository {
    private val youtubeDataApiService = YoutubeDataApiModule.provideYoutubeDataService()
    @Autowired
    private lateinit var firebaseRepository: FirebaseRepository
    @Autowired
    private lateinit var localPropertiesRepository: LocalPropertiesRepository
    companion object {
        const val YOUTUBE_LATEST_VIDEO_FILE_NAME = "./src/main/resources/youtubeLatestVideo.properties"
        const val YOUTUBE_UPLOADS_PLAYLIST_FILE_NAME = "./src/main/resources/youtubeUploadsPlaylist.properties"
    }
    /** Channels **/
    suspend fun getChannels(ids: List<String>): List<Channels> {
        val channels = mutableListOf<Channels>()
        val commaSeparatedIdsString = Converter.stringListToCommaSeparatedString(ids)
        var nextPageToken:String? = null
        do {
            val channelsResponseBody = youtubeDataApiService.requestListChannels(null, commaSeparatedIdsString, nextPageToken)
            nextPageToken = channelsResponseBody.nextPageToken
            channelsResponseBody.items?.let {
                channels.addAll(it)
            }
        } while (nextPageToken != null)
        if(channels.isNotEmpty()) {
            insertUploadsPlaylistsIdInProperties(channels)
        }
        return channels.toList()
    }

    suspend fun insertChannels(channels:List<Channels>) {
        val channelsBody = ChannelsBody(channels)
        firebaseRepository.insertData("youtubeChannels", channelsBody)
    }

    /**
     * Playlist
     * 체널의 업로드 플레이리스트 id를 properties에 저장
     */
    private fun insertUploadsPlaylistsIdInProperties(channels: List<Channels>) {
        channels.forEach { channel ->
            channel.contentDetails.relatedPlaylists[RelatedPlaylistsKey.UPLOADS]?.let { uploadsPlaylistId ->
                localPropertiesRepository.insertProperty(YOUTUBE_UPLOADS_PLAYLIST_FILE_NAME, channel.id, uploadsPlaylistId)
            }
        }
    }

    private fun getUploadsPlaylistIdInProperties(channelId: String):String? {
        return localPropertiesRepository.getProperty(YOUTUBE_UPLOADS_PLAYLIST_FILE_NAME, channelId)
    }

    suspend fun getUploadsPlaylistId(channelId: String):String? {
        return getUploadsPlaylistIdInProperties(channelId) ?: getChannels(listOf(channelId)).first().contentDetails.relatedPlaylists[RelatedPlaylistsKey.UPLOADS]
    }

    /** PlaylistItem **/
    private suspend fun getLatestPlaylistItems(playlistId: String, sinceVideoId: String): List<PlaylistItems> {
        val playlistItems = mutableListOf<PlaylistItems>()
        var nextPageToken: String? = null
        do {
            val playlistItemsResponseBody = youtubeDataApiService.requestListPlaylistItems(null, playlistId, nextPageToken)
            nextPageToken = playlistItemsResponseBody.nextPageToken
            playlistItemsResponseBody.items?.let { tmpPlaylistItems ->
                val indexOfSinceVideo = tmpPlaylistItems.indexOfFirst { playlistItem -> playlistItem.contentDetails.videoId == sinceVideoId }
                if(indexOfSinceVideo >= 0) {
                    nextPageToken = null
                    for(i in 0 until indexOfSinceVideo){
                        playlistItems.add(tmpPlaylistItems[i])
                    }
                } else {
                    playlistItems.addAll(tmpPlaylistItems)
                }
            }
        } while (nextPageToken != null)
        return playlistItems.toList()
    }

    private suspend fun getFirstPlaylistItem(playlistId: String): PlaylistItems? {
        return youtubeDataApiService.requestListPlaylistItems(null, playlistId).items?.first()
    }

    /** Videos **/
    private suspend fun getVideos(videoIds:List<String>): List<Videos> {
        val videos = mutableListOf<Videos>()
        val commaSeparatedIdsString = Converter.stringListToCommaSeparatedString(videoIds)
        var nextPageToken: String? = null
        do {
            val videosResponseBody = youtubeDataApiService.requestListVideos(commaSeparatedIdsString, nextPageToken)
            nextPageToken = videosResponseBody.nextPageToken
            videosResponseBody.items?.let {
                videos.addAll(it)
            }
        } while (nextPageToken != null)
        return videos
    }

    suspend fun getLatestVideos(playlistId: String): List<Videos> {
        val latestVideosId:String? = localPropertiesRepository.getProperty(YOUTUBE_LATEST_VIDEO_FILE_NAME, playlistId)
        if(latestVideosId.isNullOrBlank()) {
            val playlistItem = getFirstPlaylistItem(playlistId)
            val video = youtubeDataApiService.requestListVideos(playlistItem?.contentDetails?.videoId).items?.first()
            video?.id?.let { localPropertiesRepository.insertProperty(YOUTUBE_LATEST_VIDEO_FILE_NAME, playlistId, it) }
            video?.let {
                return listOf(it)
            }
            return listOf()
        }
        val latestPlaylistItems = getLatestPlaylistItems(playlistId, latestVideosId)
        val videoIds = latestPlaylistItems.map { playlistItems -> playlistItems.contentDetails.videoId }
        val videos = getVideos(videoIds)
        if(videos.isNotEmpty()) {
            localPropertiesRepository.insertProperty(YOUTUBE_LATEST_VIDEO_FILE_NAME, playlistId, videos.first().id)
        }
        return videos
    }

    suspend fun insertVideos(channelId: String, videos:List<Videos>) {
        val videosBody = VideosBody(videos)
        firebaseRepository.insertData("latestVideos/$channelId", videosBody)
    }
}