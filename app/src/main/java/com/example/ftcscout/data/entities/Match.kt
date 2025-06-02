package com.example.ftcscout.data.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "matches",
    foreignKeys = [
        ForeignKey(
            entity = Event::class,
            parentColumns = ["eventId"],
            childColumns = ["eventId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Match(
    @PrimaryKey(autoGenerate = true) val matchId: Int = 0,
    val eventId: Int,
    val number: Int,
    val teamRed1: Int,
    val teamRed2: Int,
    val teamBlue1: Int,
    val teamBlue2: Int
) 