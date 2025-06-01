package com.example.ftcscout.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ftcscout.data.entities.Match
import com.example.ftcscout.data.repository.MatchRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MatchDetailsViewModel(
    private val matchRepository: MatchRepository
) : ViewModel() {

    private val _match = MutableStateFlow<Match?>(null)
    val match: StateFlow<Match?> = _match.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    fun loadMatch(matchId: Int) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null
                val match = matchRepository.getMatch(matchId)
                _match.value = match
            } catch (e: Exception) {
                _error.value = e.message ?: "Erro ao carregar partida"
            } finally {
                _isLoading.value = false
            }
        }
    }
} 