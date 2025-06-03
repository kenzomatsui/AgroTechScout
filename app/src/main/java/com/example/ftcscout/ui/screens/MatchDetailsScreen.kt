package com.example.ftcscout.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ftcscout.FTCScoutApplication
import com.example.ftcscout.data.entities.ScoutData
import com.example.ftcscout.ui.viewmodels.MatchDetailsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MatchDetailsScreen(
    matchId: Int,
    onBackClick: () -> Unit,
    onScoutClick: (Int) -> Unit,
    viewModel: MatchDetailsViewModel = viewModel(
        factory = MatchDetailsViewModel.Factory(
            (LocalContext.current.applicationContext as FTCScoutApplication).matchRepository,
            (LocalContext.current.applicationContext as FTCScoutApplication).scoutDataRepository,
            matchId
        )
    )
) {
    val match by viewModel.match.collectAsState()
    val scoutDataList by viewModel.scoutDataList.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Partida #$matchId") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Voltar")
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(androidx.compose.ui.Alignment.Center)
                    )
                }
                error != null -> {
                    Text(
                        text = error ?: "Erro desconhecido",
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier
                            .align(androidx.compose.ui.Alignment.Center)
                            .padding(16.dp)
                    )
                }
                match != null -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "Detalhes da partida #$matchId",
                            style = MaterialTheme.typography.headlineMedium
                        )
                        Spacer(modifier = Modifier.height(16.dp))

                        Text("Dados de Scouting", style = MaterialTheme.typography.titleLarge)
                        Spacer(modifier = Modifier.height(8.dp))

                        if (scoutDataList.isEmpty()) {
                            Text("Nenhum dado de scouting disponível para esta partida.")
                        } else {
                            LazyColumn {
                                items(scoutDataList) { scoutData ->
                                    ScoutDataItem(scoutData = scoutData) { id ->
                                        viewModel.deleteScoutDataEntry(id)
                                    }
                                    Divider()
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ScoutDataItem(scoutData: ScoutData, onDeleteClick: (Int) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text("Time: ${scoutData.teamNumber}")
            Text("Pontuação Final: ${scoutData.finalScore}")
        }
        IconButton(onClick = { onDeleteClick(scoutData.id) }) {
            Icon(Icons.Default.Delete, contentDescription = "Excluir dado de scouting")
        }
    }
} 