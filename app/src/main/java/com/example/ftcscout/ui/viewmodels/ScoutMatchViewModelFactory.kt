package com.example.ftcscout.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.ftcscout.data.repository.MatchRepository
import com.example.ftcscout.data.repository.TeamRepository

class ScoutMatchViewModelFactory(
    private val matchRepository: MatchRepository,
    private val teamRepository: TeamRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ScoutMatchViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ScoutMatchViewModel(matchRepository, teamRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
} 