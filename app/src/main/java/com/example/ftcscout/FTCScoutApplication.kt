package com.example.ftcscout

import android.app.Application
import com.example.ftcscout.data.AppDatabase
import com.example.ftcscout.data.repository.*

class FTCScoutApplication : Application() {
    val database by lazy { AppDatabase.getDatabase(this) }
    
    val teamRepository by lazy { TeamRepository(database.teamDao()) }
    val eventRepository by lazy { EventRepository(database.eventDao()) }
    val matchRepository by lazy { MatchRepository(database.matchDao()) }
    val scoutDataRepository by lazy { ScoutDataRepository(database.scoutDataDao()) }
} 