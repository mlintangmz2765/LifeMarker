package com.lifemarker.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.lifemarker.R
import com.lifemarker.data.local.AppDatabase
import com.lifemarker.data.local.dao.CategoryDao
import com.lifemarker.data.local.dao.MarkerDao
import com.lifemarker.data.local.entity.CategoryEntity
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Provider
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context,
        provider: Provider<CategoryDao>
    ): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "lifemarker.db"
        )
        .addMigrations(MIGRATION_1_2)
        .addCallback(object : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                // Seed initial data
                CoroutineScope(Dispatchers.IO).launch {
                    val categoryDao = provider.get()
                    seedDefaultCategories(categoryDao)
                }
            }
        })
        .build()
    }

    private val MIGRATION_1_2 = object : Migration(1, 2) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("ALTER TABLE markers ADD COLUMN photoUri TEXT")
        }
    }

    private suspend fun seedDefaultCategories(categoryDao: CategoryDao) {
        val defaultCategories = listOf(
            CategoryEntity(
                isSystemGenerated = true,
                systemNameKey = "cat_eat",
                colorHex = 0xFFFF9800.toInt(), // Orange
                iconName = "Restaurant" // Will map to Icons.Filled.Restaurant
            ),
            CategoryEntity(
                isSystemGenerated = true,
                systemNameKey = "cat_toilet",
                colorHex = 0xFF2196F3.toInt(), // Blue
                iconName = "Wc"
            ),
            CategoryEntity(
                isSystemGenerated = true,
                systemNameKey = "cat_pray",
                colorHex = 0xFF4CAF50.toInt(), // Green
                iconName = "Mosque" // Or Spa/SelfImprovement as generic
            )
        )
        defaultCategories.forEach { categoryDao.insertCategory(it) }
    }

    @Provides
    @Singleton
    fun provideCategoryDao(db: AppDatabase): CategoryDao {
        return db.categoryDao
    }

    @Provides
    @Singleton
    fun provideMarkerDao(db: AppDatabase): MarkerDao {
        return db.markerDao
    }
}
