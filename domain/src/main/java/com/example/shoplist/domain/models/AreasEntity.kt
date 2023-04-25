package com.example.shoplist.domain.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Area(
    @SerializedName("strArea") val title: String = ""
) : Parcelable

@Parcelize
data class AreasEntity(
    @SerializedName("meals") val areas: List<Area> = emptyList()
) : Parcelable

