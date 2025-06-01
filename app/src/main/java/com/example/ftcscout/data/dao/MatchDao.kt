package com.example.ftcscout.data.dao

import androidx.room.*
import com.example.ftcscout.data.entities.Match
import kotlinx.coroutines.flow.Flow

@Dao
interface MatchDao {
    @Query("SELECT * FROM matches WHERE eventId = :eventId ORDER BY number")
    fun getMatchesForEvent(eventId: Int): Flow<List<Match>>

    @Query("SELECT * FROM matches WHERE matchId = :matchId")
    suspend fun getMatch(matchId: Int): Match?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMatch(match: Match)

    @Delete
    suspend fun deleteMatch(match: Match)

    @Query("SELECT * FROM matches WHERE eventId = :eventId AND (teamRed1 = :teamNumber OR teamRed2 = :teamNumber OR teamBlue1 = :teamNumber OR teamBlue2 = :teamNumber)")
    fun getMatchesForTeam(eventId: Int, teamNumber: Int): Flow<List<Match>>
} 