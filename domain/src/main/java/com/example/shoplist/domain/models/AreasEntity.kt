package com.example.shoplist.domain.models

data class Area(
    val title: String = ""
)

data class AreasEntity(
    val areas: List<Area> = emptyList()
)

