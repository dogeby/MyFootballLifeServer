package com.example.myfootballlifeserver.service

import com.example.myfootballlifeserver.repositories.TwitterRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class TwitterService {
    @Autowired
    lateinit var twitterRepository: TwitterRepository
    @Value("\${TEAM_OFFICIAL_TWITTER_USER_ID_LIST}")
    lateinit var teamOfficialTwitterIdList:List<String>
    @Value("\${REPORTER_TWITTER_USER_ID_LIST}")
    lateinit var reporterTwitterIdList:List<String>

    fun requestLatestUsers() {
        val twitterIdList = teamOfficialTwitterIdList + reporterTwitterIdList
        GlobalScope.launch(Dispatchers.IO) {
            val users = twitterRepository.getUsers(twitterIdList)
            if(users.isNotEmpty()) {
                twitterRepository.insertUsers(users)
            }
        }
    }

    fun requestLatestTweets(){
        val twitterIdList = teamOfficialTwitterIdList + reporterTwitterIdList
        GlobalScope.launch(Dispatchers.IO) {
            twitterIdList.forEach { userId->
                val tweets = twitterRepository.getTweets(userId)
                if(tweets.isNotEmpty()) {
                    twitterRepository.insertTweets(userId, tweets)
                }
            }
        }
    }
}