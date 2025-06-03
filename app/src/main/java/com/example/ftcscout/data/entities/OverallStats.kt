package com.example.ftcscout.data.entities

data class OverallStats(
    val totalMatches: Int = 0,
    val averageScore: Double = 0.0,
    val highestScore: Int = 0,
    val lowestScore: Int = 0,
    val autoAveragePixels: Double = 0.0,
    val autoParkingRate: Double = 0.0,
    val teleopAveragePixelsBackdrop: Double = 0.0,
    val teleopAveragePixelsMosaic: Double = 0.0,
    val endgameDroneRate: Double = 0.0,
    val endgameHangingRate: Double = 0.0
) 