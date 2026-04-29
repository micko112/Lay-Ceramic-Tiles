package com.example.layceramictiles.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.layceramictiles.ui.components.AreaCard
import com.example.layceramictiles.ui.components.CustomButton
import com.example.layceramictiles.ui.components.CustomWallsDialog
import com.example.layceramictiles.ui.components.NextPreviousSaveButtons
import com.example.layceramictiles.ui.components.SettingsButton
import com.example.layceramictiles.ui.theme.ThemeMode
import com.example.layceramictiles.view.ProjectViewModel

@Composable
fun ScreenCalculate(
    viewModel: ProjectViewModel,
    onNextClick: () -> Unit,
    onPreviousClick: () -> Unit,
    currentThemeMode: ThemeMode,
    onThemeModeChange: (ThemeMode) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val resultsReady = uiState.resultsCalculate.all { !it.isNullOrBlank() }
    var showCustomWallsDialog by remember { mutableStateOf(false) }

    // Custom Walls Dialog
    if (showCustomWallsDialog) {
        CustomWallsDialog(
            walls = uiState.customWalls,
            onAddWall = { viewModel.addCustomWall() },
            onRemoveWall = { viewModel.removeCustomWall(it) },
            onWallWidthChange = { i, v -> viewModel.onCustomWallWidthChange(i, v) },
            onWallHeightChange = { i, v -> viewModel.onCustomWallHeightChange(i, v) },
            onClearAll = { viewModel.clearCustomWalls() },
            onDismiss = { showCustomWallsDialog = false }
        )
    }

    Scaffold { innerPadding ->
        Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            // Scrollable content
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
            ) {
                Spacer(modifier = Modifier.height(80.dp))

                AreaCard(
                    title = "AREA",
                    valueSideA = uiState.sideA,
                    valueSideB = uiState.sideB,
                    valueSideC = uiState.sideC,
                    valueSideD = uiState.sideD,
                    valueHeight = uiState.heightH,
                    useCustomWalls = uiState.useCustomWalls,
                    customWallCount = uiState.customWalls.size,
                    onSideAChange = { viewModel.onSideAChange(it) },
                    onSideBChange = { viewModel.onSideBChange(it) },
                    onSideCChange = { viewModel.onSideCChange(it) },
                    onSideDChange = { viewModel.onSideDChange(it) },
                    onHeightChange = { viewModel.onHeightHChange(it) },
                    onCustomWallsClick = { showCustomWallsDialog = true },
                    modifier = Modifier
                )

                Spacer(modifier = Modifier.height(16.dp))

                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    CustomButton(
                        text = "CALCULATE",
                        onClick = { viewModel.calculateArea() },
                        enabled = uiState.isAreaInputValid
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    shape = RoundedCornerShape(16.dp),
                    color = MaterialTheme.colorScheme.secondary,
                    tonalElevation = 8.dp,
                    shadowElevation = 4.dp
                ) {
                    Column(modifier = Modifier.padding(horizontal = 12.dp, vertical = 12.dp)) {
                        Text(
                            "AREA:", style = MaterialTheme.typography.headlineSmall,
                            modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp),
                            color = MaterialTheme.colorScheme.tertiary
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 10.dp),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                ResultColumn(label = "Total", value = uiState.resultsCalculate.getOrNull(0))
                                ResultColumn(label = "Floor", value = uiState.resultsCalculate.getOrNull(1))
                                ResultColumn(label = "Wall", value = uiState.resultsCalculate.getOrNull(2))
                            }
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            "TILES NEEDED:", style = MaterialTheme.typography.headlineSmall,
                            modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp),
                            color = MaterialTheme.colorScheme.tertiary
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 10.dp),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                ResultColumn(label = "Total", value = uiState.resultsCalculate.getOrNull(3))
                                ResultColumn(label = "Floor", value = uiState.resultsCalculate.getOrNull(4))
                                ResultColumn(label = "Wall", value = uiState.resultsCalculate.getOrNull(5))
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Bottom buttons
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .padding(bottom = innerPadding.calculateBottomPadding())
            ) {
                NextPreviousSaveButtons(
                    onPreviousClick = onPreviousClick,
                    onNextClick = onNextClick,
                    isNextEnabled = resultsReady
                )
            }
        }
            SettingsButton(
                currentThemeMode = currentThemeMode,
                onThemeModeChange = onThemeModeChange,
                modifier = Modifier.align(Alignment.TopEnd)
            )
        }
    }
}

@Composable
fun RowScope.ResultColumn(label: String, value: String?) {
    Column(
        modifier = Modifier
            .weight(1f)
            .padding(horizontal = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge
        )
        Text(
            text = value ?: "\u2014",
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
        )
    }
}
