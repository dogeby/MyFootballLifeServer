package com.example.myfootballlifeserver.service

import com.example.myfootballlifeserver.repositories.YoutubeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class YoutubeService {
    @Autowired
    lateinit var youtubeRepository: YoutubeRepository
    @Value("\${TEAM_OFFICIAL_YOUTUBE_CHANNEL_ID_LIST}")
    lateinit var teamOfficeYoutubeChannelIdList:List<String>
    @Value("\${ETC_YOUTUBE_CHANNEL_ID_LIST}")
    lateinit var etcYoutubeChannelIdList:List<String>

    fun requestLatestChannels() {
        val youtubeChannelIdList = teamOfficeYoutubeChannelIdList + etcYoutubeChannelIdList
        GlobalScope.launch(Dispatchers.IO) {
            val channels = youtubeRepository.getChannels(youtubeChannelIdList)
            if(channels.isNotEmpty()) {
                youtubeRepository.insertChannels(channels)
            }
        }
    }

    fun requestLatestVideos() {
        val youtubeChannelIdList = teamOfficeYoutubeChannelIdList + etcYoutubeChannelIdList
        GlobalScope.launch(Dispatchers.IO) {
            youtubeChannelIdList.forEach { channelId ->
                val uploadPlaylistId = youtubeRepository.getUploadsPlaylistId(channelId)
                val videos = uploadPlaylistId?.let { youtubeRepository.getLatestVideos(it) }
                if(!videos.isNullOrEmpty()) {
                    youtubeRepository.insertVideos(channelId, videos)
                }
            }
        }
    }
}