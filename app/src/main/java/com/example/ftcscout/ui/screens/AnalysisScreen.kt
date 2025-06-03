package com.example.ftcscout.ui.screens

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ftcscout.FTCScoutApplication
import com.example.ftcscout.ui.viewmodels.AnalysisViewModel
import com.example.ftcscout.data.entities.TeamStats

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnalysisScreen(
    onBackClick: () -> Unit,
    viewModel: AnalysisViewModel = viewModel(
        factory = AnalysisViewModel.Factory(
            (LocalContext.current.applicationContext as FTCScoutApplication).scoutDataRepository
        )
    )
) {
    val teamStats by viewModel.teamStats.collectAsState()
    val overallStats by viewModel.overallStats.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Análise de Dados") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            // Estatísticas Gerais
            item {
                Text("Estatísticas Gerais", style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(8.dp))
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        StatRow("Total de Partidas", overallStats.totalMatches.toString())
                        StatRow("Média de Pontos", String.format("%.1f", overallStats.averageScore))
                        StatRow("Maior Pontuação", overallStats.highestScore.toString())
                        StatRow("Menor Pontuação", overallStats.lowestScore.toString())
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
            }

            // Estatísticas por Fase
            item {
                Text("Estatísticas por Fase", style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(8.dp))
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text("Autonomous", style = MaterialTheme.typography.titleMedium)
                        StatRow("Média de Pixels", String.format("%.1f", overallStats.autoAveragePixels))
                        StatRow("Taxa de Estacionamento", String.format("%.1f%%", overallStats.autoParkingRate * 100))
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("TeleOp", style = MaterialTheme.typography.titleMedium)
                        StatRow("Média de Pixels Backdrop", String.format("%.1f", overallStats.teleopAveragePixelsBackdrop))
                        StatRow("Média de Pixels Mosaic", String.format("%.1f", overallStats.teleopAveragePixelsMosaic))
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Endgame", style = MaterialTheme.typography.titleMedium)
                        StatRow("Taxa de Drone", String.format("%.1f%%", overallStats.endgameDroneRate * 100))
                        StatRow("Taxa de Hanging", String.format("%.1f%%", overallStats.endgameHangingRate * 100))
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
            }

            // Ranking de Times
            item {
                Text("Ranking de Times", style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(8.dp))
            }

            items(teamStats) { team ->
                TeamCard(team)
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
fun StatRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label)
        Text(value, style = MaterialTheme.typography.bodyLarge)
    }
}

@Composable
fun TeamCard(team: TeamStats) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                "Time ${team.teamNumber}",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            StatRow("Pontuação Média", String.format("%.1f", team.averageScore))
            StatRow("Total de Partidas", team.totalMatches.toString())
            StatRow("Maior Pontuação", team.highestScore.toString())
            StatRow("Nível de Cooperação", String.format("%.1f", team.averageCooperation))
        }
    }
} 