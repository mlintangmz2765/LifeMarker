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
    val editingCategoryId: Long? = null,
    val isEditingSystemCategory: Boolean = false,
    val editingSystemNameKey: String? = null,
    val newCategoryName: String = "",
    val newCategoryColor: Int = 0xFFFF9800.toInt(),
    val newCategoryIcon: String = "Place"
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
        _uiState.update { it.copy(
            isAddingCategory = true, 
            editingCategoryId = null,
            isEditingSystemCategory = false,
            editingSystemNameKey = null,
            newCategoryName = "",
            newCategoryColor = 0xFFFF9800.toInt(),
            newCategoryIcon = "Place"
        ) }
    }

    fun startEditingCategory(category: Category) {
        _uiState.update { it.copy(
            isAddingCategory = true,
            editingCategoryId = category.id,
            isEditingSystemCategory = category.isSystemGenerated,
            editingSystemNameKey = category.systemNameKey,
            newCategoryName = category.customName ?: "",
            newCategoryColor = category.colorHex,
            newCategoryIcon = category.iconName
        ) }
    }

    fun cancelAddingCategory() {
        _uiState.update { it.copy(isAddingCategory = false, editingCategoryId = null) }
    }

    fun updateNewCategoryName(name: String) {
        _uiState.update { it.copy(newCategoryName = name) }
    }

    fun updateNewCategoryColor(color: Int) {
        _uiState.update { it.copy(newCategoryColor = color) }
    }

    fun updateNewCategoryIcon(iconName: String) {
        _uiState.update { it.copy(newCategoryIcon = iconName) }
    }

    fun deleteCategory(category: Category) {
        viewModelScope.launch {
            categoryRepository.deleteCategory(category)
        }
    }

    fun saveCategory() {
        val state = _uiState.value
        if (state.newCategoryName.isBlank()) return

        viewModelScope.launch {
            val category = Category(
                id = state.editingCategoryId ?: 0,
                isSystemGenerated = state.isEditingSystemCategory,
                systemNameKey = state.editingSystemNameKey,
                customName = state.newCategoryName,
                colorHex = state.newCategoryColor,
                iconName = state.newCategoryIcon
            )
            
            if (state.editingCategoryId != null) {
                categoryRepository.updateCategory(category)
            } else {
                categoryRepository.insertCategory(category)
            }
            cancelAddingCategory()
        }
    }
}
