package com.example.shoplist.data_remote.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class CategoryResponse(
    @SerializedName("strCategory")  val title: String = ""
): Parcelable

@Parcelize
data class CategoriesEntityResponse(
    @SerializedName("meals") val categories: List<CategoryResponse> = emptyList()
): Parcelable