package com.example.layceramictiles.ui.screens

// SVI POTREBNI IMPORTI SU OVDE
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import com.example.layceramictiles.R
import com.example.layceramictiles.ui.components.AreaCard
import com.example.layceramictiles.ui.components.CustomButton
import com.example.layceramictiles.ui.components.NextPreviousSaveButtons
import com.example.layceramictiles.view.ProjectViewModel

@Composable
fun ScreenCalculate(
    viewModel: ProjectViewModel,
    onNextClick: () -> Unit,
    onPreviousClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val resultsReady = uiState.resultsCalculate.all { !it.isNullOrBlank() }

    // KORISTIMO SCAFFOLD KAO OSNOVU, KAO I NA DRUGOM EKRANU
    Scaffold { innerPadding ->
        // Glavni Column koji drži sve
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding) // <-- PRIMENJUJEMO PADDING IZ SCAFFOLD-A
        ) {
            // 1. Deo koji se skroluje i zauzima sav preostali prostor
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
            ) {
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
                        onClick = { viewModel.calculateArea() },
                        enabled = uiState.isAreaInputValid
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
                    Column(modifier = Modifier.padding(horizontal = 6.dp)) {
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
                                modifier = Modifier.fillMaxWidth().height(50.dp),
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
                                modifier = Modifier.fillMaxWidth().height(50.dp),
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
                Spacer(modifier = Modifier.height(16.dp))
            }

            // 2. Deo sa dugmadima koji je uvek na dnu
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                NextPreviousSaveButtons(
                    onPreviousClick = onPreviousClick,
                    onNextClick = onNextClick,
                    isNextEnabled = resultsReady
                )
            }
        }
    }
}

@Composable
fun RowScope.ResultColumn(label: String, value: String?) {
    Column(
        modifier = Modifier
            .weight(1f)
            .padding(horizontal = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge
        )
        Text(
            text = value ?: "—",
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
        )
    }
}