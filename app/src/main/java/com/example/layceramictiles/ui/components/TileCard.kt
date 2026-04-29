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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.layceramictiles.R

private data class TileCardSizes(
    val imageSize: Dp,
    val labelBoxHeight: Dp,
    val lengthBoxHeight: Dp,
    val groutBoxHeight: Dp,
    val paddingH: Dp,
    val paddingV: Dp,
    val titlePaddingBottom: Dp,
    val inputBoxWidth: Dp,
    val widthInputWidth: Dp,
    val innerSpacer: Dp,
)

@Composable
private fun tileCardSizes(): TileCardSizes {
    val screenWidth = LocalConfiguration.current.screenWidthDp
    return when {
        screenWidth < 360 -> TileCardSizes(   // Small phones
            imageSize = 78.dp,
            labelBoxHeight = 24.dp,
            lengthBoxHeight = 52.dp,
            groutBoxHeight = 34.dp,
            paddingH = 10.dp,
            paddingV = 6.dp,
            titlePaddingBottom = 3.dp,
            inputBoxWidth = 155.dp,
            widthInputWidth = 88.dp,
            innerSpacer = 10.dp,
        )
        screenWidth < 390 -> TileCardSizes(   // Medium phones
            imageSize = 90.dp,
            labelBoxHeight = 28.dp,
            lengthBoxHeight = 60.dp,
            groutBoxHeight = 40.dp,
            paddingH = 12.dp,
            paddingV = 8.dp,
            titlePaddingBottom = 4.dp,
            inputBoxWidth = 180.dp,
            widthInputWidth = 100.dp,
            innerSpacer = 12.dp,
        )
        screenWidth < 450 -> TileCardSizes(   // Large phones (Redmi Note 10 Pro ~437dp)
            imageSize = 108.dp,
            labelBoxHeight = 34.dp,
            lengthBoxHeight = 72.dp,
            groutBoxHeight = 48.dp,
            paddingH = 14.dp,
            paddingV = 10.dp,
            titlePaddingBottom = 5.dp,
            inputBoxWidth = 210.dp,
            widthInputWidth = 115.dp,
            innerSpacer = 14.dp,
        )
        else -> TileCardSizes(                // XLarge phones / tablets
            imageSize = 130.dp,
            labelBoxHeight = 40.dp,
            lengthBoxHeight = 86.dp,
            groutBoxHeight = 58.dp,
            paddingH = 16.dp,
            paddingV = 12.dp,
            titlePaddingBottom = 6.dp,
            inputBoxWidth = 240.dp,
            widthInputWidth = 130.dp,
            innerSpacer = 16.dp,
        )
    }
}

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
    val s = tileCardSizes()

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
            .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(16.dp))
    ) {
        Column(
            modifier = Modifier.padding(horizontal = s.paddingH, vertical = s.paddingV)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(bottom = s.titlePaddingBottom)
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.Center
            ) {
                Column {
                    Box(
                        modifier = Modifier
                            .height(s.labelBoxHeight)
                            .width(s.inputBoxWidth),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Text(
                            text = "ENTER DIMENSIONS",
                            fontSize = 16.sp,
                            style = MaterialTheme.typography.displayMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }

                    Box(
                        modifier = Modifier
                            .height(s.lengthBoxHeight)
                            .width(s.inputBoxWidth),
                        contentAlignment = Alignment.BottomEnd
                    ) {
                        InputField(
                            value = valueLength,
                            prefix = "length =",
                            unit = "cm",
                            onValueChange = onLengthChange,
                            modifier = Modifier.clip(RoundedCornerShape(10.dp))
                        )
                    }

                    Box(
                        modifier = Modifier
                            .height(s.groutBoxHeight)
                            .width(s.inputBoxWidth),
                        contentAlignment = Alignment.BottomStart
                    ) {
                        InputField(
                            value = valueGrout,
                            prefix = "Grout Width = ",
                            unit = "mm",
                            onValueChange = onGroutChange,
                            modifier = Modifier.clip(RoundedCornerShape(10.dp))
                        )
                    }
                }

                Spacer(modifier = Modifier.width(s.innerSpacer))

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    InputField(
                        value = valueWidth,
                        prefix = "width =",
                        unit = "cm",
                        onValueChange = onWidthChange,
                        modifier = Modifier
                            .width(s.widthInputWidth)
                            .clip(RoundedCornerShape(10.dp))
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Image(
                        painter = painterResource(R.drawable.plocica),
                        contentDescription = "Tile Image",
                        modifier = Modifier.size(s.imageSize)
                    )
                }
            }
        }
    }
}
