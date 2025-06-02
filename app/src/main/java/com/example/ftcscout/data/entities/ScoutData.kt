package com.example.ftcscout.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "scout_data")
data class ScoutData(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val matchId: Int,
    val teamNumber: Int,
    
    // Autonomous
    val isParked: Boolean = false,
    
    // Outros
    val notes: String = "",
    val cooperationLevel: Int = 0
) 