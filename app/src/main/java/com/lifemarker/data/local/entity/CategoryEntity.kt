package com.lifemarker.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "categories")
data class CategoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val isSystemGenerated: Boolean = false,
    val systemNameKey: String? = null, // e.g., "cat_eat" maps to string resource
    val customName: String? = null, // Custom user-defined category name
    val colorHex: Int, // ARGB color format
    val iconName: String // Identifier for built-in or compose vector icon
)
