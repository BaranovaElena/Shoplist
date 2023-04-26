package com.example.shoplist.data_remote.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import com.google.gson.annotations.SerializedName

@Parcelize
data class MealEntityResponse(
    @SerializedName("strMeal") val title: String = "",
    @SerializedName("strMealThumb") val imageUrl: String = "",
    @SerializedName("idMeal") val id: Int = 0
):Parcelable

@Parcelize
data class MealsEntityResponse(
    val meals: List<MealEntityResponse>? = emptyList()
):Parcelable
