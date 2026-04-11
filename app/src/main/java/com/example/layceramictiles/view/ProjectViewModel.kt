package com.example.layceramictiles.view

import android.os.Parcelable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.layceramictiles.dataBase.ProjectRepository
import kotlinx.coroutines.flow.StateFlow
import kotlinx.parcelize.Parcelize


@Parcelize
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

    val floorTilesNeeded: Float = 0f,
    val wallTilesNeeded: Float = 0f,

    // Rezultati
    val resultsCalculate: List<String?> = List(6) {null},
    val resultsMaterials: List<String?> = List(2) {null},
    // Naziv fajla za čuvanje/učitavanje
    val currentFileName: String? = null,

    val isAreaInputValid: Boolean = false,
    val isMaterialsInputValid: Boolean = false,

    val saveError: String? = null,
    val loadError: String? = null,
    val showSaveSuccessMessage: Boolean = false
        ) : Parcelable

class ProjectViewModel(private val savedStateHandle: SavedStateHandle): ViewModel() {

    private val repository = ProjectRepository()
    // Jedan StateFlow koji drži celo stanje (UiState)

       val uiState: StateFlow<ProjectUiState> = savedStateHandle.getStateFlow("uiState",
        ProjectUiState())

    // --- Funkcije koje UI poziva za promenu stanja ---
    private fun updateState(newState: ProjectUiState) {
        val isAreaValid = (newState.widthA.toFloatOrNull() ?: 0f) > 0f &&
                (newState.lengthB.toFloatOrNull() ?: 0f) > 0f &&
                (newState.heightH.toFloatOrNull() ?: 0f) > 0f

        val areMaterialsValid = (newState.tileWallWidth.toFloatOrNull() ?: 0f) > 0f &&
                (newState.tileWallLength.toFloatOrNull() ?: 0f) > 0f &&
                (newState.wallGroutWidth.toFloatOrNull() ?: 0f) > 0f &&
                (newState.tileFloorWidth.toFloatOrNull() ?: 0f) > 0f &&
                (newState.tileFloorLength.toFloatOrNull() ?: 0f) > 0f &&
                (newState.floorGroutWidth.toFloatOrNull() ?: 0f) > 0f

        savedStateHandle["uiState"] = newState.copy(
            isAreaInputValid = isAreaValid,
            isMaterialsInputValid = areMaterialsValid
        )
    }

    // --- SVE `on...Change` FUNKCIJE SADA IZGLEDAJU OVAKO ---

    fun onWidthAChange(newValue: String) {
        updateState(uiState.value.copy(widthA = newValue))
    }
    fun onLengthBChange(newValue: String) {
        updateState(uiState.value.copy(lengthB = newValue))
    }
    fun onHeightHChange(newValue: String) {
        updateState(uiState.value.copy(heightH = newValue))
    }
    fun onTileWallWidthChange(newValue: String) {
        updateState(uiState.value.copy(tileWallWidth = newValue))
    }
    fun onTileWallLengthChange(newValue: String) {
        updateState(uiState.value.copy(tileWallLength = newValue))
    }
    fun onTileFloorWidthChange(newValue: String) {
        updateState(uiState.value.copy(tileFloorWidth = newValue))
    }
    fun onTileFloorLengthChange(newValue: String) {
        updateState(uiState.value.copy(tileFloorLength = newValue))
    }
    fun onWallGroutWidthChange(newValue: String) {
        updateState(uiState.value.copy(wallGroutWidth = newValue))
    }
    fun onFloorGroutWidthChange(newValue: String) {
        updateState(uiState.value.copy(floorGroutWidth = newValue))
    }

    fun calculateArea() {
        val currentState = uiState.value
        val widthA = currentState.widthA.toFloatOrNull() ?: 0f
        val lengthB = currentState.lengthB.toFloatOrNull() ?: 0f
        val heightH = currentState.heightH.toFloatOrNull() ?: 0f

        val floorArea = widthA * lengthB
        val wallArea = 2 * (widthA + lengthB) * heightH
        val floorTiles = floorArea * 1.1f   // +10% waste margin for floor tiles
        val wallTiles = wallArea * 1.08f    // +8% waste margin for wall tiles
        val totalTiles = floorTiles + wallTiles

        val newResults = listOf(
            String.format("%.1f m²", floorArea + wallArea),
            String.format("%.1f m²", floorArea),
            String.format("%.1f m²", wallArea),
            String.format("%.1f m²", totalTiles),
            String.format("%.1f m²", floorTiles),
            String.format("%.1f m²", wallTiles)
        )

        // Pripremimo novo stanje i prosledimo ga centralnoj funkciji
        val newState = currentState.copy(
            resultsCalculate = newResults,
            floorTilesNeeded = floorTiles,
            wallTilesNeeded = wallTiles
        )
        updateState(newState) // <-- PROMENA
    }
    fun calculateMaterials() {
        val currentState = uiState.value
        // Uzimamo vrednosti direktno iz stanja (state)
        val floorTileArea = currentState.floorTilesNeeded
        val wallTileArea = currentState.wallTilesNeeded

        val tileFloorWidth = currentState.tileFloorWidth.toFloatOrNull() ?: 0f
        val tileFloorLength = currentState.tileFloorLength.toFloatOrNull() ?: 0f
        val tileWallWidth = currentState.tileWallWidth.toFloatOrNull() ?: 0f
        val tileWallLength = currentState.tileWallLength.toFloatOrNull() ?: 0f
        val floorGroutWidth = currentState.floorGroutWidth.toFloatOrNull() ?: 0f
        val wallGroutWidth = currentState.wallGroutWidth.toFloatOrNull() ?: 0f

        val adhesiveKg = (floorTileArea * 6.0 + wallTileArea * 5).toFloat() // ~6 kg/m² floor, ~5 kg/m² wall
        val wallTileGirth = 0.8f  // grout depth factor for wall tiles
        val floorTileGirth = 1f   // grout depth factor for floor tiles

        // Formula: area * (perimeter * groutWidth / 10 * depthFactor * 1.6 density) / tileArea
        // 1.6 = grout density (kg/dm³), /10 converts mm to cm
        val floorGrout = if (tileFloorLength > 0f && tileFloorWidth > 0f)
            floorTileArea * ((tileFloorLength + tileFloorWidth) * floorGroutWidth / 10 * floorTileGirth * 1.6f) / (tileFloorLength * tileFloorWidth)
        else 0f
        val wallGrout = if (tileWallLength > 0f && tileWallWidth > 0f)
            wallTileArea * ((tileWallLength + tileWallWidth) * wallGroutWidth / 10 * wallTileGirth * 1.6f) / (tileWallLength * tileWallWidth)
        else 0f
        val totalGrout = floorGrout + wallGrout


        val newResults = listOf(
            "%.1f kg".format(adhesiveKg),
            "%.1f kg".format(totalGrout)
        )
        val newState = currentState.copy(resultsMaterials = newResults)
        updateState(newState)
    }
    fun saveProject(fileName: String) {
        repository.saveProject(fileName, uiState.value) { result ->
            result.onSuccess {
                savedStateHandle["uiState"] = uiState.value.copy(showSaveSuccessMessage = true)
            }.onFailure { error ->
                savedStateHandle["uiState"] = uiState.value.copy(saveError = error.message)
            }
        }

    }
    fun loadProject(fileName: String, onDone: () -> Unit) {
        repository.loadProject(fileName) { result ->
            result.onSuccess { loadedState ->
                // Prolazimo kroz updateState da bi isAreaInputValid/isMaterialsInputValid bili ispravno izračunati
                updateState(loadedState)
                onDone()
            }.onFailure { error ->
                savedStateHandle["uiState"] = uiState.value.copy(loadError = error.message)
            }
        }
    }

    fun resetState() {
        savedStateHandle["uiState"] = ProjectUiState()
    }
    fun onSaveMessageShown() {
        savedStateHandle["uiState"] = uiState.value.copy(showSaveSuccessMessage = false)
    }
    }

