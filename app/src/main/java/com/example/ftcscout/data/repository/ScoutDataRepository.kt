package com.example.ftcscout.data.repository

import com.example.ftcscout.data.dao.ScoutDataDao
import com.example.ftcscout.data.entities.ScoutData
import kotlinx.coroutines.flow.Flow

class ScoutDataRepository(private val scoutDataDao: ScoutDataDao) {
    suspend fun getScoutData(matchId: Int, teamNumber: Int): ScoutData? =
        scoutDataDao.getScoutData(matchId, teamNumber)

    fun getScoutDataForTeam(teamNumber: Int): Flow<List<ScoutData>> =
        scoutDataDao.getScoutDataForTeam(teamNumber)

    fun getAllScoutData(): Flow<List<ScoutData>> = scoutDataDao.getAllScoutData()

    suspend fun insertScoutData(scoutData: ScoutData) = scoutDataDao.insertScoutData(scoutData)

    suspend fun deleteScoutData(scoutData: ScoutData) = scoutDataDao.deleteScoutData(scoutData)

    suspend fun deleteScoutDataByMatchId(matchId: Int) {
        scoutDataDao.deleteScoutDataByMatchId(matchId)
    }

    suspend fun deleteScoutDataById(scoutDataId: Int) {
        scoutDataDao.deleteScoutDataById(scoutDataId)
    }

    fun getScoutDataForMatch(matchId: Int): Flow<List<ScoutData>> {
        return scoutDataDao.getScoutDataForMatch(matchId)
    }
} 