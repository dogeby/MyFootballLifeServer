package com.example.myfootballlifeserver.repositories

import com.example.myfootballlifeserver.data.models.team.TeamBody
import com.example.myfootballlifeserver.utils.Converter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository

@Repository
class TeamInfoRepository {
    @Autowired
    private lateinit var firebaseRepository:FirebaseRepository

    val teamBody:TeamBody by lazy {
        val teamBody = Converter.jsonFileToObject("./src/main/resources/team-info.json", TeamBody::class.java)
        updateTeamInfo(teamBody)
        teamBody
    }

    private fun updateTeamInfo(teamBody: TeamBody) {
        firebaseRepository.insertData("teamInfo", teamBody)
    }
}