package com.lifemarker.data.local.entity

import androidx.room.Embedded
import androidx.room.Relation

data class MarkerWithCategory(
    @Embedded val marker: MarkerEntity,
    @Relation(
        parentColumn = "categoryId",
        entityColumn = "id"
    )
    val category: CategoryEntity?
)
