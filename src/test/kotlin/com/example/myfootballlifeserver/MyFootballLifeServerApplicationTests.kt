package com.example.myfootballlifeserver

import com.example.myfootballlifeserver.data.api.TwitterApiModule
import com.example.myfootballlifeserver.data.api.YoutubeDataApiModule
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class MyFootballLifeServerApplicationTests {

	@Test
	fun testTwitterApi() = runTest{
		val retrofit = TwitterApiModule.provideTwitterApiService()
		val userResponseBody = retrofit.requestRetrieveUsersByUsernames("SpursOfficial")
		val users = userResponseBody.users
		MatcherAssert.assertThat(users?.get(0)?.name, CoreMatchers.`is`("Tottenham Hotspur"))

		val timelineResponseBody = retrofit.requestUserTweetTimeline(users?.get(0)?.id ?: "")
		MatcherAssert.assertThat(timelineResponseBody.tweets!![0].authorId, CoreMatchers.`is`(users?.get(0)?.id ?: ""))
		val usersResponseBody = retrofit.requestUsersByIds(users?.get(0)?.id ?: "")
		MatcherAssert.assertThat(usersResponseBody.users?.get(0)?.name ?: "", CoreMatchers.`is`("Tottenham Hotspur"))
		assert( usersResponseBody.users!![0].name == "Tottenham Hotspur")
	}

	@Test
	fun testYoutubeDataApi() = runTest {
		// channel
		val channelResponseBody = YoutubeDataApiModule.provideYoutubeDataService().requestListChannels("zilioner83")
		val uploadsPlaylistId = channelResponseBody.items!![0].contentDetails.relatedPlaylists["uploads"]
		MatcherAssert.assertThat(channelResponseBody.items!![0].snippet.title, CoreMatchers.`is`("침착맨"))
		MatcherAssert.assertThat(uploadsPlaylistId, CoreMatchers.`is`("UUUj6rrhMTR9pipbAWBAMvUQ"))

		//playlist
		val playlistResponseBody = YoutubeDataApiModule.provideYoutubeDataService().requestListPlaylists(uploadsPlaylistId)
		MatcherAssert.assertThat(playlistResponseBody.items[0].snippet.title, CoreMatchers.`is`("Uploads from 침착맨"))

		//playlistItems
		val playlistItemResponseBody = YoutubeDataApiModule.provideYoutubeDataService().requestListPlaylistItems(null, uploadsPlaylistId)
		MatcherAssert.assertThat(
			playlistItemResponseBody.items!![0].snippet.playlistId,
			CoreMatchers.`is`(uploadsPlaylistId)
		)

		//video
		val videoId = playlistItemResponseBody.items!![0].contentDetails.videoId
		val videoResponseBody = YoutubeDataApiModule.provideYoutubeDataService().requestListVideos(videoId)
		MatcherAssert.assertThat(
			videoResponseBody.items!![0].snippet.channelId,
			CoreMatchers.`is`(channelResponseBody.items!![0].id)
		)
	}
}

