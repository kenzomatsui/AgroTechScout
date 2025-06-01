package com.example.ftcscout.data.repository

import com.example.ftcscout.data.dao.ScoutDataDao
import com.example.ftcscout.data.entities.ScoutData
import kotlinx.coroutines.flow.Flow

class ScoutDataRepository(private val scoutDataDao: ScoutDataDao) {
    suspend fun getScoutData(matchId: Int, teamNumber: Int): ScoutData? =
        scoutDataDao.getScoutData(matchId, teamNumber)

    fun getScoutDataForTeam(teamNumber: Int): Flow<List<ScoutData>> =
        scoutDataDao.getScoutDataForTeam(teamNumber)

    suspend fun insertScoutData(scoutData: ScoutData) = scoutDataDao.insertScoutData(scoutData)

    suspend fun deleteScoutData(scoutData: ScoutData) = scoutDataDao.deleteScoutData(scoutData)

    fun getAverageAutonomousPixels(teamNumber: Int): Flow<Double?> =
        scoutDataDao.getAverageAutonomousPixels(teamNumber)

    fun getAverageTeleopScore(teamNumber: Int): Flow<Double?> =
        scoutDataDao.getAverageTeleopScore(teamNumber)

    fun getEndgameSuspendedCount(teamNumber: Int): Flow<Int> =
        scoutDataDao.getEndgameSuspendedCount(teamNumber)

    fun getDroneLaunchedCount(teamNumber: Int): Flow<Int> =
        scoutDataDao.getDroneLaunchedCount(teamNumber)
} 