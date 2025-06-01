package com.example.ftcscout.data.repository

import com.example.ftcscout.data.dao.TeamDao
import com.example.ftcscout.data.entities.Team
import kotlinx.coroutines.flow.Flow

class TeamRepository(private val teamDao: TeamDao) {
    fun getAllTeams(): Flow<List<Team>> = teamDao.getAllTeams()

    suspend fun getTeam(teamNumber: Int): Team? = teamDao.getTeam(teamNumber)

    suspend fun insertTeam(team: Team) = teamDao.insertTeam(team)

    suspend fun deleteTeam(team: Team) = teamDao.deleteTeam(team)

    fun searchTeams(query: String): Flow<List<Team>> = teamDao.searchTeams(query)
} 