package com.example.ftcscout.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ftcscout.FTCScoutApplication
import com.example.ftcscout.ui.viewmodels.ScoutViewModel
import androidx.compose.ui.platform.LocalFocusManager
import com.example.ftcscout.data.entities.Match

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScoutScreen(
    matchId: Int,
    teamNumber: Int,
    onBackClick: () -> Unit,
    viewModel: ScoutViewModel = viewModel(
        factory = ScoutViewModel.Factory(
            (LocalContext.current.applicationContext as FTCScoutApplication).scoutDataRepository,
            (LocalContext.current.applicationContext as FTCScoutApplication).matchRepository,
            matchId,
            teamNumber
        )
    )
) {
    val scoutData by viewModel.scoutData.collectAsState()
    val matchData by viewModel.matchData.collectAsState()
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Scout: Partida $matchId, Time $teamNumber") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            // Autonomous
            Text("Autonomous", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(8.dp))

            // Pixels no Backdrop
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Pixels no Backdrop (3 pts cada)")
                NumberPicker(
                    value = scoutData?.autoPixelsBackdrop ?: 0,
                    onValueChange = { newValue: Int -> viewModel.updateAutoPixelsBackdrop(newValue) }
                )
            }
            Spacer(modifier = Modifier.height(8.dp))

            // Estacionado
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Estacionado (5 pts)")
                Switch(
                    checked = scoutData?.autoParked ?: false,
                    onCheckedChange = { isChecked: Boolean -> viewModel.updateAutoParked(isChecked) }
                )
            }
            Spacer(modifier = Modifier.height(24.dp))

            // TeleOp
            Text("TeleOp", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(8.dp))

             // Pixels no Backdrop
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Pixels no Backdrop (2 pts cada)")
                NumberPicker(
                    value = scoutData?.teleopPixelsBackdrop ?: 0,
                    onValueChange = { newValue: Int -> viewModel.updateTeleopPixelsBackdrop(newValue) }
                )
            }
            Spacer(modifier = Modifier.height(8.dp))

            // Pixels em Mosaic
             Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Pixels em Mosaic (1 pt cada)")
                NumberPicker(
                    value = scoutData?.teleopPixelsMosaic ?: 0,
                    onValueChange = { newValue: Int -> viewModel.updateTeleopPixelsMosaic(newValue) }
                )
            }
            Spacer(modifier = Modifier.height(8.dp))

            // Penalidades (apenas registro)
            Text("Penalidades (registro)", style = MaterialTheme.typography.titleMedium)
             Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Penalidades Maiores")
                NumberPicker(
                    value = scoutData?.teleopMajorPenalties ?: 0,
                    onValueChange = { newValue: Int -> viewModel.updateTeleopMajorPenalties(newValue) }
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
             Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Penalidades Menores")
                NumberPicker(
                    value = scoutData?.teleopMinorPenalties ?: 0,
                    onValueChange = { newValue: Int -> viewModel.updateTeleopMinorPenalties(newValue) }
                )
            }
            Spacer(modifier = Modifier.height(24.dp))

            // Endgame
            Text("Endgame", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(8.dp))

            // Drone Lançado
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Drone Lançado (10 pts)")
                Switch(
                    checked = scoutData?.endgameDroneLaunched ?: false,
                    onCheckedChange = { isChecked: Boolean -> viewModel.updateEndgameDroneLaunched(isChecked) }
                )
            }
            Spacer(modifier = Modifier.height(8.dp))

            // Hanging
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Hanging (20 pts)")
                Switch(
                    checked = scoutData?.endgameHanging ?: false,
                    onCheckedChange = { isChecked: Boolean -> viewModel.updateEndgameHanging(isChecked) }
                )
            }
            Spacer(modifier = Modifier.height(24.dp))

            // Pontuação
            Text("Pontuação", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(8.dp))

            // Pontuação Calculada
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Pontuação Calculada:", style = MaterialTheme.typography.titleMedium)
                Text(scoutData?.calculatedScore.toString(), style = MaterialTheme.typography.titleMedium)
            }
            Spacer(modifier = Modifier.height(8.dp))

            // Pontuação Final (Editável)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextField(
                    value = scoutData?.finalScore?.toString() ?: "0",
                    onValueChange = { newValue ->
                        viewModel.updateFinalScore(newValue.toIntOrNull() ?: 0)
                    },
                    label = { Text("Pontuação Final (Editável)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = {
                        scoutData?.calculatedScore?.let { calculated ->
                            viewModel.updateFinalScore(calculated)
                        }
                        focusManager.clearFocus()
                    }
                ) {
                    Text("Usar Calculada")
                }
            }
            Spacer(modifier = Modifier.height(24.dp))

            // Outros
            Text("Outros", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = scoutData?.notes ?: "",
                onValueChange = { newNotes ->
                    viewModel.updateNotes(newNotes)
                },
                label = { Text("Notas sobre o desempenho, problemas, cooperação, etc.") },
                modifier = Modifier.fillMaxWidth().height(150.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Botão Salvar Notas (mantido para fechar teclado)
            Button(
                onClick = {
                    keyboardController?.hide()
                    focusManager.clearFocus()
                },
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("Salvar Notas")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Nível de Cooperação
            Text("Nível de Cooperação", style = MaterialTheme.typography.titleMedium)
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                val cooperationLevel = scoutData?.cooperationLevel ?: 0
                for (i in 1..5) {
                    Icon(
                        imageVector = if (i <= cooperationLevel) Icons.Filled.Star else Icons.Filled.StarBorder,
                        contentDescription = "Star $i",
                        tint = if (i <= cooperationLevel) Color.Yellow else Color.Gray,
                        modifier = Modifier
                            .weight(1f)
                            .clickable { viewModel.updateCooperationLevel(i) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Pontuação das Alianças (para cálculo de RP)
            Text("Pontuação das Alianças (para cálculo de RP)", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(8.dp))

            AllianceScoreInput(
                label = "Pontuação da Aliança Vermelha",
                score = matchData?.redScore ?: 0,
                onScoreChange = { newValue: Int -> viewModel.updateRedScore(newValue) }
            )
            Spacer(modifier = Modifier.height(8.dp))

            AllianceScoreInput(
                label = "Pontuação da Aliança Azul",
                score = matchData?.blueScore ?: 0,
                onScoreChange = { newValue: Int -> viewModel.updateBlueScore(newValue) }
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun NumberPicker(
    value: Int,
    onValueChange: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        IconButton(
            onClick = { if (value > 0) onValueChange(value - 1) },
            enabled = value > 0
        ) {
            Text("-", style = MaterialTheme.typography.titleLarge)
        }
        Text(
            text = value.toString(),
            style = MaterialTheme.typography.titleMedium
        )
        IconButton(
            onClick = { onValueChange(value + 1) }
        ) {
            Text("+", style = MaterialTheme.typography.titleLarge)
        }
    }
}

@Composable
fun AllianceScoreInput(
    label: String,
    score: Int,
    onScoreChange: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    TextField(
        value = score.toString(),
        onValueChange = { newValue: String ->
            onScoreChange(newValue.toIntOrNull() ?: 0)
        },
        label = { Text(label) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        modifier = modifier.fillMaxWidth()
    )
}