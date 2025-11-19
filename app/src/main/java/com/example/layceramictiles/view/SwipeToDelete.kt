@file:OptIn(ExperimentalMaterialApi::class)
package com.example.layceramictiles.view


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.DismissDirection
import androidx.compose.material.DismissValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.SwipeToDismiss
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.rememberDismissState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.layceramictiles.CalculationData
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore



@Composable
fun SavedFilesList(
    files: List<CalculationData>,
    onOpen: (CalculationData) -> Unit,
    onDeletedLocally: (CalculationData) -> Unit
) {
    // Kutijica za listu (scrollable)
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        items(files, key = { it.id }) { file ->
            // state za swipe
            val dismissState = rememberDismissState(confirmStateChange = { value ->
                if (value == DismissValue.DismissedToStart) {
                    // pokušaj brisanja iz Firestore; kad uspe, ukloni lokalno
                    deleteProjectFromFirestore("Full Baza", file.id) { success ->
                        if (success) onDeletedLocally(file)
                    }
                    true
                } else false
            })

            SwipeToDismiss(
                state = dismissState,
                directions = setOf(DismissDirection.EndToStart),
                background = {
                    // crvena pozadina sa ikonkom
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(vertical = 6.dp)
                            .background(MaterialTheme.colorScheme.error),
                        contentAlignment = Alignment.CenterEnd
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(end = 16.dp)
                        ) {
                            androidx.compose.material.Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Delete",
                                tint = Color.White
                            )
                            Spacer(Modifier.width(8.dp))
                            Text(text = "Delete", color = Color.White)
                        }
                    }
                },
                dismissContent = {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp),
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
                                    .padding(vertical = 12.dp)
                                    .fillMaxWidth()
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
