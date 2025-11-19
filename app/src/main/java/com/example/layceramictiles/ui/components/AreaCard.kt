package com.example.layceramictiles.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.layceramictiles.R

@Composable
fun AreaCard(
    title: String,
    imageRes: Int,
    valueWidth: String,
    valueLength: String,
    valueHeight: String,
    onWidthChange: (String) -> Unit,
    onHeightChange: (String) -> Unit,
    onLengthChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
            .border(1.dp, Color.LightGray, RoundedCornerShape(16.dp))
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold),
                color = Color.Black,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Box(
                modifier = Modifier
                    .height(48.dp) // ista visina kao slika
                    .fillMaxWidth()
                    .padding( horizontal = 16.dp)
                    .padding(bottom = 10.dp), // širina h inputa
                contentAlignment = Alignment.CenterStart
            ) {
                Row(verticalAlignment = Alignment.CenterVertically,) {
                    Text(
                        text = "ENTER DIMENSIONS",
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.Gray,
                        modifier = Modifier,
                    )
                    Image(
                        painter = painterResource(R.drawable.dimensions),
                        contentDescription = "dimensions"
                    )
                }

            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top, // bitno!
                horizontalArrangement = Arrangement.Center
            ) {
                // Box iste visine kao slika
                Column {


                    Box(
                        modifier = Modifier
                            .height(80.dp) // ista visina kao slika
                            .width(180.dp), // širina h inputa
                        contentAlignment = Alignment.BottomEnd
                    ) {
                        InputField(
                            value = valueLength,
                            prefix = "length =",
                            unit = "m",
                            onValueChange = onLengthChange,
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))

                        )
                    }


                    Box(
                        modifier = Modifier
                            .height(50.dp) // ista visina kao slika
                            .width(180.dp), // širina h inputa
                        contentAlignment = Alignment.BottomStart
                    ) {
                        InputField(
                            value = valueHeight,
                            prefix = "Height =",
                            unit = "m",
                            onValueChange = onHeightChange,
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))

                        )
                    }

                }




                Spacer(modifier = Modifier.width(16.dp))

                // w= iznad slike
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    InputField(
                        value = valueWidth,
                        prefix = "Width =",
                        unit = "m",
                        onValueChange = onWidthChange,
                        modifier = Modifier
                            .width(100.dp)
                            .clip(RoundedCornerShape(10.dp))
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Image(
                        painter = painterResource(R.drawable.wc),
                        contentDescription = "WC Image",
                        modifier = Modifier
                            .size(130.dp)

                    )
                }
            }
        }
    }
}