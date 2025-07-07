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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.layceramictiles.View.ScreenMaterialsViewModel
import com.example.layceramictiles.components.CustomButton
import com.example.layceramictiles.components.NextPreviousSaveButtons
import com.example.layceramictiles.components.TileCard

@Composable
fun ScreenMaterials(
    wallTileArea: Float,
    floorTileArea: Float,
    viewModel: ScreenMaterialsViewModel = viewModel(),
    onPreviousClick: () -> Unit = {},
    onNextClick: () -> Unit = {},
    onSaveClick: () -> Unit = {}
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
                     viewModel.results.value = calculateAdhesiveAndGrout(
                        floorTileArea = floorTileArea,
                        wallTileArea = wallTileArea,
                        floorTileWidthCm = tileFloorWidth.toFloatOrNull() ?: 0f,
                        floorTileLengthCm = tileFloorLength.toFloatOrNull() ?: 0f,
                        wallTileWidthCm = tileWallWidth.toFloatOrNull() ?: 0f,
                        wallTileLengthCm = tileWallLength.toFloatOrNull() ?: 0f,
                        floorGroutWidthMm = floorGroutWidth.toFloatOrNull() ?: 0f,
                        wallGroutWidthMm = wallGroutWidth.toFloatOrNull() ?: 0f,
                    )
                    // ništa konkretno sada jer si već izračunao iznad
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

        Row(modifier = Modifier.padding(16.dp)) {
            NextPreviousSaveButtons(
                onPreviousClick = onPreviousClick,
                onSaveClick = onSaveClick
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