package com.example.layceramictiles.dataBase

import com.example.layceramictiles.ui.screens.CalculationData
import com.example.layceramictiles.view.CustomWall
import com.example.layceramictiles.view.ProjectUiState
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions

class ProjectRepository {
    private val db = FirebaseFirestore.getInstance()
    private val collection = db.collection("Full Baza")

    fun getSavedFiles(onResult: (Result<List<CalculationData>>) -> Unit) {
        collection
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .limit(20)
            .get()
            .addOnSuccessListener { snap ->
                val files = snap.documents.map { CalculationData(id = it.id, fileName = it.id) }
                onResult(Result.success(files))
            }
            .addOnFailureListener { e ->
                onResult(Result.failure(e))
            }
    }

    fun deleteProject(fileId: String, onResult: (Result<Unit>) -> Unit) {
        collection
            .document(fileId)
            .delete()
            .addOnSuccessListener { onResult(Result.success(Unit)) }
            .addOnFailureListener { e -> onResult(Result.failure(e)) }
    }

    fun loadProject(fileName: String, onResult: (Result<ProjectUiState>) -> Unit) {
        collection.document(fileName).get()
            .addOnSuccessListener { doc ->
                if (doc != null && doc.exists()) {
                    // Backward compat: try new field names, fall back to old ones
                    val sideA = doc.getString("sideA") ?: doc.getString("widthA") ?: ""
                    val sideB = doc.getString("sideB") ?: doc.getString("lengthB") ?: ""
                    val sideC = doc.getString("sideC") ?: sideA // default to a for old rectangular data
                    val sideD = doc.getString("sideD") ?: sideB // default to b for old rectangular data

                    // Load custom walls
                    @Suppress("UNCHECKED_CAST")
                    val customWallsRaw = doc.get("customWalls") as? List<Map<String, Any>> ?: emptyList()
                    val customWalls = customWallsRaw.map { wallMap ->
                        CustomWall(
                            width = wallMap["width"] as? String ?: "",
                            height = wallMap["height"] as? String ?: ""
                        )
                    }
                    val useCustomWalls = doc.getBoolean("useCustomWalls") ?: customWalls.isNotEmpty()

                    val loadedState = ProjectUiState(
                        sideA = sideA,
                        sideB = sideB,
                        sideC = sideC,
                        sideD = sideD,
                        heightH = doc.getString("heightH") ?: "",
                        useCustomWalls = useCustomWalls,
                        customWalls = customWalls,
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
                } else {
                    onResult(Result.failure(Exception("Document does not exist.")))
                }
            }
            .addOnFailureListener { e -> onResult(Result.failure(e)) }
    }

    fun saveProject(fileName: String, projectState: ProjectUiState, onResult: (Result<Unit>) -> Unit) {
        val data = hashMapOf(
            "timestamp" to FieldValue.serverTimestamp(),
            // New field names
            "sideA" to projectState.sideA,
            "sideB" to projectState.sideB,
            "sideC" to projectState.sideC,
            "sideD" to projectState.sideD,
            "heightH" to projectState.heightH,
            // Custom walls
            "useCustomWalls" to projectState.useCustomWalls,
            "customWalls" to projectState.customWalls.map { wall ->
                mapOf("width" to wall.width, "height" to wall.height)
            },
            // Keep old field names for backward compat
            "widthA" to projectState.sideA,
            "lengthB" to projectState.sideB,
            // Results and materials
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
