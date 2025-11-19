package com.example.layceramictiles.view

import androidx.lifecycle.ViewModel
import com.example.layceramictiles.ui.screens.CalculationData
import com.example.layceramictiles.dataBase.ProjectRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class Screen1UiState (
    val loading: Boolean = true,
    val files: List<CalculationData> = emptyList(),
    val error: String? = null
)
class Screen1ViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    private val repository = ProjectRepository()
    private val _uiState = MutableStateFlow(Screen1UiState())
    val uiState = _uiState.asStateFlow()


    fun loadSavedFiles() {
        _uiState.update { it.copy(loading = true, error = null) }

        repository.getSavedFiles { result ->
            result.onSuccess { files ->
                _uiState.update { it.copy(loading = false,files=files) }
            }.onFailure { error->
                _uiState.update { it.copy(loading = false, error = "Failed to load files: ${error.message}") }
            }
        }
    }
    fun deleteProject(file: CalculationData){
        repository.deleteProject(file.id){result ->
            result.onSuccess {
                _uiState.update { currentState ->
                    val updatedFiles = currentState.files.filter { it.id != file.id }
                    currentState.copy(files= updatedFiles)
                }
            }.onFailure { error ->
                _uiState.update { it.copy(error = "Failed to delete ${file.fileName}: ${error.message}") }
            }

        }
    }
}