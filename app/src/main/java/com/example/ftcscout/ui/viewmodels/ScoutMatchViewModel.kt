package com.example.ftcscout.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ftcscout.data.entities.Match
import com.example.ftcscout.data.entities.Team
import com.example.ftcscout.data.repository.MatchRepository
import com.example.ftcscout.data.repository.TeamRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ScoutMatchViewModel(
    private val matchRepository: MatchRepository,
    private val teamRepository: TeamRepository
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _match = MutableStateFlow<Match?>(null)
    val match: StateFlow<Match?> = _match.asStateFlow()

    private val _team = MutableStateFlow<Team?>(null)
    val team: StateFlow<Team?> = _team.asStateFlow()

    fun loadMatchData(matchId: Int, teamNumber: Int) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null
                
                // Load match data
                val match = matchRepository.getMatch(matchId)
                _match.value = match
                
                // Load team data
                val team = teamRepository.getTeam(teamNumber)
                _team.value = team
                
            } catch (e: Exception) {
                _error.value = e.message ?: "Erro ao carregar dados"
            } finally {
                _isLoading.value = false
            }
        }
    }
} 