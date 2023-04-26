package com.example.shoplist.domain.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "meals")
data class MealShortEntity(
    @ColumnInfo(name = "title")
    val title: String = "",

    @ColumnInfo(name = "imageUrl")
    val imageUrl: String = "",

    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Int = 0
)

data class MealsEntity(
    val meals: List<MealShortEntity>? = emptyList()
)
