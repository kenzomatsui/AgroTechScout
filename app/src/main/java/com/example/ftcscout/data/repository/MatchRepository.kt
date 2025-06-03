package com.example.ftcscout.data.repository

import com.example.ftcscout.data.dao.MatchDao
import com.example.ftcscout.data.entities.Match
import kotlinx.coroutines.flow.Flow

class MatchRepository(private val matchDao: MatchDao) {
    fun getMatchesForEvent(eventId: Int): Flow<List<Match>> = matchDao.getMatchesForEvent(eventId)

    suspend fun getMatch(matchId: Int): Match? = matchDao.getMatch(matchId)

    fun getAllMatches(): Flow<List<Match>> = matchDao.getAllMatches()

    suspend fun insertMatch(match: Match) = matchDao.insertMatch(match)

    suspend fun updateMatch(match: Match) = matchDao.updateMatch(match)

    suspend fun deleteMatch(match: Match) = matchDao.deleteMatch(match)

    fun getMatchesForTeam(eventId: Int, teamNumber: Int): Flow<List<Match>> =
        matchDao.getMatchesForTeam(eventId, teamNumber)
} 