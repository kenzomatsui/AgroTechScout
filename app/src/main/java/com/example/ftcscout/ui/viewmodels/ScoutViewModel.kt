package com.example.ftcscout.ui.viewmodels

import android.util.Log // Importar Log para depuração
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.ftcscout.data.entities.ScoutData
import com.example.ftcscout.data.repository.ScoutDataRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import com.example.ftcscout.data.entities.Match // Importar Match
import com.example.ftcscout.data.repository.MatchRepository // Importar MatchRepository

private const val TAG = "ScoutViewModel" // Tag para logs

class ScoutViewModel(
    private val scoutDataRepository: ScoutDataRepository,
    private val matchRepository: MatchRepository, // Adicionar MatchRepository
    private val matchId: Int,
    private val teamNumber: Int
) : ViewModel() {

    private val _scoutData = MutableStateFlow<ScoutData?>(null)
    val scoutData: StateFlow<ScoutData?> = _scoutData.asStateFlow()

    private val _matchData = MutableStateFlow<Match?>(null) // StateFlow para dados da partida
    val matchData: StateFlow<Match?> = _matchData.asStateFlow()

    init {
        Log.d(TAG, "ViewModel initialized for match $matchId, team $teamNumber") // Log de inicialização
        loadScoutData()
        loadMatchData() // Carregar dados da partida
    }

    private fun loadScoutData() {
        viewModelScope.launch {
            Log.d(TAG, "Attempting to load scout data for match $matchId, team $teamNumber") // Log antes de carregar
            val existingScoutData = scoutDataRepository.getScoutData(matchId, teamNumber)
            if (existingScoutData != null) {
                Log.d(TAG, "Existing scout data found: $existingScoutData") // Log se encontrado
                _scoutData.value = existingScoutData
            } else {
                // Crie um novo ScoutData se não existir
                val newScoutData = ScoutData(matchId = matchId, teamNumber = teamNumber)
                Log.d(TAG, "No existing data, creating new: $newScoutData") // Log se criando novo
                _scoutData.value = newScoutData
            }
             Log.d(TAG, "Scout data state updated: ${_scoutData.value}") // Log após atualização
        }
    }

    private fun loadMatchData() {
        viewModelScope.launch {
            Log.d(TAG, "Attempting to load match data for match $matchId")
            val existingMatchData = matchRepository.getMatch(matchId)
            if (existingMatchData != null) {
                Log.d(TAG, "Existing match data found: $existingMatchData")
                _matchData.value = existingMatchData
            } else {
                Log.w(TAG, "No existing match data found for match $matchId")
                // Decidir como lidar com partida inexistente, talvez mostrar um erro
            }
        }
    }

    private fun calculateScore(data: ScoutData): Int {
        var score = 0
        // Autonomous
        score += data.autoPixelsBackdrop * 3 // Ex: 3 pontos por pixel no Backdrop
        if (data.autoParked) score += 5 // Ex: 5 pontos por estacionar

        // TeleOp
        score += data.teleopPixelsBackdrop * 2 // Ex: 2 pontos por pixel no Backdrop
        score += data.teleopPixelsMosaic * 1 // Ex: 1 ponto por pixel em Mosaic
        // Penalidades afetam a pontuação adversária na regra oficial, mas aqui só registramos
        // score -= data.teleopMajorPenalties * X // Depende da regra oficial
        // score -= data.teleopMinorPenalties * Y // Depende da regra oficial

        // Endgame
        if (data.endgameDroneLaunched) score += 10 // Ex: 10 pontos por drone lançado
        if (data.endgameHanging) score += 20 // Ex: 20 pontos por hanging

        return score
    }

    private fun updateScoutData(update: (ScoutData) -> ScoutData) {
        _scoutData.update { currentScoutData ->
            currentScoutData?.let {
                val updatedData = update(it)
                val calculatedScore = calculateScore(updatedData)
                val finalData = updatedData.copy(
                    calculatedScore = calculatedScore,
                    // Se a pontuação final ainda for 0 (inicial ou após migração destrutiva),
                    // use a pontuação calculada. Caso contrário, mantenha a pontuação final manual.
                    finalScore = if (updatedData.finalScore == 0 && currentScoutData.finalScore == 0) calculatedScore else updatedData.finalScore
                )
                viewModelScope.launch {
                    Log.d(TAG, "Updating scout data in repository: $finalData")
                    scoutDataRepository.insertScoutData(finalData)
                }
                Log.d(TAG, "Scout data state updated after insert: $finalData")
                finalData
            }
        }
    }

    private fun updateMatchData(update: (Match) -> Match) {
        _matchData.value?.let { currentMatchData ->
            viewModelScope.launch {
                val updatedMatchData = update(currentMatchData)
                Log.d(TAG, "Updating match data in repository: $updatedMatchData")
                matchRepository.updateMatch(updatedMatchData)
                _matchData.value = updatedMatchData // Atualizar StateFlow após salvar
                Log.d(TAG, "Match data state updated after update: ${_matchData.value}")
            }
        } ?: Log.w(TAG, "updateMatchData called with null matchData")
    }

    // Autonomous
    fun updateAutoPixelsBackdrop(count: Int) {
        updateScoutData { it.copy(autoPixelsBackdrop = count) }
        Log.d(TAG, "updateAutoPixelsBackdrop called: $count")
    }

    fun updateAutoParked(isParked: Boolean) {
        updateScoutData { it.copy(autoParked = isParked) }
        Log.d(TAG, "updateAutoParked called: $isParked")
    }

    // TeleOp
    fun updateTeleopPixelsBackdrop(count: Int) {
        updateScoutData { it.copy(teleopPixelsBackdrop = count) }
        Log.d(TAG, "updateTeleopPixelsBackdrop called: $count")
    }

    fun updateTeleopPixelsMosaic(count: Int) {
        updateScoutData { it.copy(teleopPixelsMosaic = count) }
        Log.d(TAG, "updateTeleopPixelsMosaic called: $count")
    }

     fun updateTeleopMajorPenalties(count: Int) {
        updateScoutData { it.copy(teleopMajorPenalties = count) }
        Log.d(TAG, "updateTeleopMajorPenalties called: $count")
    }

     fun updateTeleopMinorPenalties(count: Int) {
        updateScoutData { it.copy(teleopMinorPenalties = count) }
        Log.d(TAG, "updateTeleopMinorPenalties called: $count")
    }

    // Endgame
     fun updateEndgameDroneLaunched(isLaunched: Boolean) {
        updateScoutData { it.copy(endgameDroneLaunched = isLaunched) }
        Log.d(TAG, "updateEndgameDroneLaunched called: $isLaunched")
    }

     fun updateEndgameHanging(isHanging: Boolean) {
        updateScoutData { it.copy(endgameHanging = isHanging) }
        Log.d(TAG, "updateEndgameHanging called: $isHanging")
    }

    // Pontuação Final
    fun updateFinalScore(score: Int) {
        updateScoutData { it.copy(finalScore = score) }
        Log.d(TAG, "updateFinalScore called: $score")
    }

    // Outros
    fun updateNotes(notes: String) {
        updateScoutData { it.copy(notes = notes) }
        Log.d(TAG, "updateNotes called: $notes")
    }
    
    fun updateCooperationLevel(level: Int) {
        updateScoutData { it.copy(cooperationLevel = level) }
        Log.d(TAG, "updateCooperationLevel called: $level")
    }

    // Pontuação das Alianças
    fun updateRedScore(score: Int) {
        updateMatchData { it.copy(redScore = score) }
        Log.d(TAG, "updateRedScore called: $score")
    }

    fun updateBlueScore(score: Int) {
        updateMatchData { it.copy(blueScore = score) }
        Log.d(TAG, "updateBlueScore called: $score")
    }

    class Factory(
        private val scoutDataRepository: ScoutDataRepository,
        private val matchRepository: MatchRepository, // Adicionar MatchRepository à Factory
        private val matchId: Int,
        private val teamNumber: Int
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ScoutViewModel::class.java)) {
                 Log.d(TAG, "Creating ScoutViewModel factory for match $matchId, team $teamNumber") // Log da factory
                return ScoutViewModel(scoutDataRepository, matchRepository, matchId, teamNumber) as T // Passar matchRepository
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
} 