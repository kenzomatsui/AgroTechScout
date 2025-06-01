package com.example.ftcscout.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "events")
data class Event(
    @PrimaryKey(autoGenerate = true) val eventId: Int = 0,
    val name: String,
    val location: String,
    val date: String
) 