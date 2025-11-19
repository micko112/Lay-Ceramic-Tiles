package com.example.layceramictiles.view

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.flow.MutableStateFlow

class ScreenMaterialsViewModel : ViewModel() {
    var tileWallWidth = MutableStateFlow("")
    var tileWallLength = MutableStateFlow("")
    var tileFloorWidth = MutableStateFlow("")
    var tileFloorLength = MutableStateFlow("")
    var wallGroutWidth = MutableStateFlow("")
    var floorGroutWidth = MutableStateFlow("")
    var wallTileArea = MutableStateFlow(0f)
    var floorTileArea = MutableStateFlow(0f)
    var results = MutableStateFlow(listOf<String?>(null, null))

    fun saveMaterialsCalculations(
        fileName: String,
    ) {
        val db = Firebase.firestore

        val resultData = hashMapOf(
            "tileWallWidth" to tileWallWidth.value,
            "tileWallLength" to tileWallLength.value,
            "tileFloorWidth" to tileFloorWidth.value,
            "tileFloorLength" to tileFloorLength.value,
            "wallGroutWidth" to wallGroutWidth.value,
            "floorGroutWidth" to floorGroutWidth.value,
            "wallTileArea" to wallTileArea.value,
            "floorTileArea" to floorTileArea.value,
            "adhesive" to results.value.getOrNull(0),
            "grout" to results.value.getOrNull(1),
            "timestamp" to FieldValue.serverTimestamp()
        )

        db.collection("testKolekcija")
            .document(fileName)
            .set(resultData)
            .addOnSuccessListener {
                Log.d("Firestore", "DocumentSnapshot saved with ID: $fileName")
            }
            .addOnFailureListener { e ->
                Log.w("Firestore", "Error saving document", e)
            }

    }
}