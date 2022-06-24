package com.example.myfootballlifeserver.data.api

import com.example.myfootballlifeserver.ApiKey
import com.example.myfootballlifeserver.data.models.twitterapi.timelines.TimelinesResponseBody
import com.example.myfootballlifeserver.data.models.twitterapi.tweets.TweetsFields
import com.example.myfootballlifeserver.data.models.twitterapi.users.UsersFields
import com.example.myfootballlifeserver.data.models.twitterapi.users.UsersResponseBody
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

/** https://developer.twitter.com/en/docs/twitter-api */
interface TwitterApiService {
    companion object {
        private const val TWITTER_API_BEARER_TOKEN = ApiKey.TWITTER_API_BEARER_TOKEN
    }

    /** https://developer.twitter.com/en/docs/twitter-api/tweets/timelines/api-reference/get-users-id-tweets **/
    @GET("2/users/{id}/tweets")
    @Headers("Authorization: Bearer $TWITTER_API_BEARER_TOKEN")
    suspend fun requestUserTweetTimeline(@Path("id") id:String, @Query("pagination_token") paginationToken:String? = null, @Query("since_id") sinceId:String? = null, @Query("until_id") untilId:String? = null, @Query("tweet.fields") tweetFields:String = "${TweetsFields.AUTHOR_ID},${TweetsFields.CREATED_AT},${TweetsFields.LANG}"): TimelinesResponseBody

    /** https://developer.twitter.com/en/docs/twitter-api/users/lookup/api-reference/get-users-by-username-username */
    @GET("2/users/by")
    @Headers("Authorization: Bearer $TWITTER_API_BEARER_TOKEN")
    suspend fun requestRetrieveUsersByUsernames(@Query("usernames") usernames:String, @Query("user.fields") userFields:String = "${UsersFields.CREATED_AT},${UsersFields.DESCRIPTION},${UsersFields.PROFILE_IMAGE_URL}"): UsersResponseBody

    /** https://developer.twitter.com/en/docs/twitter-api/users/lookup/api-reference/get-users **/
    @GET("2/users")
    @Headers("Authorization: Bearer $TWITTER_API_BEARER_TOKEN")
    suspend fun requestUsersByIds(@Query("ids") ids: String, @Query("user.fields") userFields:String = "${UsersFields.CREATED_AT},${UsersFields.DESCRIPTION},${UsersFields.PROFILE_IMAGE_URL}"): UsersResponseBody
}