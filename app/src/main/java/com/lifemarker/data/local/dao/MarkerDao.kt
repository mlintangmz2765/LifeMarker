package com.lifemarker.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.lifemarker.data.local.entity.MarkerEntity
import com.lifemarker.data.local.entity.MarkerWithCategory
import kotlinx.coroutines.flow.Flow

@Dao
interface MarkerDao {
    @Transaction
    @Query("SELECT * FROM markers ORDER BY timestamp DESC")
    fun getAllMarkers(): Flow<List<MarkerWithCategory>>

    @Transaction
    @Query("SELECT * FROM markers WHERE categoryId = :categoryId ORDER BY timestamp DESC")
    fun getMarkersByCategory(categoryId: Long): Flow<List<MarkerWithCategory>>

    @Query("SELECT * FROM markers WHERE id = :id")
    suspend fun getMarkerById(id: Long): MarkerEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMarker(marker: MarkerEntity): Long

    @Update
    suspend fun updateMarker(marker: MarkerEntity)

    @Delete
    suspend fun deleteMarker(marker: MarkerEntity)
}
