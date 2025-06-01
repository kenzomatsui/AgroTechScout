package com.example.ftcscout.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ftcscout.ui.viewmodels.MatchDetailsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MatchDetailsScreen(
    matchId: Int,
    onBackClick: () -> Unit,
    onScoutClick: (Int) -> Unit,
    viewModel: MatchDetailsViewModel = viewModel()
) {
    val match by viewModel.match.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    LaunchedEffect(matchId) {
        viewModel.loadMatch(matchId)
    }

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
                        // Match details content will go here
                        Text(
                            text = "Detalhes da partida #$matchId",
                            style = MaterialTheme.typography.headlineMedium
                        )
                    }
                }
            }
        }
    }
} 