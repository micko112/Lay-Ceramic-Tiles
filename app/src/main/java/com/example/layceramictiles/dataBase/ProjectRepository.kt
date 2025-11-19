package com.example.layceramictiles.dataBase

import com.example.layceramictiles.ui.screens.CalculationData
import com.example.layceramictiles.view.ProjectUiState
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions

class ProjectRepository {
    private val db = FirebaseFirestore.getInstance()
    private val collection = db.collection("Full Baza")

    fun getSavedFiles(onResult: (Result<List<CalculationData>>) -> Unit){
        collection
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .limit(20)
            .get()
            .addOnSuccessListener {snap ->
                val files = snap.documents.map { CalculationData(id = it.id, fileName = it.id) }
                onResult(Result.success(files))
            }
            .addOnFailureListener { e ->
                onResult(Result.failure(e))
            }
    }
    fun deleteProject(fileId: String, onResult: (Result<Unit>) -> Unit){
        collection
            .document(fileId)
            .delete()
            .addOnSuccessListener { onResult(Result.success(Unit)) }
            .addOnFailureListener { e -> onResult(Result.failure(e)) }
    }
    fun loadProject(fileName: String, onResult: (Result<ProjectUiState>) -> Unit){
        collection.document(fileName).get()
            .addOnSuccessListener { doc ->
                if(doc!=null && doc.exists()) {
                    val loadedState = ProjectUiState(
                        widthA = doc.getString("widthA") ?: "",
                        lengthB = doc.getString("lengthB") ?: "",
                        heightH = doc.getString("heightH") ?: "",
                        resultsCalculate = doc.get("resultsCalculate") as? List<String?> ?: emptyList(),
                        tileWallWidth = doc.getString("tileWallWidth") ?: "",
                        tileWallLength = doc.getString("tileWallLength") ?: "",
                        tileFloorWidth = doc.getString("tileFloorWidth") ?: "",
                        tileFloorLength = doc.getString("tileFloorLength") ?: "",
                        wallGroutWidth = doc.getString("wallGroutWidth") ?: "",
                        floorGroutWidth = doc.getString("floorGroutWidth") ?: "",

                        floorTilesNeeded = (doc.getDouble("floorTileArea") ?: 0.0).toFloat(),
                        wallTilesNeeded = (doc.getDouble("wallTileArea") ?: 0.0).toFloat(),

                        resultsMaterials = listOf(
                            doc.getString("adhesive"),
                            doc.getString("grout")
                        ),
                        currentFileName = fileName
                    )
                    onResult(Result.success(loadedState))
                }else {
                    onResult(Result.failure(Exception("Document does not exist.")))
                }
            }
            .addOnFailureListener { e -> onResult(Result.failure(e)) }
    }
    fun saveProject(fileName: String,projectState: ProjectUiState ,onResult: (Result<Unit>) -> Unit){
        val data = hashMapOf(
            "timestamp" to FieldValue.serverTimestamp(),
            "widthA" to projectState.widthA,
            "lengthB" to projectState.lengthB,
            "heightH" to projectState.heightH,
            "resultsCalculate" to projectState.resultsCalculate,
            "tileWallWidth" to projectState.tileWallWidth,
            "tileWallLength" to projectState.tileWallLength,
            "tileFloorWidth" to projectState.tileFloorWidth,
            "tileFloorLength" to projectState.tileFloorLength,
            "wallGroutWidth" to projectState.wallGroutWidth,
            "floorGroutWidth" to projectState.floorGroutWidth,

            "floorTileArea" to projectState.floorTilesNeeded,
            "wallTileArea" to projectState.wallTilesNeeded,

            "adhesive" to projectState.resultsMaterials.getOrNull(0),
            "grout" to projectState.resultsMaterials.getOrNull(1)
        )
        collection
            .document(fileName)
            .set(data, SetOptions.merge())
            .addOnSuccessListener { onResult(Result.success(Unit)) }
            .addOnFailureListener { e -> onResult(Result.failure(e)) }
    }
}
