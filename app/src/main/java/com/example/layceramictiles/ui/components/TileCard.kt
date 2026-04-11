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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.layceramictiles.R

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
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
            .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(16.dp))
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.Center
            ) {
                Column {
                    Box(
                        modifier = Modifier
                            .height(32.dp)
                            .width(180.dp),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "DIMENSIONS",
                                fontSize = 18.sp,
                                style = MaterialTheme.typography.displayMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier,
                            )
                            Image(
                                painter = painterResource(R.drawable.dimensions),
                                contentDescription = "dimensions"
                            )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .height(80.dp)
                            .width(180.dp),
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

                    Box(
                        modifier = Modifier
                            .height(50.dp)
                            .width(180.dp),
                        contentAlignment = Alignment.BottomStart
                    ) {
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
