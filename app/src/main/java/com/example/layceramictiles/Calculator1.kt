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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.layceramictiles.components.InputField

@Composable
fun ScreenCalculate() {
    var values by remember { mutableStateOf(List(5) { "" }) }
    Column {
        InputCrossLayout(
            values = values,
            onValueChange = { index, newValue ->
                values = values.toMutableList().also { it[index] = newValue }
            }
        )

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
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Top field (a)
        InputField(
            value = values[0],
            prefix = "a=",
            unit = "m",
            onValueChange = { newValue -> onValueChange(0, newValue) },
            modifier = Modifier.padding(8.dp)
        )

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Left field (b)
            InputField(
                value = values[1],
                prefix = "b=",
                unit = "m",
                onValueChange = { newValue -> onValueChange(1, newValue) },
                modifier = Modifier.padding(8.dp)
            )

            // Middle - space for image
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .padding(4.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.wc),
                    contentDescription = "Ceramic logo",
                    modifier = Modifier.fillMaxSize()
                )
            }

            // Right field (c)
            InputField(
                value = values[2],
                prefix = "c=",
                unit = "m",
                onValueChange = { newValue -> onValueChange(2, newValue) },
                modifier = Modifier.padding(8.dp)
            )
        }

        // Bottom field (d)
        InputField(
            value = values[3],
            prefix = "d=",
            unit = "m",
            onValueChange = { newValue -> onValueChange(3, newValue) },
            modifier = Modifier.padding(8.dp)
        )
        Row(modifier= Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Start, verticalAlignment = Alignment.CenterVertically) {
            Spacer(Modifier.width(18.dp))
            Text("Height: ")
            InputField(
                value = values[4],
                prefix = "h=",
                unit = "m",
                onValueChange = { newValue -> onValueChange(4, newValue) },
                modifier = Modifier.padding(8.dp)
            )

        }
    }
}
