package com.example.shoplist.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Area(
    @SerializedName("strArea") val title: String = ""
) : Parcelable

@Parcelize
data class AreasEntity(
    @SerializedName("meals") val areas: List<Area> = emptyList()
) : Parcelable

