package com.example.ftcscout.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.padding
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.ftcscout.ui.screens.*

sealed class MainScreen(val route: String, val icon: @Composable () -> Unit, val label: String) {
    object Events : MainScreen("events", { Icon(Icons.Default.Event, contentDescription = "Eventos") }, "Eventos")
    object Analysis : MainScreen("analysis", { Icon(Icons.Default.Analytics, contentDescription = "Análise") }, "Análise")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainNavigation(navController: NavHostController) {
    val screens = listOf(
        MainScreen.Events,
        MainScreen.Analysis
    )

    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination

                screens.forEach { screen ->
                    NavigationBarItem(
                        icon = screen.icon,
                        label = { Text(screen.label) },
                        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = MainScreen.Events.route,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(MainScreen.Events.route) {
                EventsScreen(
                    onEventClick = { eventId ->
                        navController.navigate(Screen.EventDetails.createRoute(eventId))
                    }
                )
            }

            composable(MainScreen.Analysis.route) {
                AnalysisScreen(
                    onBackClick = { navController.popBackStack() }
                )
            }

            // Rotas detalhadas
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
                    onBackClick = { navController.popBackStack() }
                )
            }

            composable(Screen.MatchDetails.route) { backStackEntry ->
                val matchId = backStackEntry.arguments?.getString("matchId")?.toIntOrNull() ?: return@composable
                MatchDetailsScreen(
                    matchId = matchId,
                    onBackClick = { navController.popBackStack() },
                    onScoutClick = { teamNumber ->
                        navController.navigate(Screen.Scout.createRoute(matchId, teamNumber))
                    }
                )
            }

            composable(Screen.Scout.route) { backStackEntry ->
                val matchId = backStackEntry.arguments?.getString("matchId")?.toIntOrNull() ?: return@composable
                val teamNumber = backStackEntry.arguments?.getString("teamNumber")?.toIntOrNull() ?: return@composable
                ScoutScreen(
                    matchId = matchId,
                    teamNumber = teamNumber,
                    onBackClick = { navController.popBackStack() }
                )
            }
        }
    }
} 