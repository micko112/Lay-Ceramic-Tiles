package com.example.layceramictiles.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.layceramictiles.R
import com.example.layceramictiles.ui.components.CustomButton
import com.example.layceramictiles.ui.components.NextPreviousSaveButtons
import com.example.layceramictiles.ui.components.TileCard
import com.example.layceramictiles.view.ProjectViewModel
import kotlinx.coroutines.launch

@Composable
fun ScreenMaterials(
    viewModel: ProjectViewModel,
    onPreviousClick: () -> Unit,
    onSaveClick: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    var showDialog by remember { mutableStateOf(false) }
    var fileName by remember { mutableStateOf(uiState.currentFileName ?: "") }

    LaunchedEffect(uiState.showSaveSuccessMessage) {
        if (uiState.showSaveSuccessMessage) {
            scope.launch {
                snackbarHostState.showSnackbar("Project saved successfully!")
            }
            viewModel.onSaveMessageShown()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { innerPadding ->

        // Glavni Column koji drži sve
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {

            // 1. Deo koji se skroluje i zauzima sav preostali prostor
            Column(
                modifier = Modifier
                    .weight(1f) // <-- KLJUČNA PROMENA
                    .verticalScroll(rememberScrollState()) // <-- DODAJEMO SKROL
            ) {
                Spacer(modifier = Modifier.height(10.dp))

                TileCard(
                    title = "WALL TILE",
                    imageRes = R.drawable.tile,
                    valueWidth = uiState.tileWallWidth,
                    valueLength = uiState.tileWallLength,
                    valueGrout = uiState.wallGroutWidth,
                    onWidthChange = { viewModel.onTileWallWidthChange(it) },
                    onLengthChange = { viewModel.onTileWallLengthChange(it) },
                    onGroutChange = { viewModel.onWallGroutWidthChange(it) }
                )
                Spacer(modifier = Modifier.height(8.dp))

                TileCard(
                    title = "FLOOR TILE",
                    imageRes = R.drawable.tile,
                    valueWidth = uiState.tileFloorWidth,
                    valueLength = uiState.tileFloorLength,
                    valueGrout = uiState.floorGroutWidth,
                    onWidthChange = { viewModel.onTileFloorWidthChange(it) },
                    onLengthChange = { viewModel.onTileFloorLengthChange(it) },
                    onGroutChange = { viewModel.onFloorGroutWidthChange(it) }
                )

                Spacer(modifier = Modifier.height(16.dp))

                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    CustomButton(
                        text = "CALCULATE",
                        onClick = { viewModel.calculateMaterials() },
                        enabled = uiState.isMaterialsInputValid
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
                    Column(modifier = Modifier.padding(horizontal = 6.dp)) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            "MATERIALS NEEDED",
                            style = MaterialTheme.typography.headlineSmall,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 16.dp),
                            shape = RoundedCornerShape(6.dp),
                            color = MaterialTheme.colorScheme.tertiary,
                            tonalElevation = 8.dp,
                            shadowElevation = 4.dp
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(50.dp),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                ResultColumn(label = "Adhesive", value = uiState.resultsMaterials.getOrNull(0))
                                ResultColumn(label = "Grout", value = uiState.resultsMaterials.getOrNull(1))
                            }
                        }
                        Spacer(modifier = Modifier.height(14.dp))
                    }
                }
            }

            // 2. Deo sa dugmadima koji je uvek na dnu
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                NextPreviousSaveButtons(
                    onPreviousClick = onPreviousClick,
                    onSaveClick = {
                        if (uiState.currentFileName != null) {
                            onSaveClick(uiState.currentFileName!!)
                        } else {
                            showDialog = true
                        }
                    }
                )
            }
        }

        // Dialog za čuvanje je ostao van glavnog Column-a da bi se prikazao preko svega
        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text("Unesite ime fajla") },
                text = {
                    TextField(
                        value = fileName,
                        onValueChange = { fileName = it },
                        placeholder = { Text("name") }
                    )
                },
                confirmButton = {
                    Button(onClick = {
                        if (fileName.isNotBlank()) {
                            showDialog = false
                            onSaveClick(fileName)
                        }
                    }) {
                        Text("Save")
                    }
                },
                dismissButton = {
                    Button(onClick = { showDialog = false }) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}



