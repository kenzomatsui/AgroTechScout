package com.example.ftcscout.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ftcscout.data.entities.Team
import com.example.ftcscout.data.repository.TeamRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class TeamDetailsViewModel(
    private val teamRepository: TeamRepository
) : ViewModel() {

    private val _team = MutableStateFlow<Team?>(null)
    val team: StateFlow<Team?> = _team.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    fun loadTeam(teamNumber: Int) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null
                val team = teamRepository.getTeam(teamNumber)
                _team.value = team
            } catch (e: Exception) {
                _error.value = e.message ?: "Erro ao carregar time"
            } finally {
                _isLoading.value = false
            }
        }
    }
} 