package com.example.ftcscout.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "teams")
data class Team(
    @PrimaryKey val teamNumber: Int,
    val name: String?,
    val robotNotes: String?,
    val photoUri: String?
) 