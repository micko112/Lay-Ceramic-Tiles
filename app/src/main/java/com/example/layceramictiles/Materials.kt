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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.layceramictiles.components.CustomButton
import com.example.layceramictiles.components.NextPreviousSaveButtons
import com.example.layceramictiles.components.TileCard

@Composable
fun ScreenMaterials(
    wallTileArea: Float,
    floorTileArea: Float,
    onPreviousClick: () -> Unit = {},
    onNextClick: () -> Unit = {},
    onSaveClick: () -> Unit = {}
) {
    var TilewallWidth by remember { mutableStateOf("") }
    var TilewallLength by remember { mutableStateOf("") }
    var TilefloorWidth by remember { mutableStateOf("") }
    var TilefloorLength by remember { mutableStateOf("") }
    var WallGroutWidth by remember { mutableStateOf("") }
    var FloorGroutWidth by remember { mutableStateOf("") }

    // 🔢 Izračunaj materijale
    var results by remember { mutableStateOf<List<String?>>(List(2) { null }) }

    Column(modifier = Modifier.fillMaxSize()) {
        Spacer(modifier = Modifier.height(40.dp))

        TileCard(
            title = "WALL TILE",
            imageRes = R.drawable.tile,
            valueWidth = TilewallWidth,
            valueHeight = TilewallLength,
            valueGrout = WallGroutWidth,
            onWidthChange = { TilewallWidth = it },
            onHeightChange = { TilewallLength = it },
            onGroutChange = { WallGroutWidth = it },
        )
        Spacer(modifier = Modifier.height(8.dp))

        TileCard(
            title = "FLOOR TILE",
            imageRes = R.drawable.tile,
            valueWidth = TilefloorWidth,
            valueHeight = TilefloorLength,
            valueGrout = FloorGroutWidth,
            onWidthChange = { TilefloorWidth = it },
            onHeightChange = { TilefloorLength = it },
            onGroutChange = { FloorGroutWidth = it}
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Calculate button
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            CustomButton(
                text = "CALCULATE",
                onClick = {
                     results = calculateAdhesiveAndGrout(
                        floorTileArea = floorTileArea,
                        wallTileArea = wallTileArea,
                        floorTileWidthCm = TilefloorWidth.toFloatOrNull() ?: 0f,
                        floorTileHeightCm = TilefloorLength.toFloatOrNull() ?: 0f,
                        wallTileWidthCm = TilewallWidth.toFloatOrNull() ?: 0f,
                        wallTileHeightCm = TilewallLength.toFloatOrNull() ?: 0f,
                        floorGroutWidthMm = FloorGroutWidth.toFloatOrNull() ?: 0f,
                        wallGroutWidthMm = WallGroutWidth.toFloatOrNull() ?: 0f,
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
    floorTileHeightCm: Float,
    wallTileWidthCm: Float,
    wallTileHeightCm: Float,
    floorGroutWidthMm: Float,
    wallGroutWidthMm: Float
): List<String> {
    val floorTilesCount = (floorTileArea * 10000) / (floorTileWidthCm * floorTileHeightCm)
    val wallTilesCount = (wallTileArea * 10000) / (wallTileWidthCm * wallTileHeightCm)

    val adhesiveKg = (floorTileArea * 4.0 + wallTileArea * 3.5).toFloat()

    val floorGrout = (floorGroutWidthMm / 1000) * floorTilesCount * 0.05f
    val wallGrout = (wallGroutWidthMm / 1000) * wallTilesCount * 0.05f
    val totalGrout = floorGrout + wallGrout

    return listOf(
        "%.1f kg".format(adhesiveKg),
        "%.1f kg".format(totalGrout)
    )
}