package com.example.ftcscout.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ftcscout.ui.viewmodels.ScoutMatchViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScoutMatchScreen(
    matchId: Int,
    teamNumber: Int,
    onBackClick: () -> Unit,
    viewModel: ScoutMatchViewModel = viewModel()
) {
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    LaunchedEffect(matchId, teamNumber) {
        viewModel.loadMatchData(matchId, teamNumber)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Scout - Time #$teamNumber") },
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
                else -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                    ) {
                        // Scout form content will go here
                        Text(
                            text = "Formulário de Scout - Partida #$matchId - Time #$teamNumber",
                            style = MaterialTheme.typography.headlineMedium
                        )
                    }
                }
            }
        }
    }
} 