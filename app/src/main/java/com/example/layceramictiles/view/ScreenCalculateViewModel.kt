package com.example.layceramictiles.view

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.flow.MutableStateFlow

class ScreenCalculateViewModel : ViewModel() {
    var lengthB = MutableStateFlow("")
    var widthA = MutableStateFlow("")
    var heightH = MutableStateFlow("")
    var results = MutableStateFlow(listOf<String?>(null, null, null, null, null, null))

    private val db = Firebase.firestore

    fun saveAreaCalculations(
        fileName: String,
    ){
        val resultData = hashMapOf(
            "lengthB" to lengthB.value,
            "widthA" to widthA.value,
            "heightH" to heightH.value,
            "results" to results.value,
            "timestamp" to FieldValue.serverTimestamp()
        )
        db.collection("testKolekcija")
            .document(fileName)
            .set(resultData) // ✔️ koristi .set umesto .add
            .addOnSuccessListener {
                Log.d("Firestore", "DocumentSnapshot saved with ID: $fileName")
            }
            .addOnFailureListener { e ->
                Log.w("Firestore", "Error saving document", e)
            }
    }
}