package com.example.ftcscout.data.dao

import androidx.room.*
import com.example.ftcscout.data.entities.Event
import kotlinx.coroutines.flow.Flow

@Dao
interface EventDao {
    @Query("SELECT * FROM events ORDER BY date DESC")
    fun getAllEvents(): Flow<List<Event>>

    @Query("SELECT * FROM events WHERE eventId = :eventId")
    suspend fun getEvent(eventId: Int): Event?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvent(event: Event)

    @Delete
    suspend fun deleteEvent(event: Event)

    @Query("SELECT * FROM events WHERE name LIKE '%' || :query || '%' OR location LIKE '%' || :query || '%'")
    fun searchEvents(query: String): Flow<List<Event>>
} 