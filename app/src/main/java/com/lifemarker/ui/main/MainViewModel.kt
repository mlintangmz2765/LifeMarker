package com.lifemarker.ui.main

import android.location.Location
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lifemarker.domain.model.Category
import com.lifemarker.domain.model.MarkerDetails
import com.lifemarker.domain.repository.CategoryRepository
import com.lifemarker.domain.repository.MarkerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MainUiState(
    val markers: List<MarkerDetails> = emptyList(),
    val categories: List<Category> = emptyList(),
    val isAddingMarker: Boolean = false,
    val selectedLocation: Location? = null,
    val newMarkerNote: String = "",
    val newMarkerCategoryId: Long? = null,
    val newMarkerPhotoUri: String? = null,
    val editingMarkerId: Long? = null,
    val editingMarkerTimestamp: Long? = null
)

@HiltViewModel
class MainViewModel @Inject constructor(
    private val markerRepository: MarkerRepository,
    private val categoryRepository: CategoryRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            markerRepository.getAllMarkers().collect { markers ->
                _uiState.update { it.copy(markers = markers) }
            }
        }
        viewModelScope.launch {
            categoryRepository.getAllCategories().collect { categories ->
                _uiState.update { 
                    val selectedCategory = it.newMarkerCategoryId ?: categories.firstOrNull()?.id
                    it.copy(categories = categories, newMarkerCategoryId = selectedCategory) 
                }
            }
        }
    }

    fun startAddingMarker(location: Location?) {
        _uiState.update { it.copy(
            isAddingMarker = true, 
            editingMarkerId = null,
            editingMarkerTimestamp = null,
            selectedLocation = location,
            newMarkerNote = "",
            newMarkerPhotoUri = null,
            newMarkerCategoryId = it.categories.firstOrNull()?.id
        ) }
    }

    fun startEditingMarker(marker: MarkerDetails) {
        val location = Location("").apply {
            latitude = marker.latitude
            longitude = marker.longitude
        }
        _uiState.update { it.copy(
            isAddingMarker = true,
            editingMarkerId = marker.id,
            editingMarkerTimestamp = marker.timestamp,
            selectedLocation = location,
            newMarkerNote = marker.note ?: "",
            newMarkerPhotoUri = marker.photoUri,
            newMarkerCategoryId = marker.categoryId
        ) }
    }

    fun cancelAddingMarker() {
        _uiState.update { it.copy(
            isAddingMarker = false, 
            editingMarkerId = null,
            editingMarkerTimestamp = null,
            newMarkerNote = "", 
            newMarkerPhotoUri = null,
            selectedLocation = null
        ) }
    }

    fun updateNewMarkerNote(note: String) {
        _uiState.update { it.copy(newMarkerNote = note) }
    }

    fun updateNewMarkerPhotoUri(uri: String?) {
        _uiState.update { it.copy(newMarkerPhotoUri = uri) }
    }

    fun selectCategory(categoryId: Long) {
        _uiState.update { it.copy(newMarkerCategoryId = categoryId) }
    }

    fun deleteMarker() {
        val editingId = _uiState.value.editingMarkerId ?: return
        viewModelScope.launch {
            val marker = _uiState.value.markers.find { it.id == editingId }
            if (marker != null) {
                markerRepository.deleteMarker(marker)
            }
            cancelAddingMarker()
        }
    }

    fun saveMarker() {
        val state = _uiState.value
        val lat = state.selectedLocation?.latitude ?: return
        val lng = state.selectedLocation?.longitude ?: return
        val catId = state.newMarkerCategoryId ?: return

        viewModelScope.launch {
            val markerDetails = MarkerDetails(
                id = state.editingMarkerId ?: 0,
                categoryId = catId,
                latitude = lat,
                longitude = lng,
                timestamp = System.currentTimeMillis(),
                note = state.newMarkerNote.takeIf { it.isNotBlank() },
                photoUri = state.newMarkerPhotoUri,
                category = null
            )
            
            if (state.editingMarkerId != null) {
                markerRepository.updateMarker(markerDetails)
            } else {
                markerRepository.insertMarker(markerDetails)
            }
            cancelAddingMarker()
        }
    }
}
