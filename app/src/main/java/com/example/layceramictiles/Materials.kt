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
import com.example.layceramictiles.View.ScreenCalculateViewModel
import com.example.layceramictiles.View.ScreenMaterialsViewModel
import com.example.layceramictiles.components.CustomButton
import com.example.layceramictiles.components.NextPreviousSaveButtons
import com.example.layceramictiles.components.TileCard

@Composable
fun ScreenMaterials(

    viewModel: ScreenMaterialsViewModel = viewModel(),
    viewModelCalc: ScreenCalculateViewModel,
    onPreviousClick: () -> Unit = {},
    onSaveClick: (String) -> Unit = {}

) {
//    var TilewallWidth by remember { mutableStateOf("") }
//    var TilewallLength by remember { mutableStateOf("") }
//    var TilefloorWidth by remember { mutableStateOf("") }
//    var TilefloorLength by remember { mutableStateOf("") }
//    var WallGroutWidth by remember { mutableStateOf("") }
//    var FloorGroutWidth by remember { mutableStateOf("") }
    val tileWallWidth by viewModel.tileWallWidth.collectAsState()
    val tileWallLength by viewModel.tileWallLength.collectAsState()
    val tileFloorWidth by viewModel.tileFloorWidth.collectAsState()
    val tileFloorLength by viewModel.tileFloorLength.collectAsState()
    val wallGroutWidth by viewModel.wallGroutWidth.collectAsState()
    val floorGroutWidth by viewModel.floorGroutWidth.collectAsState()
    val results by viewModel.results.collectAsState()

    val calculateViewModel = viewModelCalc
    val materialsViewModel = viewModel

    var showDialog by remember { mutableStateOf(false) }
    var fileName by remember { mutableStateOf("") }
    // 🔢 Izračunaj materijale
    //var results by remember { mutableStateOf<List<String?>>(List(2) { null }) }


    Column(modifier = Modifier.fillMaxSize()) {
        Spacer(modifier = Modifier.height(40.dp))

        TileCard(
            title = "WALL TILE",
            imageRes = R.drawable.tile,
            valueWidth = tileWallWidth,
            valueHeight = tileWallLength,
            valueGrout = wallGroutWidth,
            onWidthChange = { viewModel.tileWallWidth.value = it },
            onHeightChange = { viewModel.tileWallLength.value = it },
            onGroutChange = { viewModel.wallGroutWidth.value = it }
            ,
        )
        Spacer(modifier = Modifier.height(8.dp))

        TileCard(
            title = "FLOOR TILE",
            imageRes = R.drawable.tile,
            valueWidth = tileFloorWidth,
            valueHeight = tileFloorLength,
            valueGrout = floorGroutWidth,
            onWidthChange = { viewModel.tileFloorWidth.value = it },
            onHeightChange = { viewModel.tileFloorLength.value = it },
            onGroutChange = { viewModel.floorGroutWidth.value = it }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Calculate button
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            CustomButton(
                text = "CALCULATE",
                onClick = {
                    // Pročitaj površine iz viewModela
                    val floorTileArea = calculateViewModel.results.value.getOrNull(4)
                        ?.replace("m²", "")?.trim()?.toFloatOrNull() ?: 0f
                    val wallTileArea = calculateViewModel.results.value.getOrNull(5)
                        ?.replace("m²", "")?.trim()?.toFloatOrNull() ?: 0f

                    materialsViewModel.floorTileArea.value = floorTileArea
                    materialsViewModel.wallTileArea.value = wallTileArea
                    // Ostali ulazi iz viewModela
                    val tileFloorWidth = materialsViewModel.tileFloorWidth.value.toFloatOrNull() ?: 0f
                    val tileFloorLength = materialsViewModel.tileFloorLength.value.toFloatOrNull() ?: 0f
                    val tileWallWidth = materialsViewModel.tileWallWidth.value.toFloatOrNull() ?: 0f
                    val tileWallLength = materialsViewModel.tileWallLength.value.toFloatOrNull() ?: 0f
                    val floorGroutWidth = materialsViewModel.floorGroutWidth.value.toFloatOrNull() ?: 0f
                    val wallGroutWidth = materialsViewModel.wallGroutWidth.value.toFloatOrNull() ?: 0f

                    // Računaj i upiši u results.value
                    val resultList = calculateAdhesiveAndGrout(
                        floorTileArea = floorTileArea,
                        wallTileArea = wallTileArea,
                        floorTileWidthCm = tileFloorWidth,
                        floorTileLengthCm = tileFloorLength,
                        wallTileWidthCm = tileWallWidth,
                        wallTileLengthCm = tileWallLength,
                        floorGroutWidthMm = floorGroutWidth,
                        wallGroutWidthMm = wallGroutWidth
                    )
                    materialsViewModel.results.value = resultList
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
                        ResultColumn(label = "Adhesive", value = results[0])
                        ResultColumn(label = "Grout", value = results[1])
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

