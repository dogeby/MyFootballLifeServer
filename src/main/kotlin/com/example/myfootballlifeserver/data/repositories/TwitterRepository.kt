package com.example.myfootballlifeserver.data.repositories

import com.example.myfootballlifeserver.data.api.TwitterApiModule

class TwitterRepository {
    suspend fun getUser(userNames:String) =
        TwitterApiModule.provideTwitterApiService().requestRetrieveUsersByUsernames(userNames)

}