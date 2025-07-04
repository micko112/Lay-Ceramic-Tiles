package com.example.layceramictiles

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.layceramictiles.components.CustomButton
import com.example.layceramictiles.components.InputField
import com.example.layceramictiles.components.NextPreviousSaveButtons
import com.example.layceramictiles.components.SharedDataHolder

@Composable
fun ScreenCalculate(onNextClick: () -> Unit = {}) {
    var values by remember { mutableStateOf(List(3) { "" }) }
    var results by remember { mutableStateOf<List<String?>>(List(6) { null }) }
    val resultsReady = results.all { !it.isNullOrBlank() }
    Column(modifier = Modifier.fillMaxSize()) {
        Spacer(modifier = Modifier.height(80.dp))
        InputCrossLayout(
            values = values,
            onValueChange = { index, newValue ->
                values = values.toMutableList().also { it[index] = newValue }
            }
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Calculate button
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            CustomButton(
                text = "CALCULATE",
                onClick = {
                    results = calculateResults(values)
                    // ✨ Ovde setuješ globalne vrednosti
                    val floorTilesArea = results[4]?.replace("m²", "")?.trim()?.toFloatOrNull() ?: 0f
                    val wallTilesArea = results[5]?.replace("m²", "")?.trim()?.toFloatOrNull() ?: 0f

                    SharedDataHolder.floorTilesArea = floorTilesArea
                    SharedDataHolder.wallTilesArea = wallTilesArea
                }
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

         //Results display
        Text("Results:", style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center )
        Surface(modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            color=MaterialTheme.colorScheme.secondary,
            tonalElevation = 8.dp,
            shadowElevation = 4.dp)
        {
            Column(modifier = Modifier.padding(horizontal = 6.dp))
            {
                Spacer(modifier = Modifier.height(8.dp))
                Text("AREA:", style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center )
                Surface(modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                    shape = RoundedCornerShape(6.dp),
                    color=MaterialTheme.colorScheme.tertiary,
                    tonalElevation = 8.dp,
                    shadowElevation = 4.dp) {
                    Row(
                        modifier = Modifier.fillMaxWidth().height(50.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        ResultColumn(label = "Total", value = results[0])
                        ResultColumn(label = "Floor", value = results[1])
                        ResultColumn(label = "Wall", value = results[2])
                    }
                }
                Text("TILES NEEDED:", style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center )
                Surface(modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom =16.dp),
                    shape = RoundedCornerShape(6.dp),
                    color=MaterialTheme.colorScheme.tertiary,
                    //tonalElevation = 8.dp,
                    shadowElevation = 4.dp) {
                    Row(
                        modifier = Modifier.fillMaxWidth().height(50.dp),
                        horizontalArrangement = Arrangement.Absolute.SpaceEvenly
                    ) {
                        ResultColumn(label = "Total", value = results[3])
                        ResultColumn(label = "Floor", value = results[4])
                        ResultColumn(label = "Wall", value = results[5])
                    }

                }


                Spacer(modifier = Modifier.height(14.dp))
//                ResultRow(label = "Total (m²):", value = results[0])
//                ResultRow(label = "Floor:", value = results[1])
//                ResultRow(label = "Wall:", value = results[2])

            }
        }
        Row(modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End){
            NextPreviousSaveButtons(onNextClick=onNextClick,
                //isNextEnabled = resultsReady
            )

        }
    }
}

@Preview
@Composable
private fun ScreenCalc() {
    ScreenCalculate()
}
@Composable
fun InputCrossLayout(
    modifier: Modifier = Modifier,
    values: List<String>,
    onValueChange: (Int, String) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Top field (a) - centered
        Row() {
            Spacer(modifier = Modifier.width(120.dp))
            InputField(
                value = values[0],
                prefix = "a=",
                unit = "m",
                onValueChange = { onValueChange(0, it) },
                modifier = Modifier
                    .padding(8.dp)
                    .width(100.dp)
            ) }


        Spacer(modifier = Modifier.height(8.dp))

        // Middle row with b= and image
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            InputField(
                value = values[1],
                prefix = "b=",
                unit = "m",
                onValueChange = { onValueChange(1, it) },
                modifier = Modifier
                    .padding(8.dp)
                    .width(100.dp)
            )

            Image(
                painter = painterResource(id = R.drawable.wc),
                contentDescription = "Ceramic logo",
                modifier = Modifier
                    .size(180.dp)
                    .padding(8.dp)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Bottom row with h= and "Height:"
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 32.dp)
        ) {
            Text(
                "Height:",
                style = MaterialTheme.typography.bodyLarge
            )

            InputField(
                value = values[2],
                prefix = "h=",
                unit = "m",
                onValueChange = { onValueChange(2, it) },
                modifier = Modifier
                    .padding(start = 8.dp)
                    .width(100.dp)
            )
        }
    }

}


@Composable
fun ResultColumn(label: String, value: String?) {
    Column (modifier = Modifier.padding(horizontal = 8.dp)) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.width(120.dp)
        )
        Text(
            text = value ?: "",
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
        )
    }
}

fun calculateResults(values: List<String>): List<String> {
    return try {
        // Pretvaranje unosa u brojeve
        val a = values[0].toFloatOrNull() ?: 0f
        val b = values[1].toFloatOrNull() ?: 0f
//        val c = values[2].toFloatOrNull() ?: 0f
//        val d = values[3].toFloatOrNull() ?: 0f
        val h = values[2].toFloatOrNull() ?: 0f
        //val tileSize = values[4].toFloatOrNull() ?: 0f

        // Izračunavanje rezultata
        val floorArea = a * b
        val wallArea = 2 * (a + b) * h
        val floorTiles = floorArea*1.1
        val wallTiles = wallArea *1.08
        val totalTiles = floorTiles+wallTiles


        // Formatiranje rezultata (2 decimalna mesta)
        listOf(
            "%.1fm²".format(floorArea + wallArea),
            "%.1fm²".format(floorArea),
            "%.1fm²".format(wallArea),
            "%.1fm²".format(totalTiles),
            "%.1fm²".format(floorTiles),
           "%.1fm²".format(wallTiles)
        )
    } catch (e: Exception) {
        // U slučaju greške vraćamo prazne rezultate
        List(6) { "Error" }
    }
}
