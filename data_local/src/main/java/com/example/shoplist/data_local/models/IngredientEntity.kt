package com.example.shoplist.data_local.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ingredients")
data class IngredientEntityResponse(
    @PrimaryKey
    @ColumnInfo(name = "title")
    val title: String = "",

    @ColumnInfo(name = "measureTitle")
    val measureTitle: String = "",

    @ColumnInfo(name = "measureAmount")
    val measureAmount: Float = 0f,

    @ColumnInfo(name = "recipeId")
    val recipeId: Int = 0,

    @ColumnInfo(name = "recipeTitle")
    val recipeTitle: String = "",
)
