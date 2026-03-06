package com.lifemarker.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "categories")
data class CategoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val isSystemGenerated: Boolean = false,
    val systemNameKey: String? = null,
    val customName: String? = null,
    val colorHex: Int,
    val iconName: String
)
