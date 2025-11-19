package com.example.layceramictiles.view

import androidx.lifecycle.ViewModel
import com.example.layceramictiles.dataBase.ProjectRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class ProjectUiState(
    // AREA
    val widthA: String = "",
    val lengthB: String = "",
    val heightH: String = "",
    // MATERIALS

    val tileWallWidth: String = "",
    val tileWallLength: String = "",
    val tileFloorWidth: String = "",
    val tileFloorLength: String = "",
    val wallGroutWidth: String = "",
    val floorGroutWidth: String = "",
    //val wallTileArea: String = "",
    //val floorTileArea: String = "",

    // Rezultati
    val resultsCalculate: List<String?> = List(6) {null},
    val resultsMaterials: List<String?> = List(2) {null},
    // Naziv fajla za čuvanje/učitavanje
    var currentFileName: String? = null,
    val saveError: String? = null,
    val loadError: String? = null,
    val showSaveSuccessMessage: Boolean = false
        )
class ProjectViewModel: ViewModel() {

    private val repository = ProjectRepository()
    // Jedan StateFlow koji drži celo stanje (UiState)
    private val _uiState = MutableStateFlow(ProjectUiState())
    val uiState = _uiState.asStateFlow()

    // --- Funkcije koje UI poziva za promenu stanja ---
    fun onWidthAChange(newValue: String) {
        _uiState.update { it.copy(widthA = newValue) }
    }
    fun onLengthBChange(newValue: String) {
        _uiState.update { it.copy(lengthB = newValue) }
    }
    fun onHeightHChange(newValue: String) {
        _uiState.update { it.copy(heightH = newValue) }
    }
    fun onTileWallWidthChange(newValue: String) = _uiState.update { it.copy(tileWallWidth = newValue) }
    fun onTileWallLengthChange(newValue: String) = _uiState.update { it.copy(tileWallLength = newValue) }
    fun onTileFloorWidthChange(newValue: String) = _uiState.update { it.copy(tileFloorWidth = newValue) }
    fun onTileFloorLengthChange(newValue: String) = _uiState.update { it.copy(tileFloorLength = newValue) }
    fun onWallGroutWidthChange(newValue: String) = _uiState.update { it.copy(wallGroutWidth = newValue) }
    fun onFloorGroutWidthChange(newValue: String) = _uiState.update { it.copy(floorGroutWidth = newValue) }
    //fun onWallTileAreaChange(newValue: String) = _uiState.update { it.copy(wallGroutWidth = newValue) }
    //fun onFloorTileAreaChange(newValue: String) = _uiState.update { it.copy(floorGroutWidth = newValue) }
    fun calculateArea() {
        val widthA = _uiState.value.widthA.toFloatOrNull() ?: 0f
        val lengthB = _uiState.value.lengthB.toFloatOrNull() ?: 0f
        val heightH = _uiState.value.heightH.toFloatOrNull() ?: 0f

        val floorArea = widthA * lengthB
        val wallArea = 2 * (widthA + lengthB) * heightH
        val floorTiles = floorArea * 1.1f // 10% waste
        val wallTiles = wallArea * 1.08f // 8% waste
        val totalTiles = floorTiles + wallTiles
        val newResults = listOf(
            "%.1fm²".format(floorArea + wallArea),
            "%.1fm²".format(floorArea),
            "%.1fm²".format(wallArea),
            "%.1fm²".format(totalTiles),
            "%.1fm²".format(floorTiles),
            "%.1fm²".format(wallTiles)
        )
        _uiState.update { it.copy(resultsCalculate = newResults) }
    }
    fun calculateMaterials() {
        // Uzimamo vrednosti direktno iz stanja (state)
        val floorTileArea = _uiState.value.resultsCalculate.getOrNull(4)?.replace("m²", "")?.trim()?.toFloatOrNull() ?: 0f
        val wallTileArea = _uiState.value.resultsCalculate.getOrNull(5)?.replace("m²", "")?.trim()?.toFloatOrNull() ?: 0f

        val tileFloorWidth = _uiState.value.tileFloorWidth.toFloatOrNull() ?: 0f
        val tileFloorLength = _uiState.value.tileFloorLength.toFloatOrNull() ?: 0f
        val tileWallWidth = _uiState.value.tileWallWidth.toFloatOrNull() ?: 0f
        val tileWallLength = _uiState.value.tileWallLength.toFloatOrNull() ?: 0f
        val floorGroutWidth = _uiState.value.floorGroutWidth.toFloatOrNull() ?: 0f
        val wallGroutWidth = _uiState.value.wallGroutWidth.toFloatOrNull() ?: 0f

        val adhesiveKg = (floorTileArea * 6.0 + wallTileArea * 5).toFloat()
        val wallTileGirth = 0.8f
        val floorTileGirth = 1f

        val floorGrout = floorTileArea * ((tileFloorLength + tileFloorWidth) * floorGroutWidth / 10 * floorTileGirth * 1.6f) / (tileFloorLength * tileFloorWidth)
        val wallGrout = wallTileArea * ((tileWallLength + tileWallWidth) * wallGroutWidth / 10 * wallTileGirth * 1.6f) / (tileWallLength * tileWallWidth)
        val totalGrout = if (floorGrout.isNaN() || wallGrout.isNaN()) 0f else floorGrout + wallGrout


        val newResults = listOf(
            "%.1f kg".format(adhesiveKg),
            "%.1f kg".format(totalGrout)
        )
        _uiState.update { it.copy(resultsMaterials = newResults) }
    }
    fun saveProject(fileName: String) {
        repository.saveProject(fileName, _uiState.value) { result ->
            result.onSuccess {
                _uiState.update { it.copy(showSaveSuccessMessage = true) }
            }.onFailure {error ->
                _uiState.update { it.copy(saveError = error.message) }
            }
        }

    }
    fun loadProject(fileName: String, onDone: () -> Unit) {
        repository.loadProject(fileName) { result ->
            result.onSuccess { loadedState ->
                _uiState.value = loadedState
                onDone()
            }.onFailure { error -> _uiState.update { it.copy(loadError = error.message) } }
        }
    }

    fun resetState() {
        _uiState.value = ProjectUiState()
    }
    }

