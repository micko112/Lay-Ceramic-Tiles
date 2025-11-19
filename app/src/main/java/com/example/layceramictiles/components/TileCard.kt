package com.example.layceramictiles.components

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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.layceramictiles.R

//@Preview
//@Composable
//private fun previewCard() {
//    var wallWidth by remember { mutableStateOf("") }
//    var wallHeight by remember { mutableStateOf("") }
//    TileCard(
//        title = "WALL TILE",
//        imageRes = R.drawable.tile,
//        valueWidth = "20",
//        valueLength = "20",
//        onWidthChange = { wallWidth = it },
//        onLengthChange = { wallWidth = it }
//    )
//}
@Composable
fun TileCard(
    title: String,
    imageRes: Int,
    valueWidth: String,
    valueLength: String,
    valueGrout: String,
    onWidthChange: (String) -> Unit,
    onLengthChange: (String) -> Unit,
    onGroutChange: (String) -> Unit,
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



            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top, // bitno!
                horizontalArrangement = Arrangement.Center
            ) {
                // Box iste visine kao slika
                Column {
                    Box(modifier = Modifier
                        .height(32.dp) // ista visina kao slika
                        .width(180.dp), // širina h inputa
                        contentAlignment = Alignment.CenterStart){
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                            text = "DIMENSIONS",
                            style = MaterialTheme.typography.titleLarge,
                            color = Color.Gray,
                            modifier = Modifier,
                        )
                            Image(painter=painterResource(R.drawable.dimensions),
                                contentDescription = "dimensions")
                        }

                    }

                    Box(
                        modifier = Modifier
                            .height(80.dp) // ista visina kao slika
                            .width(180.dp), // širina h inputa
                        contentAlignment = Alignment.BottomEnd
                    ) {
                        InputField(
                            value = valueLength,
                            prefix = "length =",
                            unit = "cm",
                            onValueChange = onLengthChange,
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))

                        )
                    }


                     Box(modifier = Modifier
                         .height(50.dp) // ista visina kao slika
                         .width(180.dp), // širina h inputa
                         contentAlignment = Alignment.BottomStart){
                         InputField(
                             value = valueGrout,
                             prefix = "Grout Width = ",
                             unit = "mm",
                             onValueChange = onGroutChange,
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
                        prefix = "width =",
                        unit = "cm",
                        onValueChange = onWidthChange,
                        modifier = Modifier
                            .width(100.dp)
                            .clip(RoundedCornerShape(10.dp))
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Image(
                        painter = painterResource(R.drawable.plocica),
                        contentDescription = "Tile Image",
                        modifier = Modifier
                            .size(130.dp)

                    )
                }
            }
        }
    }
}



//ovde stavi mesavinu nove i stare

@Composable
fun Card(
    title: String,
    imageRes: Int,
    valueWidth: String,
    valueLength: String,
    onWidthChange: (String) -> Unit,
    onLengthChange: (String) -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.fillMaxWidth()
            )

            Image(
                painter = painterResource(id = imageRes),
                contentDescription = "Tile image",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("Width (cm)", fontSize = 14.sp)
                    TextField(
                        value = valueWidth,
                        onValueChange = onWidthChange,
                        singleLine = true,
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                        colors = TextFieldDefaults.colors(
                            unfocusedContainerColor = Color.LightGray,
                            focusedContainerColor = Color.White
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                Column(modifier = Modifier.weight(1f)) {
                    Text("Height (cm)", fontSize = 14.sp)
                    TextField(
                        value = valueLength,
                        onValueChange = onLengthChange,
                        singleLine = true,
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                        colors = TextFieldDefaults.colors(
                            unfocusedContainerColor = Color.LightGray,
                            focusedContainerColor = Color.White
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}