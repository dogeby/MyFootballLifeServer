package com.example.myfootballlifeserver.service

import com.example.myfootballlifeserver.data.models.team.Team
import com.example.myfootballlifeserver.repositories.TeamInfoRepository
import com.example.myfootballlifeserver.repositories.TwitterRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class TwitterService {
    @Autowired
    lateinit var twitterRepository: TwitterRepository
    @Autowired
    lateinit var teamInfoRepository: TeamInfoRepository

    fun requestLatestUsers() {
        val twitterIdList = getTwitterIdList()
        GlobalScope.launch(Dispatchers.IO) {
            val users = twitterRepository.getUsers(twitterIdList)
            if(users.isNotEmpty()) {
                twitterRepository.insertUsers(users)
            }
        }
    }

    fun requestLatestTweets(){
        val twitterIdList = getTwitterIdList()
        GlobalScope.launch(Dispatchers.IO) {
            twitterIdList.forEach { userId ->
                val tweets = twitterRepository.getTweets(userId)
                if (tweets.isNotEmpty()) {
                    twitterRepository.insertTweets(userId, tweets)
                }
            }
        }
    }

    private fun getTwitterIdList() =
        teamInfoRepository.teamBody.teams.fold<Team, MutableList<String>>(mutableListOf()) { acc, team ->
            team.officialTwitterUserId?.let { acc.add(it) }
            team.reporterTwitterUserId?.let { acc.addAll(it) }
            acc
        }.toList()
}