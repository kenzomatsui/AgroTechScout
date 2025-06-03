package com.example.ftcscout.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "scout_data")
data class ScoutData(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val matchId: Int,
    val teamNumber: Int,
    
    // Autonomous
    val autoPixelsBackdrop: Int = 0, // Ex: Pixels no Backdrop (3 pts cada)
    val autoParked: Boolean = false, // Ex: Estacionado (5 pts)
    
    // TeleOp
    val teleopPixelsBackdrop: Int = 0, // Ex: Pixels no Backdrop (2 pts cada)
    val teleopPixelsMosaic: Int = 0, // Ex: Pixels em Mosaic (1 pt cada) - Exemplo fictício
    val teleopMajorPenalties: Int = 0, // Ex: Penalidades maiores (reduz pontuação adversária, mas registramos aqui)
    val teleopMinorPenalties: Int = 0, // Ex: Penalidades menores
    
    // Endgame
    val endgameDroneLaunched: Boolean = false, // Ex: Drone Lançado (10 pts)
    val endgameHanging: Boolean = false, // Ex: Hanging (20 pts)
    
    // Pontuação Final (calculada e editável)
    val calculatedScore: Int = 0, // Pontuação calculada automaticamente
    val finalScore: Int = 0, // Pontuação final editável
    
    // Outros
    val notes: String = "",
    val cooperationLevel: Int = 0
) 