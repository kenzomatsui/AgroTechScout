package com.example.ftcscout.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.ftcscout.data.entities.Match
import com.example.ftcscout.data.entities.ScoutData
import com.example.ftcscout.data.repository.MatchRepository
import com.example.ftcscout.data.repository.ScoutDataRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MatchDetailsViewModel(
    private val matchRepository: MatchRepository,
    private val scoutDataRepository: ScoutDataRepository,
    private val matchId: Int
) : ViewModel() {

    private val _match = MutableStateFlow<Match?>(null)
    val match: StateFlow<Match?> = _match.asStateFlow()

    private val _scoutDataList = MutableStateFlow<List<ScoutData>>(emptyList())
    val scoutDataList: StateFlow<List<ScoutData>> = _scoutDataList.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init {
        loadMatchDetails(matchId)
    }

    private fun loadMatchDetails(matchId: Int) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null

                val match = matchRepository.getMatch(matchId)
                _match.value = match

                scoutDataRepository.getScoutDataForMatch(matchId).collect { scoutData ->
                    _scoutDataList.value = scoutData
                }

            } catch (e: Exception) {
                _error.value = e.message ?: "Erro ao carregar detalhes da partida"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun deleteScoutDataEntry(scoutDataId: Int) {
        viewModelScope.launch {
            scoutDataRepository.deleteScoutDataById(scoutDataId)
        }
    }

    class Factory(
        private val matchRepository: MatchRepository,
        private val scoutDataRepository: ScoutDataRepository,
        private val matchId: Int
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MatchDetailsViewModel::class.java)) {
                return MatchDetailsViewModel(matchRepository, scoutDataRepository, matchId) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
} 