package com.lifemarker.domain.model

data class Category(
    val id: Long,
    val isSystemGenerated: Boolean,
    val systemNameKey: String?,
    val customName: String?,
    val colorHex: Int,
    val iconName: String
)
