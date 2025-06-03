package com.example.ftcscout.data.dao

import androidx.room.*
import com.example.ftcscout.data.entities.ScoutData
import kotlinx.coroutines.flow.Flow

@Dao
interface ScoutDataDao {
    @Query("SELECT * FROM scout_data WHERE matchId = :matchId AND teamNumber = :teamNumber")
    suspend fun getScoutData(matchId: Int, teamNumber: Int): ScoutData?

    @Query("SELECT * FROM scout_data WHERE teamNumber = :teamNumber")
    fun getScoutDataForTeam(teamNumber: Int): Flow<List<ScoutData>>

    @Query("SELECT * FROM scout_data")
    fun getAllScoutData(): Flow<List<ScoutData>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertScoutData(scoutData: ScoutData)

    @Delete
    suspend fun deleteScoutData(scoutData: ScoutData)

    @Query("DELETE FROM scout_data WHERE matchId = :matchId")
    suspend fun deleteScoutDataByMatchId(matchId: Int)

    @Query("DELETE FROM scout_data WHERE id = :scoutDataId")
    suspend fun deleteScoutDataById(scoutDataId: Int)

    @Query("SELECT * FROM scout_data WHERE matchId = :matchId")
    fun getScoutDataForMatch(matchId: Int): Flow<List<ScoutData>>
} 