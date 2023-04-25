package com.example.shoplist.domain.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Category(
    @SerializedName("strCategory")  val title: String = ""
): Parcelable

@Parcelize
data class CategoriesEntity(
    @SerializedName("meals") val categories: List<Category> = emptyList()
): Parcelable