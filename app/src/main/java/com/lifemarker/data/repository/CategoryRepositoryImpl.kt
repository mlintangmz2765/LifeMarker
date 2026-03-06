package com.lifemarker.data.repository

import com.lifemarker.data.local.dao.CategoryDao
import com.lifemarker.data.local.entity.CategoryEntity
import com.lifemarker.domain.model.Category
import com.lifemarker.domain.repository.CategoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CategoryRepositoryImpl @Inject constructor(
    private val categoryDao: CategoryDao
) : CategoryRepository {
    
    override fun getAllCategories(): Flow<List<Category>> {
        return categoryDao.getAllCategories().map { list ->
            list.map { it.toDomain() }
        }
    }

    override suspend fun getCategoryById(id: Long): Category? {
        return categoryDao.getCategoryById(id)?.toDomain()
    }

    override suspend fun insertCategory(category: Category): Long {
        return categoryDao.insertCategory(category.toEntity())
    }

    override suspend fun updateCategory(category: Category) {
        categoryDao.updateCategory(category.toEntity())
    }

    override suspend fun deleteCategory(category: Category) {
        categoryDao.deleteCategory(category.toEntity())
    }

    private fun CategoryEntity.toDomain() = Category(
        id = id,
        isSystemGenerated = isSystemGenerated,
        systemNameKey = systemNameKey,
        customName = customName,
        colorHex = colorHex,
        iconName = iconName
    )

    private fun Category.toEntity() = CategoryEntity(
        id = id,
        isSystemGenerated = isSystemGenerated,
        systemNameKey = systemNameKey,
        customName = customName,
        colorHex = colorHex,
        iconName = iconName
    )
}
