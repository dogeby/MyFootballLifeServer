package com.example.myfootballlifeserver.schedulers

import com.example.myfootballlifeserver.service.TwitterService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class TwitterScheduler {
    companion object {
        private const val usersRefreshTime = 604_800_000L   //7일
        private const val tweetsRefreshTime = 3600_000L //1시간
    }
    @Autowired
    private lateinit var twitterService: TwitterService

    @Scheduled(fixedRate = usersRefreshTime)
    fun scheduleGetUsers() {
        twitterService.requestLatestUsers()
    }

    @Scheduled(fixedRate = tweetsRefreshTime)
    fun scheduleGetTweets() {
        twitterService.requestLatestTweets()
    }
}