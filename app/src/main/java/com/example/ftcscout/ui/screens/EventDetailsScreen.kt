package com.example.ftcscout.ui.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

// Outras importações que já estavam no seu código:
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
    onBackClick: () -> Unit,
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
    var showDeleteConfirmDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
                    }
                },
                title = { Text(event?.name ?: "Detalhes do Evento") },
                actions = {
                    IconButton(onClick = { showAddMatchDialog = true }) {
                        Icon(Icons.Default.Add, contentDescription = "Adicionar Partida")
                    }
                    IconButton(onClick = { showDeleteConfirmDialog = true }) {
                        Icon(Icons.Default.Delete, contentDescription = "Excluir Evento")
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
                    items(matches, key = { it.matchId }) { match ->
                        val dismissState = rememberSwipeToDismissBoxState(
                            confirmValueChange = {
                                if (it == SwipeToDismissBoxValue.EndToStart) {
                                    viewModel.deleteMatch(match)
                                    true
                                } else {
                                    false
                                }
                            }
                        )

                        SwipeToDismissBox(
                            state = dismissState,
                            backgroundContent = {
                                val color by animateColorAsState(
                                    when (dismissState.targetValue) {
                                        SwipeToDismissBoxValue.Settled -> Color.LightGray
                                        SwipeToDismissBoxValue.EndToStart -> Color.Red
                                        SwipeToDismissBoxValue.StartToEnd -> Color.LightGray
                                    },
                                    label = ""
                                )
                                val scale by animateFloatAsState(
                                    if (dismissState.targetValue == SwipeToDismissBoxValue.Settled) 0.75f else 1f,
                                    label = ""
                                )

                                Box(
                                    Modifier
                                        .fillMaxSize()
                                        .background(color)
                                        .padding(horizontal = 20.dp),
                                    contentAlignment = Alignment.CenterEnd
                                ) {
                                    Icon(
                                        Icons.Default.Delete,
                                        contentDescription = "Excluir",
                                        modifier = Modifier.scale(scale),
                                        tint = Color.White
                                    )
                                }
                            },
                            content = {
                                MatchCard(
                                    match = match,
                                    onTeamClick = onTeamClick,
                                    onScoutClick = onScoutClick
                                )
                            }
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

    if (showDeleteConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirmDialog = false },
            title = { Text("Confirmar Exclusão") },
            text = { Text("Tem certeza que deseja excluir este evento e todas as partidas associadas?") },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.viewModelScope.launch {
                        viewModel.deleteEvent()
                        showDeleteConfirmDialog = false
                        onBackClick()
                    }
                }) {
                    Text("Excluir")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirmDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MatchCard(
    match: Match,
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

    val isInputValid = number.isNotBlank() && number.toIntOrNull() != null &&
                       teamRed1.isNotBlank() && teamRed1.toIntOrNull() != null &&
                       teamRed2.isNotBlank() && teamRed2.toIntOrNull() != null &&
                       teamBlue1.isNotBlank() && teamBlue1.toIntOrNull() != null &&
                       teamBlue2.isNotBlank() && teamBlue2.toIntOrNull() != null

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
                    onConfirm(
                        number.toInt(),
                        teamRed1.toInt(),
                        teamRed2.toInt(),
                        teamBlue1.toInt(),
                        teamBlue2.toInt()
                    )
                },
                enabled = isInputValid
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