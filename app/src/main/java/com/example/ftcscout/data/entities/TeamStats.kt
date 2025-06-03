package com.example.ftcscout.data.entities

data class TeamStats(
    val teamNumber: Int,
    val totalMatches: Int,
    val averageScore: Double,
    val highestScore: Int,
    val averageCooperation: Double,
    val autoAveragePixels: Double,
    val autoParkingRate: Double,
    val teleopAveragePixelsBackdrop: Double,
    val teleopAveragePixelsMosaic: Double,
    val endgameDroneRate: Double,
    val endgameHangingRate: Double
) 