package com.example.ftcscout.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.ftcscout.data.entities.Event
import com.example.ftcscout.data.entities.Match
import com.example.ftcscout.data.repository.EventRepository
import com.example.ftcscout.data.repository.MatchRepository
import com.example.ftcscout.data.repository.ScoutDataRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class EventDetailsViewModel(
    private val eventRepository: EventRepository,
    private val matchRepository: MatchRepository,
    private val scoutDataRepository: ScoutDataRepository,
    private val eventId: Int
) : ViewModel() {

    private val _event = MutableStateFlow<Event?>(null)
    val event: StateFlow<Event?> = _event.asStateFlow()

    private val _matches = MutableStateFlow<List<Match>>(emptyList())
    val matches: StateFlow<List<Match>> = _matches.asStateFlow()

    init {
        loadEvent()
        loadMatches()
    }

    private fun loadEvent() {
        viewModelScope.launch {
            val event = eventRepository.getEvent(eventId)
            _event.value = event
        }
    }

    private fun loadMatches() {
        viewModelScope.launch {
            matchRepository.getMatchesForEvent(eventId).collect { matches ->
                _matches.value = matches
            }
        }
    }

    fun addMatch(match: Match) {
        viewModelScope.launch {
            matchRepository.insertMatch(match)
        }
    }

    fun deleteMatch(match: Match) {
        viewModelScope.launch {
            scoutDataRepository.deleteScoutDataByMatchId(match.matchId)
            matchRepository.deleteMatch(match)
        }
    }

    fun deleteEvent() {
        viewModelScope.launch {
            // Primeiro, obtenha os IDs das partidas para este evento
            val matchesInEvent = matchRepository.getMatchesForEvent(eventId).first() // Coleta o estado atual
            matchesInEvent.forEach { match ->
                scoutDataRepository.deleteScoutDataByMatchId(match.matchId)
            }
            eventRepository.deleteEventById(eventId)
        }
    }

    class Factory(
        private val eventRepository: EventRepository,
        private val matchRepository: MatchRepository,
        private val scoutDataRepository: ScoutDataRepository,
        private val eventId: Int
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(EventDetailsViewModel::class.java)) {
                return EventDetailsViewModel(eventRepository, matchRepository, scoutDataRepository, eventId) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
} 