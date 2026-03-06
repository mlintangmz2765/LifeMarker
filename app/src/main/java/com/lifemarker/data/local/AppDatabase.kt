package com.lifemarker.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.lifemarker.data.local.dao.CategoryDao
import com.lifemarker.data.local.dao.MarkerDao
import com.lifemarker.data.local.entity.CategoryEntity
import com.lifemarker.data.local.entity.MarkerEntity

@Database(
    entities = [CategoryEntity::class, MarkerEntity::class],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract val categoryDao: CategoryDao
    abstract val markerDao: MarkerDao
}
