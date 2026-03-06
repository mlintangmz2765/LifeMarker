package com.lifemarker.domain.model

data class MarkerDetails(
    val id: Long,
    val categoryId: Long,
    val latitude: Double,
    val longitude: Double,
    val timestamp: Long,
    val note: String?,
    val photoUri: String? = null,
    val category: Category? = null // Joined later in UseCase or Repository
)
