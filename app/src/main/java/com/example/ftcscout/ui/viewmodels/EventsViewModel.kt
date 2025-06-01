package com.example.ftcscout.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.ftcscout.data.entities.Event
import com.example.ftcscout.data.repository.EventRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class EventsViewModel(
    private val eventRepository: EventRepository
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    val events: StateFlow<List<Event>> = searchQuery
        .flatMapLatest { query ->
            if (query.isBlank()) {
                eventRepository.getAllEvents()
            } else {
                eventRepository.searchEvents(query)
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun addEvent(event: Event) {
        viewModelScope.launch {
            eventRepository.insertEvent(event)
        }
    }

    fun deleteEvent(event: Event) {
        viewModelScope.launch {
            eventRepository.deleteEvent(event)
        }
    }

    class Factory(private val eventRepository: EventRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(EventsViewModel::class.java)) {
                return EventsViewModel(eventRepository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
} 