package com.example.myfootballlifeserver.service

import com.example.myfootballlifeserver.data.models.team.Team
import com.example.myfootballlifeserver.repositories.TeamInfoRepository
import com.example.myfootballlifeserver.repositories.YoutubeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class YoutubeService {
    @Autowired
    lateinit var youtubeRepository: YoutubeRepository
    @Autowired
    lateinit var teamInfoRepository: TeamInfoRepository

    fun requestLatestChannels() {
        val youtubeChannelIdList = getChannelIdList()
        GlobalScope.launch(Dispatchers.IO) {
            val channels = youtubeRepository.getChannels(youtubeChannelIdList)
            if(channels.isNotEmpty()) {
                youtubeRepository.insertChannels(channels)
            }
        }
    }

    fun requestLatestVideos() {
        val youtubeChannelIdList = getChannelIdList()
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

    private fun getChannelIdList() =
        teamInfoRepository.teamBody.teams.fold<Team, MutableList<String>>(mutableListOf()) { acc, team ->
            team.officialYoutubeChannelId?.let { acc.add(it) }
            acc
        }.toList()
}