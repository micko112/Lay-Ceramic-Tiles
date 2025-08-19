package com.example.layceramictiles.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun NextPreviousSaveButtons(
    onNextClick: (() -> Unit)? = null,
    onPreviousClick: (() -> Unit)? = null,
    onSaveClick: (() -> Unit)? = null,
    isNextEnabled: Boolean = true
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        if (onPreviousClick != null) {
            Button(onClick = onPreviousClick) {
                Text("BACK")
            }
        } else {
            Spacer(modifier = Modifier.width(8.dp)) // da ostane raspored
        }

        if (onNextClick != null) {
            Button(onClick = onNextClick, enabled = isNextEnabled) {
                Text("NEXT")
            }
        }

        if (onSaveClick != null) {

            Button(onClick = onSaveClick) {
                Text("SAVE")
            }
        }
    }
}
