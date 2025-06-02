package com.example.ftcscout.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ftcscout.data.AppDatabase
import com.example.ftcscout.data.repository.MatchRepository
import com.example.ftcscout.data.repository.TeamRepository
import com.example.ftcscout.ui.screens.*
import com.example.ftcscout.ui.viewmodels.ScoutViewModel
import com.example.ftcscout.data.repository.ScoutDataRepository

sealed class Screen(val route: String) {
    object Events : Screen("events")
    object EventDetails : Screen("event/{eventId}") {
        fun createRoute(eventId: Int) = "event/$eventId"
    }
    object TeamDetails : Screen("team/{teamNumber}") {
        fun createRoute(teamNumber: Int) = "team/$teamNumber"
    }
    object MatchDetails : Screen("match/{matchId}") {
        fun createRoute(matchId: Int) = "match/$matchId"
    }
    object Scout : Screen("scout/{matchId}/{teamNumber}") {
        fun createRoute(matchId: Int, teamNumber: Int) = "scout/$matchId/$teamNumber"
    }
    object Analysis : Screen("analysis")
}

@Composable
fun NavGraph(
    navController: NavHostController,
    startDestination: String = Screen.Events.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Screen.Events.route) {
            EventsScreen(
                onEventClick = { eventId ->
                    navController.navigate(Screen.EventDetails.createRoute(eventId))
                }
            )
        }

        composable(Screen.EventDetails.route) { backStackEntry ->
            val eventId = backStackEntry.arguments?.getString("eventId")?.toIntOrNull() ?: return@composable
            EventDetailsScreen(
                eventId = eventId,
                onTeamClick = { teamNumber ->
                    navController.navigate(Screen.TeamDetails.createRoute(teamNumber))
                },
                onMatchClick = { matchId ->
                    navController.navigate(Screen.MatchDetails.createRoute(matchId))
                },
                onScoutClick = { matchId, teamNumber ->
                    navController.navigate(Screen.Scout.createRoute(matchId, teamNumber))
                },
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(Screen.TeamDetails.route) { backStackEntry ->
            val teamNumber = backStackEntry.arguments?.getString("teamNumber")?.toIntOrNull() ?: return@composable
            TeamDetailsScreen(
                teamNumber = teamNumber,
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        composable(Screen.MatchDetails.route) { backStackEntry ->
            val matchId = backStackEntry.arguments?.getString("matchId")?.toIntOrNull() ?: return@composable
            MatchDetailsScreen(
                matchId = matchId,
                onBackClick = {
                    navController.popBackStack()
                },
                onScoutClick = { teamNumber ->
                    navController.navigate(Screen.Scout.createRoute(matchId, teamNumber))
                }
            )
        }

        composable(Screen.Scout.route) { backStackEntry ->
            val matchId = backStackEntry.arguments?.getString("matchId")?.toIntOrNull() ?: return@composable
            val teamNumber = backStackEntry.arguments?.getString("teamNumber")?.toIntOrNull() ?: return@composable

            val context = LocalContext.current
            val database = AppDatabase.getDatabase(context)
            val scoutDataRepository = ScoutDataRepository(database.scoutDataDao())
            val viewModel: ScoutViewModel = viewModel(
                factory = ScoutViewModel.Factory(scoutDataRepository, matchId, teamNumber)
            )

            ScoutScreen(
                matchId = matchId,
                teamNumber = teamNumber,
                onBackClick = {
                    navController.popBackStack()
                },
                viewModel = viewModel
            )
        }

        composable(Screen.Analysis.route) {
            AnalysisScreen()
        }
    }
} 