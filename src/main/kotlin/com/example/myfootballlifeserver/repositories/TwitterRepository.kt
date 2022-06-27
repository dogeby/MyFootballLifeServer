package com.example.myfootballlifeserver.repositories

import com.example.myfootballlifeserver.data.api.TwitterApiModule
import com.example.myfootballlifeserver.data.models.twitterapi.TweetsBody
import com.example.myfootballlifeserver.data.models.twitterapi.UsersBody
import com.example.myfootballlifeserver.data.models.twitterapi.tweets.Tweets
import com.example.myfootballlifeserver.data.models.twitterapi.users.Users
import com.example.myfootballlifeserver.utils.Converter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository

@Repository
class TwitterRepository {
    private val twitterApiService = TwitterApiModule.provideTwitterApiService()
    @Autowired
    private lateinit var firebaseRepository: FirebaseRepository
    @Autowired
    private lateinit var localPropertiesRepository: LocalPropertiesRepository
    companion object {
        const val TWITTER_LATEST_TWEET_FILE_NAME = "twitterLatestTweet.properties"
    }
    /** User **/
    suspend fun getUsers(userIds:List<String>): List<Users> { //userId 100개까지 가능
        val userIdsList = mutableListOf<Users>()
        val chunkedUserIds = userIds.chunked(100)
        chunkedUserIds.forEach {
            val resultUserIds = twitterApiService.requestUsersByIds(Converter.stringListToCommaSeparatedString(it)).users ?: listOf()
            userIdsList.addAll(resultUserIds)
        }
        return userIdsList.toList()
    }

    suspend fun insertUsers(users: List<Users>) {
        val usersBody = UsersBody(users)
        firebaseRepository.insertData("twitterUsers", usersBody)
    }

    /** Tweet **/
    suspend fun getTweets(authorId:String):List<Tweets> {
        val latestTweetsId:String? = localPropertiesRepository.getProperty(TWITTER_LATEST_TWEET_FILE_NAME, authorId)
        if(latestTweetsId.isNullOrBlank()) {
            val tweets = twitterApiService.requestUserTweetTimeline(authorId).tweets
            tweets?.first()?.let {
                localPropertiesRepository.insertProperty(TWITTER_LATEST_TWEET_FILE_NAME, authorId, it.id)
                return listOf(it)
            }
            return listOf()
        }
        val tweets = mutableListOf<Tweets>()
        var nextToken:String? = null
        do {
            val timelinesResponseBody = twitterApiService.requestUserTweetTimeline(authorId, nextToken, latestTweetsId)
            nextToken = timelinesResponseBody.meta.nextToken
            timelinesResponseBody.tweets?.let {
                tweets.addAll(it)
            }
        } while (nextToken != null)
        if(tweets.isNotEmpty()){
            localPropertiesRepository.insertProperty(TWITTER_LATEST_TWEET_FILE_NAME, authorId, tweets.first().id)
        }
        return tweets.toList()
    }

    suspend fun insertTweets(authorId:String, tweets: List<Tweets>) {
        val tweetsBody = TweetsBody(tweets)
        firebaseRepository.insertData("latestTweets/$authorId", tweetsBody)
    }
}