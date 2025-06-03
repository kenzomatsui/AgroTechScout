package com.example.ftcscout.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.NavHost
import com.example.ftcscout.ui.screens.ScoutScreen
import com.example.ftcscout.ui.screens.AnalysisScreen
import com.example.ftcscout.ui.screens.HomeScreen

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeScreen(navController = navController)
        }

        composable("scout/{matchId}/{teamNumber}") { backStackEntry ->
            val matchId = backStackEntry.arguments?.getString("matchId")?.toIntOrNull() ?: 0
            val teamNumber = backStackEntry.arguments?.getString("teamNumber")?.toIntOrNull() ?: 0
            ScoutScreen(
                matchId = matchId,
                teamNumber = teamNumber,
                onBackClick = { navController.popBackStack() }
            )
        }

        composable("analysis") {
            AnalysisScreen(
                onBackClick = { navController.popBackStack() }
            )
        }
    }
} 