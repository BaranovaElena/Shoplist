package com.example.shoplist.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import com.google.gson.annotations.SerializedName

@Parcelize
data class MealShortEntity(
    @SerializedName("strMeal")  val title: String = "",
    @SerializedName("strMealThumb") val imageUrl: String = "",
    @SerializedName("idMeal") val id: Int = 0
):Parcelable

@Parcelize
data class MealsEntity(
    val meals: List<MealShortEntity> = emptyList()
):Parcelable
