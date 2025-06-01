package com.example.ftcscout.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ftcscout.data.repository.MatchRepository
import com.example.ftcscout.data.repository.TeamRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AnalysisViewModel(
    private val matchRepository: MatchRepository,
    private val teamRepository: TeamRepository
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init {
        loadAnalysisData()
    }

    private fun loadAnalysisData() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null
                // Load and analyze data
                // This will be implemented based on specific analysis requirements
            } catch (e: Exception) {
                _error.value = e.message ?: "Erro ao carregar dados de análise"
            } finally {
                _isLoading.value = false
            }
        }
    }
} 