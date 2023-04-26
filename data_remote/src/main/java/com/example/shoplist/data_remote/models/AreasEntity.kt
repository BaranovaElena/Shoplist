package com.example.shoplist.data_remote.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class AreaResponse(
    @SerializedName("strArea") val title: String = ""
) : Parcelable

@Parcelize
data class AreasEntityResponse(
    @SerializedName("meals") val areas: List<AreaResponse> = emptyList()
) : Parcelable

