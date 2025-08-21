package com.example.layceramictiles

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.layceramictiles.View.ScreenCalculateViewModel
import com.example.layceramictiles.View.ScreenMaterialsViewModel
import com.example.layceramictiles.components.CustomButton
import com.example.layceramictiles.components.SharedDataHolder
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

data class CalculationData(
    val id: String,
    val fileName: String
)

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Screen1(
    onContinueClick: () -> Unit,
    viewModelCalc: ScreenCalculateViewModel,
    viewModelMat: ScreenMaterialsViewModel,
    onOpenSaved: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val ctx = LocalContext.current
    val db = Firebase.firestore

    var files by remember { mutableStateOf<List<CalculationData>>(emptyList()) }
    var loading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        db.collection("Full Baza")
            .orderBy("timestamp", com.google.firebase.firestore.Query.Direction.DESCENDING)
            .limit(20)
            .get()
            .addOnSuccessListener { snap ->
                files = snap.documents.map {
                    CalculationData(
                        id = it.id,
                        fileName = it.id
                    )
                }
                loading = false
            }
            .addOnFailureListener { e ->
                error = e.message
                loading = false
            }
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

        if (loading) {
            Text("Loading saves...")
        } else if (error != null) {
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
                    text = "Error: $error",
                    color = MaterialTheme.colorScheme.onErrorContainer,
                    textAlign = TextAlign.Center
                )
            }
        } else if (files.isNotEmpty()) {
            Surface(
                tonalElevation = 4.dp,
                shape = RoundedCornerShape(24.dp), // Povećan radius zaobljenja
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 240.dp) // ~4 dugmeta pa scroll
                    .padding(horizontal = 8.dp)
            ) {
                SavedFilesList(
                    files = files,
                    onOpen = { file ->
                        loadProjectIntoViewModels(
                            fileName = file.fileName,
                            viewModelCalc = viewModelCalc,
                            viewModelMat = viewModelMat,
                            onDone = {
                                SharedDataHolder.currentFileName = file.fileName
                                onOpenSaved(file.fileName)
                            }
                        )
                    },
                    onDeletedLocally = { file ->
                        // Remove from local list after successful deletion
                        files = files.filter { it.id != file.id }
                    }
                )
            }
            Spacer(Modifier.height(16.dp))
        }

        CustomButton("CALCULATE", onClick = {
            SharedDataHolder.currentFileName = null // novi projekat
            onContinueClick()
        })
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SavedFilesList(
    files: List<CalculationData>,
    onOpen: (CalculationData) -> Unit,
    onDeletedLocally: (CalculationData) -> Unit,
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(files, key = { it.id }) { file ->
            val dismissState = rememberDismissState(
                confirmStateChange = { value ->
                    if (value == DismissValue.DismissedToStart) {
                        // Delete from Firestore and notify parent
                        deleteProjectFromFirestore(
                            collection = "Full Baza",
                            docId = file.id,
                            onDone = { success ->
                                if (success) onDeletedLocally(file)
                            }
                        )
                        true
                    } else {
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

private fun deleteProjectFromFirestore(
    collection: String,
    docId: String,
    onDone: (Boolean) -> Unit
) {
    val db = Firebase.firestore
    db.collection(collection)
        .document(docId)
        .delete()
        .addOnSuccessListener { onDone(true) }
        .addOnFailureListener { onDone(false) }
}

fun loadProjectIntoViewModels(
    fileName: String,
    viewModelCalc: ScreenCalculateViewModel,
    viewModelMat: ScreenMaterialsViewModel,
    onDone: () -> Unit
) {
    val db = Firebase.firestore
    db.collection("Full Baza").document(fileName).get()
        .addOnSuccessListener { doc ->
            // Calculate input
            viewModelCalc.widthA.value  = doc.getString("widthA") ?: ""
            viewModelCalc.lengthB.value = doc.getString("lengthB") ?: ""
            viewModelCalc.heightH.value = doc.getString("heightH") ?: ""

            // Calculate results
            val rCalc = doc.get("resultsCalculate") as? List<*> ?: emptyList<String>()
            viewModelCalc.results.value = rCalc.map { it?.toString() } // List<String?>

            // Materials input
            viewModelMat.tileWallWidth.value   = doc.getString("tileWallWidth") ?: ""
            viewModelMat.tileWallLength.value  = doc.getString("tileWallLength") ?: ""
            viewModelMat.tileFloorWidth.value  = doc.getString("tileFloorWidth") ?: ""
            viewModelMat.tileFloorLength.value = doc.getString("tileFloorLength") ?: ""
            viewModelMat.wallGroutWidth.value  = doc.getString("wallGroutWidth") ?: ""
            viewModelMat.floorGroutWidth.value = doc.getString("floorGroutWidth") ?: ""

            // Materials results
            viewModelMat.wallTileArea.value  = (doc.getDouble("wallTileArea") ?: 0.0).toFloat()
            viewModelMat.floorTileArea.value = (doc.getDouble("floorTileArea") ?: 0.0).toFloat()
            val adhesive = doc.getString("adhesive")
            val grout    = doc.getString("grout")
            viewModelMat.results.value = listOf(adhesive, grout)

            // Extra: upiši u SharedDataHolder za materijale (ako koristiš tamo)
            SharedDataHolder.floorTilesArea = viewModelMat.floorTileArea.value
            SharedDataHolder.wallTilesArea  = viewModelMat.wallTileArea.value

            onDone()
        }
}