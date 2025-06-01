package com.example.ftcscout.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ftcscout.FTCScoutApplication
import com.example.ftcscout.data.entities.Match
import com.example.ftcscout.ui.components.EmptyStateScreen
import com.example.ftcscout.ui.viewmodels.EventDetailsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventDetailsScreen(
    eventId: Int,
    onTeamClick: (Int) -> Unit,
    onMatchClick: (Int) -> Unit,
    onScoutClick: (Int, Int) -> Unit,
    viewModel: EventDetailsViewModel = viewModel(
        factory = EventDetailsViewModel.Factory(
            (LocalContext.current.applicationContext as FTCScoutApplication).eventRepository,
            (LocalContext.current.applicationContext as FTCScoutApplication).matchRepository,
            eventId
        )
    )
) {
    val event by viewModel.event.collectAsState()
    val matches by viewModel.matches.collectAsState()
    var showAddMatchDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(event?.name ?: "Detalhes do Evento") },
                actions = {
                    IconButton(onClick = { showAddMatchDialog = true }) {
                        Icon(Icons.Default.Add, contentDescription = "Adicionar Partida")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (matches.isEmpty()) {
                EmptyStateScreen(message = "Nenhuma partida cadastrada")
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(matches) { match ->
                        MatchCard(
                            match = match,
                            onMatchClick = { onMatchClick(match.matchId) },
                            onTeamClick = onTeamClick,
                            onScoutClick = onScoutClick
                        )
                    }
                }
            }
        }
    }

    if (showAddMatchDialog) {
        AddMatchDialog(
            onDismiss = { showAddMatchDialog = false },
            onConfirm = { number, teamRed1, teamRed2, teamBlue1, teamBlue2 ->
                viewModel.addMatch(
                    Match(
                        eventId = eventId,
                        number = number,
                        teamRed1 = teamRed1,
                        teamRed2 = teamRed2,
                        teamBlue1 = teamBlue1,
                        teamBlue2 = teamBlue2
                    )
                )
                showAddMatchDialog = false
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MatchCard(
    match: Match,
    onMatchClick: () -> Unit,
    onTeamClick: (Int) -> Unit,
    onScoutClick: (Int, Int) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Partida ${match.number}",
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.height(8.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("Aliança Vermelha", style = MaterialTheme.typography.titleMedium)
                    TextButton(onClick = { onTeamClick(match.teamRed1) }) {
                        Text("Time ${match.teamRed1}")
                    }
                    TextButton(onClick = { onTeamClick(match.teamRed2) }) {
                        Text("Time ${match.teamRed2}")
                    }
                }
                Column {
                    Text("Aliança Azul", style = MaterialTheme.typography.titleMedium)
                    TextButton(onClick = { onTeamClick(match.teamBlue1) }) {
                        Text("Time ${match.teamBlue1}")
                    }
                    TextButton(onClick = { onTeamClick(match.teamBlue2) }) {
                        Text("Time ${match.teamBlue2}")
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(onClick = { onScoutClick(match.matchId, match.teamRed1) }) {
                    Text("Scout Time ${match.teamRed1}")
                }
                Button(onClick = { onScoutClick(match.matchId, match.teamRed2) }) {
                    Text("Scout Time ${match.teamRed2}")
                }
                Button(onClick = { onScoutClick(match.matchId, match.teamBlue1) }) {
                    Text("Scout Time ${match.teamBlue1}")
                }
                Button(onClick = { onScoutClick(match.matchId, match.teamBlue2) }) {
                    Text("Scout Time ${match.teamBlue2}")
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddMatchDialog(
    onDismiss: () -> Unit,
    onConfirm: (number: Int, teamRed1: Int, teamRed2: Int, teamBlue1: Int, teamBlue2: Int) -> Unit
) {
    var number by remember { mutableStateOf("") }
    var teamRed1 by remember { mutableStateOf("") }
    var teamRed2 by remember { mutableStateOf("") }
    var teamBlue1 by remember { mutableStateOf("") }
    var teamBlue2 by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Nova Partida") },
        text = {
            Column {
                TextField(
                    value = number,
                    onValueChange = { number = it },
                    label = { Text("Número da Partida") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = teamRed1,
                    onValueChange = { teamRed1 = it },
                    label = { Text("Time Vermelho 1") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = teamRed2,
                    onValueChange = { teamRed2 = it },
                    label = { Text("Time Vermelho 2") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = teamBlue1,
                    onValueChange = { teamBlue1 = it },
                    label = { Text("Time Azul 1") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = teamBlue2,
                    onValueChange = { teamBlue2 = it },
                    label = { Text("Time Azul 2") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (number.isNotBlank() && teamRed1.isNotBlank() && teamRed2.isNotBlank() &&
                        teamBlue1.isNotBlank() && teamBlue2.isNotBlank()
                    ) {
                        onConfirm(
                            number.toIntOrNull() ?: 0,
                            teamRed1.toIntOrNull() ?: 0,
                            teamRed2.toIntOrNull() ?: 0,
                            teamBlue1.toIntOrNull() ?: 0,
                            teamBlue2.toIntOrNull() ?: 0
                        )
                    }
                }
            ) {
                Text("Adicionar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
} 