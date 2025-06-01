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

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertScoutData(scoutData: ScoutData)

    @Delete
    suspend fun deleteScoutData(scoutData: ScoutData)

    @Query("SELECT AVG(autonomousPixels) FROM scout_data WHERE teamNumber = :teamNumber")
    fun getAverageAutonomousPixels(teamNumber: Int): Flow<Double?>

    @Query("SELECT AVG(teleopLow + teleopMid + teleopHigh) FROM scout_data WHERE teamNumber = :teamNumber")
    fun getAverageTeleopScore(teamNumber: Int): Flow<Double?>

    @Query("SELECT COUNT(*) FROM scout_data WHERE teamNumber = :teamNumber AND endgameSuspended = 1")
    fun getEndgameSuspendedCount(teamNumber: Int): Flow<Int>

    @Query("SELECT COUNT(*) FROM scout_data WHERE teamNumber = :teamNumber AND droneLaunched = 1")
    fun getDroneLaunchedCount(teamNumber: Int): Flow<Int>
} 