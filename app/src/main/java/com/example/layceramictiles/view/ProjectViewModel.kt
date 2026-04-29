package com.example.layceramictiles.view

import android.os.Parcelable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.layceramictiles.dataBase.ProjectRepository
import kotlinx.coroutines.flow.StateFlow
import kotlinx.parcelize.Parcelize

@Parcelize
data class CustomWall(
    val width: String = "",
    val height: String = ""
) : Parcelable

@Parcelize
data class ProjectUiState(
    // AREA - room sides
    val sideA: String = "",
    val sideB: String = "",
    val sideC: String = "",
    val sideD: String = "",
    val heightH: String = "",

    // Custom walls
    val useCustomWalls: Boolean = false,
    val customWalls: List<CustomWall> = emptyList(),

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
    val resultsCalculate: List<String?> = List(6) { null },
    val resultsMaterials: List<String?> = List(2) { null },
    val currentFileName: String? = null,

    val isAreaInputValid: Boolean = false,
    val isMaterialsInputValid: Boolean = false,

    val saveError: String? = null,
    val loadError: String? = null,
    val showSaveSuccessMessage: Boolean = false
) : Parcelable

class ProjectViewModel(private val savedStateHandle: SavedStateHandle) : ViewModel() {

    private val repository = ProjectRepository()

    val uiState: StateFlow<ProjectUiState> = savedStateHandle.getStateFlow("uiState", ProjectUiState())

    private fun updateState(newState: ProjectUiState) {
        val sidesValid = (newState.sideA.toFloatOrNull() ?: 0f) > 0f &&
                (newState.sideB.toFloatOrNull() ?: 0f) > 0f &&
                (newState.sideC.toFloatOrNull() ?: 0f) > 0f &&
                (newState.sideD.toFloatOrNull() ?: 0f) > 0f

        val wallsValid = if (newState.useCustomWalls) {
            newState.customWalls.isNotEmpty() && newState.customWalls.all { wall ->
                (wall.width.toFloatOrNull() ?: 0f) > 0f &&
                        (wall.height.toFloatOrNull() ?: 0f) > 0f
            }
        } else {
            (newState.heightH.toFloatOrNull() ?: 0f) > 0f
        }

        val isAreaValid = sidesValid && wallsValid

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

    // --- Room side changes ---
    fun onSideAChange(newValue: String) {
        updateState(uiState.value.copy(sideA = newValue))
    }

    fun onSideBChange(newValue: String) {
        updateState(uiState.value.copy(sideB = newValue))
    }

    fun onSideCChange(newValue: String) {
        updateState(uiState.value.copy(sideC = newValue))
    }

    fun onSideDChange(newValue: String) {
        updateState(uiState.value.copy(sideD = newValue))
    }

    fun onHeightHChange(newValue: String) {
        updateState(uiState.value.copy(heightH = newValue))
    }

    // --- Custom walls ---
    fun addCustomWall() {
        val current = uiState.value
        updateState(
            current.copy(
                customWalls = current.customWalls + CustomWall(),
                useCustomWalls = true
            )
        )
    }

    fun removeCustomWall(index: Int) {
        val current = uiState.value
        val newWalls = current.customWalls.toMutableList().apply { removeAt(index) }
        updateState(
            current.copy(
                customWalls = newWalls,
                useCustomWalls = newWalls.isNotEmpty()
            )
        )
    }

    fun onCustomWallWidthChange(index: Int, newValue: String) {
        val current = uiState.value
        val newWalls = current.customWalls.toMutableList()
        newWalls[index] = newWalls[index].copy(width = newValue)
        updateState(current.copy(customWalls = newWalls))
    }

    fun onCustomWallHeightChange(index: Int, newValue: String) {
        val current = uiState.value
        val newWalls = current.customWalls.toMutableList()
        newWalls[index] = newWalls[index].copy(height = newValue)
        updateState(current.copy(customWalls = newWalls))
    }

    fun clearCustomWalls() {
        updateState(
            uiState.value.copy(
                customWalls = emptyList(),
                useCustomWalls = false
            )
        )
    }

    // --- Tile dimension changes ---
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

    // --- Calculations ---
    fun calculateArea() {
        val currentState = uiState.value
        val a = currentState.sideA.toFloatOrNull() ?: 0f
        val b = currentState.sideB.toFloatOrNull() ?: 0f
        val c = currentState.sideC.toFloatOrNull() ?: 0f
        val d = currentState.sideD.toFloatOrNull() ?: 0f
        val h = currentState.heightH.toFloatOrNull() ?: 0f

        // Floor area: trapezoid approximation — works exactly for rectangles (a=c, b=d)
        val floorArea = ((a + c) / 2f) * ((b + d) / 2f)

        // Wall area: custom walls or simple perimeter * height
        val wallArea = if (currentState.useCustomWalls && currentState.customWalls.isNotEmpty()) {
            currentState.customWalls.sumOf { wall ->
                val w = wall.width.toFloatOrNull() ?: 0f
                val wh = wall.height.toFloatOrNull() ?: 0f
                (w * wh).toDouble()
            }.toFloat()
        } else {
            (a + b + c + d) * h
        }

        val floorTiles = floorArea * 1.1f   // +10% waste margin
        val wallTiles = wallArea * 1.08f     // +8% waste margin
        val totalTiles = floorTiles + wallTiles

        val newResults = listOf(
            String.format("%.1f m\u00B2", floorArea + wallArea),
            String.format("%.1f m\u00B2", floorArea),
            String.format("%.1f m\u00B2", wallArea),
            String.format("%.1f m\u00B2", totalTiles),
            String.format("%.1f m\u00B2", floorTiles),
            String.format("%.1f m\u00B2", wallTiles)
        )

        val newState = currentState.copy(
            resultsCalculate = newResults,
            floorTilesNeeded = floorTiles,
            wallTilesNeeded = wallTiles
        )
        updateState(newState)
    }

    fun calculateMaterials() {
        val currentState = uiState.value
        val floorTileArea = currentState.floorTilesNeeded
        val wallTileArea = currentState.wallTilesNeeded

        val tileFloorWidth = currentState.tileFloorWidth.toFloatOrNull() ?: 0f
        val tileFloorLength = currentState.tileFloorLength.toFloatOrNull() ?: 0f
        val tileWallWidth = currentState.tileWallWidth.toFloatOrNull() ?: 0f
        val tileWallLength = currentState.tileWallLength.toFloatOrNull() ?: 0f
        val floorGroutWidth = currentState.floorGroutWidth.toFloatOrNull() ?: 0f
        val wallGroutWidth = currentState.wallGroutWidth.toFloatOrNull() ?: 0f

        val adhesiveKg = (floorTileArea * 6.0 + wallTileArea * 5).toFloat()
        val wallTileGirth = 0.8f
        val floorTileGirth = 1f

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

    // --- Save / Load / Reset ---
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
