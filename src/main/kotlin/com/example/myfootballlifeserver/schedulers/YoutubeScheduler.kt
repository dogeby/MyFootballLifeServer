package com.example.myfootballlifeserver.schedulers

import com.example.myfootballlifeserver.service.YoutubeService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class YoutubeScheduler {
    companion object {
        private const val channelsRefreshTime = 604_800_000L   //7일
        private const val videosRefreshTime = 3600_000L //1시간
    }
    @Autowired
    private lateinit var youtubeService: YoutubeService

    @Scheduled(fixedRate = channelsRefreshTime)
    fun scheduleGetChannels() {
        youtubeService.requestLatestChannels()
    }

    @Scheduled(fixedRate = videosRefreshTime)
    fun scheduleGetVideos() {
        youtubeService.requestLatestVideos()
    }

}