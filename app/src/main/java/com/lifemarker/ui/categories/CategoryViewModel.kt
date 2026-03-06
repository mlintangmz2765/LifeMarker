package com.lifemarker.ui.categories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lifemarker.domain.model.Category
import com.lifemarker.domain.repository.CategoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CategoryUiState(
    val categories: List<Category> = emptyList(),
    val isAddingCategory: Boolean = false,
    val newCategoryName: String = "",
    val newCategoryColor: Int = 0xFFFF9800.toInt(),
    val newCategoryIcon: String = "Star"
)

@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val categoryRepository: CategoryRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CategoryUiState())
    val uiState: StateFlow<CategoryUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            categoryRepository.getAllCategories().collect { categories ->
                _uiState.update { it.copy(categories = categories) }
            }
        }
    }

    fun startAddingCategory() {
        _uiState.update { it.copy(isAddingCategory = true, newCategoryName = "") }
    }

    fun cancelAddingCategory() {
        _uiState.update { it.copy(isAddingCategory = false) }
    }

    fun updateNewCategoryName(name: String) {
        _uiState.update { it.copy(newCategoryName = name) }
    }

    fun updateNewCategoryColor(color: Int) {
        _uiState.update { it.copy(newCategoryColor = color) }
    }

    fun saveCategory() {
        val state = _uiState.value
        if (state.newCategoryName.isBlank()) return

        viewModelScope.launch {
            categoryRepository.insertCategory(
                Category(
                    id = 0,
                    isSystemGenerated = false,
                    systemNameKey = null,
                    customName = state.newCategoryName,
                    colorHex = state.newCategoryColor,
                    iconName = state.newCategoryIcon
                )
            )
            cancelAddingCategory()
        }
    }
}
