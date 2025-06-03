package com.example.ftcscout.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.ftcscout.data.entities.Event
import com.example.ftcscout.data.repository.EventRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class EventListViewModel(private val eventRepository: EventRepository) : ViewModel() {

    private val _events = MutableStateFlow<List<Event>>(emptyList())
    val events: StateFlow<List<Event>> = _events.asStateFlow()

    init {
        loadEvents()
    }

    private fun loadEvents() {
        viewModelScope.launch {
            eventRepository.getAllEvents().collect { events ->
                _events.value = events
            }
        }
    }

    fun addEvent(event: Event) {
        viewModelScope.launch {
            eventRepository.insertEvent(event)
        }
    }

    class Factory(private val eventRepository: EventRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(EventListViewModel::class.java)) {
                return EventListViewModel(eventRepository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
} 