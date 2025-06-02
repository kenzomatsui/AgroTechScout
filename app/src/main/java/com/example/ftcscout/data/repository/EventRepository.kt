package com.example.ftcscout.data.repository

import com.example.ftcscout.data.dao.EventDao
import com.example.ftcscout.data.entities.Event
import kotlinx.coroutines.flow.Flow

class EventRepository(private val eventDao: EventDao) {
    fun getAllEvents(): Flow<List<Event>> = eventDao.getAllEvents()

    suspend fun getEvent(eventId: Int): Event? = eventDao.getEvent(eventId)

    suspend fun insertEvent(event: Event) = eventDao.insertEvent(event)

    suspend fun deleteEvent(event: Event) = eventDao.deleteEvent(event)

    suspend fun deleteEventById(eventId: Int) = eventDao.deleteEventById(eventId)

    fun searchEvents(query: String): Flow<List<Event>> = eventDao.searchEvents(query)
} 