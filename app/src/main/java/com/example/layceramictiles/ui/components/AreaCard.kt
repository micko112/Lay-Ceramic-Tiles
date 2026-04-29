package com.example.layceramictiles.ui.components

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AreaCard(
    title: String,
    valueSideA: String,
    valueSideB: String,
    valueSideC: String,
    valueSideD: String,
    valueHeight: String,
    useCustomWalls: Boolean,
    customWallCount: Int,
    onSideAChange: (String) -> Unit,
    onSideBChange: (String) -> Unit,
    onSideCChange: (String) -> Unit,
    onSideDChange: (String) -> Unit,
    onHeightChange: (String) -> Unit,
    onCustomWallsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showInfoDialog by remember { mutableStateOf(false) }

    if (showInfoDialog) {
        CustomWallsInfoDialog(onDismiss = { showInfoDialog = false })
    }

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
            // Title
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(bottom = 4.dp)
            )

            // Subtitle
            Text(
                text = "ENTER DIMENSIONS",
                fontSize = 16.sp,
                style = MaterialTheme.typography.displayMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Diagram with a, b, c, d inputs around a rectangle
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // b input (top side)
                InputField(
                    value = valueSideB,
                    prefix = "b = ",
                    unit = " m",
                    onValueChange = onSideBChange,
                    modifier = Modifier.clip(RoundedCornerShape(10.dp))
                )

                Spacer(modifier = Modifier.height(6.dp))

                // Middle row: a input | rectangle | c input
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    // Side a (left)
                    InputField(
                        value = valueSideA,
                        prefix = "a = ",
                        unit = " m",
                        onValueChange = onSideAChange,
                        modifier = Modifier.clip(RoundedCornerShape(10.dp))
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    // Rectangle representing the room
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(60.dp)
                            .border(
                                2.dp,
                                MaterialTheme.colorScheme.outline,
                                RoundedCornerShape(4.dp)
                            )
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    // Side c (right)
                    InputField(
                        value = valueSideC,
                        prefix = "c = ",
                        unit = " m",
                        onValueChange = onSideCChange,
                        modifier = Modifier.clip(RoundedCornerShape(10.dp))
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                // d input (bottom side)
                InputField(
                    value = valueSideD,
                    prefix = "d = ",
                    unit = " m",
                    onValueChange = onSideDChange,
                    modifier = Modifier.clip(RoundedCornerShape(10.dp))
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Height + Custom Walls row
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Height input
                InputField(
                    value = valueHeight,
                    prefix = "h = ",
                    unit = " m",
                    onValueChange = onHeightChange,
                    modifier = Modifier.clip(RoundedCornerShape(10.dp))
                )

                Spacer(modifier = Modifier.weight(1f))

                // Info button
                IconButton(
                    onClick = { showInfoDialog = true },
                    modifier = Modifier.size(36.dp),
                    colors = IconButtonDefaults.iconButtonColors(
                        contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                ) {
                    Icon(
                        Icons.Outlined.Info,
                        contentDescription = "How custom walls work",
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.width(4.dp))

                // Custom Walls button
                if (useCustomWalls) {
                    Button(
                        onClick = onCustomWallsClick,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        ),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(
                            text = "WALLS ($customWallCount)",
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                } else {
                    OutlinedButton(
                        onClick = onCustomWallsClick,
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(
                            text = "CUSTOM WALLS",
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CustomWallsInfoDialog(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("GOT IT")
            }
        },
        title = {
            Text(
                text = "How does it work?",
                style = MaterialTheme.typography.titleLarge
            )
        },
        text = {
            Column {
                Text(
                    text = buildAnnotatedString {
                        withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                            append("Simple mode")
                        }
                        append(" (default)")
                    },
                    style = MaterialTheme.typography.titleSmall
                )
                Text(
                    text = "Wall area is calculated automatically from the room sides and height:\n" +
                            "(a + b + c + d) \u00D7 h",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 12.dp)
                )
                Text(
                    text = buildAnnotatedString {
                        withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                            append("Custom walls")
                        }
                    },
                    style = MaterialTheme.typography.titleSmall
                )
                Text(
                    text = "Define each wall segment individually with its own width and height. " +
                            "Useful when walls are not all the same height, or the room shape is irregular.",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 12.dp)
                )
                Text(
                    text = "Example",
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                )
                Text(
                    text = "A 5\u00D73m bathroom with a niche:\n" +
                            "\u2022 Wall 1: 5m \u00D7 2.5m (full wall)\n" +
                            "\u2022 Wall 2: 1.5m \u00D7 2.5m (side of niche)\n" +
                            "\u2022 Wall 3: 2m \u00D7 2.5m (back of niche)\n" +
                            "\u2022 Wall 4: 1.5m \u00D7 2.5m (side of niche)\n" +
                            "\u2022 Wall 5: 3m \u00D7 2.5m (full wall)\n" +
                            "\u2022 Wall 6: 3m \u00D7 2m (above bathtub, shorter)",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    text = "Note: a, b, c, d are always used for floor area calculation, regardless of wall mode.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    )
}
