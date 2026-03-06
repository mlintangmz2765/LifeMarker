package com.lifemarker.domain.repository

import com.lifemarker.domain.model.MarkerDetails
import kotlinx.coroutines.flow.Flow

interface MarkerRepository {
    fun getAllMarkers(): Flow<List<MarkerDetails>>
    fun getMarkersByCategory(categoryId: Long): Flow<List<MarkerDetails>>
    suspend fun getMarkerById(id: Long): MarkerDetails?
    suspend fun insertMarker(marker: MarkerDetails): Long
    suspend fun updateMarker(marker: MarkerDetails)
    suspend fun deleteMarker(marker: MarkerDetails)
}
