package com.example.layceramictiles

// SVI POTREBNI IMPORTI SU OVDE
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.layceramictiles.components.AreaCard
import com.example.layceramictiles.components.CustomButton
import com.example.layceramictiles.components.NextPreviousSaveButtons
import com.example.layceramictiles.view.ProjectViewModel

@Composable
fun ScreenCalculate(
    viewModel: ProjectViewModel, // ViewModel se prosleđuje
    onNextClick: () -> Unit
) {
    // Svi podaci dolaze iz uiState
    val uiState by viewModel.uiState.collectAsState()
    val resultsReady = uiState.resultsCalculate.all { !it.isNullOrBlank() }

    Column(modifier = Modifier.fillMaxSize()) {
        Spacer(modifier = Modifier.height(80.dp))

        AreaCard(
            title = "AREA",
            imageRes = R.drawable.wc,
            valueWidth = uiState.widthA,
            valueLength = uiState.lengthB,
            valueHeight = uiState.heightH,
            onWidthChange = { viewModel.onWidthAChange(it) },
            onLengthChange = { viewModel.onLengthBChange(it) },
            onHeightChange = { viewModel.onHeightHChange(it) },
            modifier = Modifier
        )

        Spacer(modifier = Modifier.height(32.dp))

        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            CustomButton(
                text = "CALCULATE",
                onClick = { viewModel.calculateArea() } // Akcija ide na viewModel
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.secondary,
            tonalElevation = 8.dp,
            shadowElevation = 4.dp
        ) {
            Column(modifier = Modifier.padding(horizontal = 6.dp))
            {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "AREA:", style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center
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
                        ResultColumn(label = "Total", value = uiState.resultsCalculate.getOrNull(0))
                        ResultColumn(label = "Floor", value = uiState.resultsCalculate.getOrNull(1))
                        ResultColumn(label = "Wall", value = uiState.resultsCalculate.getOrNull(2))
                    }
                }
                Text(
                    "TILES NEEDED:", style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center
                )
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    shape = RoundedCornerShape(6.dp),
                    color = MaterialTheme.colorScheme.tertiary,
                    shadowElevation = 4.dp
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        horizontalArrangement = Arrangement.Absolute.SpaceEvenly
                    ) {
                        ResultColumn(label = "Total", value = uiState.resultsCalculate.getOrNull(3))
                        ResultColumn(label = "Floor", value = uiState.resultsCalculate.getOrNull(4))
                        ResultColumn(label = "Wall", value = uiState.resultsCalculate.getOrNull(5))
                    }
                }
                Spacer(modifier = Modifier.height(14.dp))
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            NextPreviousSaveButtons(
                onNextClick = onNextClick,
                isNextEnabled = resultsReady
            )
        }
    }
}

@Composable
fun ResultColumn(label: String, value: String?) {
    Column(modifier = Modifier.padding(horizontal = 8.dp)) {
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