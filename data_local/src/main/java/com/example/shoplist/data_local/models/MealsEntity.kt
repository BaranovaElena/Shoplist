package com.example.shoplist.data_local.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "meals")
data class MealShortEntityResponse(
    @ColumnInfo(name = "title")
    val title: String = "",

    @ColumnInfo(name = "imageUrl")
    val imageUrl: String = "",

    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Int = 0
)
