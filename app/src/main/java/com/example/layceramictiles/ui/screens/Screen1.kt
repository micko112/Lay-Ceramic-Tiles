package com.example.layceramictiles.ui.screens

import android.os.Parcelable
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.DismissDirection
import androidx.compose.material.DismissValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.SwipeToDismiss
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.rememberDismissState
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.layceramictiles.R
import com.example.layceramictiles.ui.components.CustomButton
import com.example.layceramictiles.view.Screen1ViewModel
import kotlinx.parcelize.Parcelize

@Parcelize
data class CalculationData(
    val id: String,
    val fileName: String
) : Parcelable

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Screen1(
    onContinueClick: () -> Unit,
    viewModel: Screen1ViewModel,
    onOpenSaved: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadSavedFiles()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Naslov - svaka reč u posebnom redu
        Text(
            text = "LAY",
            fontSize = 60.sp,
            fontWeight = FontWeight.ExtraBold
        )
        Text(
            text = "CERAMIC",
            fontSize = 60.sp,
            fontWeight = FontWeight.ExtraBold
        )
        Text(
            text = "TILES",
            fontSize = 60.sp,
            fontWeight = FontWeight.ExtraBold
        )

        // Logo
        Image(
            painter = painterResource(id = R.drawable.micko_logo),
            contentDescription = "Ceramic logo",
            modifier = Modifier
                .height(80.dp)
                .fillMaxWidth()
        )
        Spacer(modifier=Modifier.height(20.dp))

        if (uiState.loading) {
            Text("Loading saves...")
        } else if (uiState.error != null) {
            // Zaobljena pozadina za error poruku
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.errorContainer)
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Error: $uiState.error",
                    color = MaterialTheme.colorScheme.onErrorContainer,
                    textAlign = TextAlign.Center
                )
            }
        } else if (uiState.files.isNotEmpty()) {
            Surface(
                tonalElevation = 4.dp,
                shape = RoundedCornerShape(24.dp), // Povećan radius zaobljenja
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 240.dp) // ~4 dugmeta pa scroll
                    .padding(horizontal = 8.dp)
            ) {
                SavedFilesList(
                    files = uiState.files,
                    onOpen = { file ->
                        // Samo obaveštavamo navigaciju koji fajl treba otvoriti
                        onOpenSaved(file.fileName)
                    },
                    onDelete = { file ->
                        // Pozivamo funkciju za brisanje iz ViewModela
                        viewModel.deleteProject(file)
                    }
                )
            }
            Spacer(Modifier.height(16.dp))
        }

        CustomButton("CALCULATE", onClick = {
            onContinueClick()
        })
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SavedFilesList(
    files: List<CalculationData>,
    onOpen: (CalculationData) -> Unit,
    onDelete: (CalculationData) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(files, key = { it.id }) { file ->
            val dismissState = rememberDismissState(
                confirmStateChange = {
                    if(it == DismissValue.DismissedToStart){
                        onDelete(file)
                        true
                    }else {
                        false
                    }
                }
            )

            SwipeToDismiss(
                state = dismissState,
                directions = setOf(DismissDirection.EndToStart), // swipe left to delete
                background = {
                    // Crvena pozadina sa zaobljenim ivicama i bijelom ikonicom
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(vertical = 4.dp)
                            .clip(RoundedCornerShape(16.dp)) // Zaobljene ivice
                            .background(MaterialTheme.colorScheme.error),
                        contentAlignment = Alignment.CenterEnd
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(end = 16.dp)
                        ) {
                            Icon(
                                Icons.Default.Delete,
                                contentDescription = "Delete",
                                tint = Color.White // Bijela ikonica
                            )
                            Spacer(Modifier.width(8.dp))
                            Text(
                                "Delete",
                                color = Color.White // Bijeli tekst
                            )
                        }
                    }
                },
                dismissContent = {
                    // Card button that opens the file when clicked
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        elevation = 4.dp,
                        shape = RoundedCornerShape(16.dp) // Zaobljene ivice za item
                    ) {
                        TextButton(
                            onClick = { onOpen(file) },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = file.fileName,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 12.dp),
                                textAlign = TextAlign.Center // Centriran tekst
                            )
                        }
                    }
                }
            )
        }
    }
}

