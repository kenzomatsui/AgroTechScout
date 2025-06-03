package com.example.ftcscout.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.ftcscout.data.repository.ScoutDataRepository
import com.example.ftcscout.data.entities.TeamStats
import com.example.ftcscout.data.entities.OverallStats
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import android.util.Log
import com.example.ftcscout.data.entities.ScoutData
import com.example.ftcscout.data.entities.Match
import kotlin.collections.List
import kotlin.comparisons.thenByDescending

private const val TAG = "AnalysisViewModel"

// Data classes para representar os resultados da análise
data class TeamAnalysis(
    val teamNumber: Int,
    val averageScore: Double = 0.0,
    val totalRPs: Int = 0,
    val autonomousStats: PhaseStats = PhaseStats(),
    val teleopStats: PhaseStats = PhaseStats(),
    val endgameStats: PhaseStats = PhaseStats(),
    val successRates: Map<String, Double> = emptyMap() // Ex: map de "ação" para "porcentagem de sucesso"
)

data class PhaseStats(
    val averageScore: Double = 0.0,
    val averagePixelsBackdrop: Double = 0.0,
    val averagePixelsMosaic: Double = 0.0,
    // Adicionar outros campos conforme necessário, ex: média de pixels no backdrop, etc.
)

class AnalysisViewModel(
    private val scoutDataRepository: ScoutDataRepository
) : ViewModel() {

    private val _teamStats = MutableStateFlow<List<TeamStats>>(emptyList())
    val teamStats: StateFlow<List<TeamStats>> = _teamStats.asStateFlow()

    private val _overallStats = MutableStateFlow(OverallStats())
    val overallStats: StateFlow<OverallStats> = _overallStats.asStateFlow()

    init {
        Log.d(TAG, "AnalysisViewModel initialized")
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            Log.d(TAG, "Loading analysis data...")
            scoutDataRepository.getAllScoutData()
                .collect { scoutDataList ->
                    if (scoutDataList.isNotEmpty()) {
                        calculateTeamStats(scoutDataList)
                        calculateOverallStats(scoutDataList)
                    }
                }
        }
    }

    private fun calculateTeamStats(scoutDataList: List<ScoutData>) {
        val teamMap = scoutDataList.groupBy { it.teamNumber }
        val stats = teamMap.map { (teamNumber, data) ->
            TeamStats(
                teamNumber = teamNumber,
                totalMatches = data.size,
                averageScore = data.map { it.finalScore }.average(),
                highestScore = data.maxOf { it.finalScore },
                averageCooperation = data.map { it.cooperationLevel }.average(),
                autoAveragePixels = data.map { it.autoPixelsBackdrop }.average(),
                autoParkingRate = data.count { it.autoParked }.toDouble() / data.size,
                teleopAveragePixelsBackdrop = data.map { it.teleopPixelsBackdrop }.average(),
                teleopAveragePixelsMosaic = data.map { it.teleopPixelsMosaic }.average(),
                endgameDroneRate = data.count { it.endgameDroneLaunched }.toDouble() / data.size,
                endgameHangingRate = data.count { it.endgameHanging }.toDouble() / data.size
            )
        }.sortedByDescending { it.averageScore }

        _teamStats.value = stats
        Log.d(TAG, "Analysis data updated: ${stats.size} teams analyzed")
    }

    private fun calculateOverallStats(scoutDataList: List<ScoutData>) {
        val uniqueMatchIds = scoutDataList.distinctBy { it.matchId }
        Log.d(TAG, "Calculating overall stats. Total scout entries: ${scoutDataList.size}, Unique match IDs: ${uniqueMatchIds.size}")
        Log.d(TAG, "Unique match IDs found: ${uniqueMatchIds.map { it.matchId }}")

        val stats = OverallStats(
            totalMatches = uniqueMatchIds.size,
            averageScore = scoutDataList.map { it.finalScore }.average(),
            highestScore = scoutDataList.maxOf { it.finalScore },
            lowestScore = scoutDataList.minOf { it.finalScore },
            autoAveragePixels = scoutDataList.map { it.autoPixelsBackdrop }.average(),
            autoParkingRate = scoutDataList.count { it.autoParked }.toDouble() / scoutDataList.size,
            teleopAveragePixelsBackdrop = scoutDataList.map { it.teleopPixelsBackdrop }.average(),
            teleopAveragePixelsMosaic = scoutDataList.map { it.teleopPixelsMosaic }.average(),
            endgameDroneRate = scoutDataList.count { it.endgameDroneLaunched }.toDouble() / scoutDataList.size,
            endgameHangingRate = scoutDataList.count { it.endgameHanging }.toDouble() / scoutDataList.size
        )

        _overallStats.value = stats
    }

    // TODO: Implementar funções para preparar dados para gráficos (se necessário, dependendo da biblioteca)

    class Factory(
        private val scoutDataRepository: ScoutDataRepository
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(AnalysisViewModel::class.java)) {
                return AnalysisViewModel(scoutDataRepository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
} 