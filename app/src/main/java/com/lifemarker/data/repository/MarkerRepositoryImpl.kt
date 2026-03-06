package com.lifemarker.data.repository

import com.lifemarker.data.local.dao.CategoryDao
import com.lifemarker.data.local.dao.MarkerDao
import com.lifemarker.data.local.entity.CategoryEntity
import com.lifemarker.data.local.entity.MarkerEntity
import com.lifemarker.data.local.entity.MarkerWithCategory
import com.lifemarker.domain.model.Category
import com.lifemarker.domain.model.MarkerDetails
import com.lifemarker.domain.repository.MarkerRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MarkerRepositoryImpl @Inject constructor(
    private val markerDao: MarkerDao,
    private val categoryDao: CategoryDao
) : MarkerRepository {

    override fun getAllMarkers(): Flow<List<MarkerDetails>> {
        return markerDao.getAllMarkers().map { list ->
            list.map { it.toDomain() }
        }
    }

    override fun getMarkersByCategory(categoryId: Long): Flow<List<MarkerDetails>> {
        return markerDao.getMarkersByCategory(categoryId).map { list ->
            list.map { it.toDomain() }
        }
    }

    override suspend fun getMarkerById(id: Long): MarkerDetails? {
        val entity = markerDao.getMarkerById(id) ?: return null
        val categoryEntity = categoryDao.getCategoryById(entity.categoryId)
        return entity.toDomain(categoryEntity?.toDomain())
    }

    override suspend fun insertMarker(marker: MarkerDetails): Long {
        return markerDao.insertMarker(marker.toEntity())
    }

    override suspend fun updateMarker(marker: MarkerDetails) {
        markerDao.updateMarker(marker.toEntity())
    }

    override suspend fun deleteMarker(marker: MarkerDetails) {
        markerDao.deleteMarker(marker.toEntity())
    }

    private fun MarkerWithCategory.toDomain() = marker.toDomain(category?.toDomain())

    private fun MarkerEntity.toDomain(category: Category?) = MarkerDetails(
        id = id,
        categoryId = categoryId,
        latitude = latitude,
        longitude = longitude,
        timestamp = timestamp,
        note = note,
        photoUri = photoUri,
        category = category
    )

    private fun MarkerDetails.toEntity() = MarkerEntity(
        id = id,
        categoryId = categoryId,
        latitude = latitude,
        longitude = longitude,
        timestamp = timestamp,
        note = note,
        photoUri = photoUri
    )

    private fun CategoryEntity.toDomain() = Category(
        id = id,
        isSystemGenerated = isSystemGenerated,
        systemNameKey = systemNameKey,
        customName = customName,
        colorHex = colorHex,
        iconName = iconName
    )
}
