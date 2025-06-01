package com.example.ftcscout.data.dao

import androidx.room.*
import com.example.ftcscout.data.entities.Team
import kotlinx.coroutines.flow.Flow

@Dao
interface TeamDao {
    @Query("SELECT * FROM teams")
    fun getAllTeams(): Flow<List<Team>>

    @Query("SELECT * FROM teams WHERE teamNumber = :teamNumber")
    suspend fun getTeam(teamNumber: Int): Team?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTeam(team: Team)

    @Delete
    suspend fun deleteTeam(team: Team)

    @Query("SELECT * FROM teams WHERE teamNumber LIKE '%' || :query || '%' OR name LIKE '%' || :query || '%'")
    fun searchTeams(query: String): Flow<List<Team>>
} 