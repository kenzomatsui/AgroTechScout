package com.example.ftcscout.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "scout_data")
data class ScoutData(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val matchId: Int,
    val teamNumber: Int,
    val autonomousPixels: Int,
    val autonomousZone: String,
    val droneLaunched: Boolean,
    val teleopLow: Int,
    val teleopMid: Int,
    val teleopHigh: Int,
    val endgameSuspended: Boolean,
    val endgameDrone: Boolean,
    val notes: String
) 