package com.example.shoplist.data

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import com.google.gson.annotations.SerializedName

@Entity(tableName = "meals")
@Parcelize
data class MealShortEntity(
    @ColumnInfo(name = "title")
    @SerializedName("strMeal")
    val title: String = "",

    @ColumnInfo(name = "imageUrl")
    @SerializedName("strMealThumb")
    val imageUrl: String = "",

    @PrimaryKey
    @ColumnInfo(name = "id")
    @SerializedName("idMeal") val id: Int = 0
):Parcelable

@Parcelize
data class MealsEntity(
    val meals: List<MealShortEntity> = emptyList()
):Parcelable
