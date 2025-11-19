package com.example.layceramictiles

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.layceramictiles.view.ProjectViewModel
import com.example.layceramictiles.components.CustomButton
import com.example.layceramictiles.components.NextPreviousSaveButtons
import com.example.layceramictiles.components.TileCard

@Composable
fun ScreenMaterials(
    viewModel: ProjectViewModel = viewModel(),
    onPreviousClick: () -> Unit = {},
    onSaveClick: (String) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()

    var showDialog by remember { mutableStateOf(false) }
    var fileName by remember { mutableStateOf(uiState.currentFileName ?: "") }

    Column(modifier = Modifier.fillMaxSize()) {
        Spacer(modifier = Modifier.height(40.dp))

        TileCard(
            title = "WALL TILE",
            imageRes = R.drawable.tile,
            valueWidth = uiState.tileWallWidth,
            valueLength = uiState.tileWallLength,
            valueGrout = uiState.wallGroutWidth,
            onWidthChange = { viewModel.onTileWallWidthChange(it)},
            onLengthChange = { viewModel.onTileWallLengthChange(it) },
            onGroutChange = { viewModel.onWallGroutWidthChange(it) }
            ,
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

        // Calculate button
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            CustomButton(
                text = "CALCULATE",
                onClick = {
                    viewModel.calculateMaterials()
                }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // ✅ Results display
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
                        ResultColumn(label = "Adhesive", value = uiState.resultsMaterials[0])
                        ResultColumn(label = "Grout", value = uiState.resultsMaterials[1])
                    }
                }
                Spacer(modifier = Modifier.height(14.dp))
            }
        }
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
                    Button(onClick = {
                        showDialog = false
                    }) {
                        Text("Cancel")
                    }
                }
            )
        }

        Row(modifier = Modifier.padding(16.dp)) {

            NextPreviousSaveButtons(
                onPreviousClick = onPreviousClick,
                onSaveClick = {
                    showDialog=true
                }
            )

        }
    }
}

fun calculateAdhesiveAndGrout(
    floorTileArea: Float,
    wallTileArea: Float,
    floorTileWidthCm: Float,
    floorTileLengthCm: Float,
    wallTileWidthCm: Float,
    wallTileLengthCm: Float,
    floorGroutWidthMm: Float,
    wallGroutWidthMm: Float
): List<String> {
    val adhesiveKg = (floorTileArea * 6.0 + wallTileArea * 5).toFloat()
    val wallTileGirth = 0.8f
    val floorTileGirth = 1f

    val floorGrout = floorTileArea * ((floorTileLengthCm + floorTileWidthCm) * floorGroutWidthMm/10 * floorTileGirth * 1.6f)/(floorTileLengthCm * floorTileWidthCm)
    val wallGrout = wallTileArea * ((wallTileLengthCm + wallTileWidthCm) * wallGroutWidthMm/10 * wallTileGirth * 1.6f)/(wallTileLengthCm * wallTileWidthCm)
    val totalGrout = floorGrout + wallGrout

    return listOf(
        "%.1f kg".format(adhesiveKg),
        "%.1f kg".format(totalGrout)
    )
}

