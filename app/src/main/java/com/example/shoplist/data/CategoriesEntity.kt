package com.example.shoplist.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Category(
    @SerializedName("strCategory")  val title: String = ""
): Parcelable

@Parcelize
data class CategoriesEntity(
    @SerializedName("meals") val categories: List<Category> = emptyList()
): Parcelable