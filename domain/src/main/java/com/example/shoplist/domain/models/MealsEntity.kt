package com.example.shoplist.domain.models

data class MealShortEntity(
    val title: String = "",
    val imageUrl: String = "",
    val id: Int = 0
)

data class MealsEntity(
    val meals: List<MealShortEntity>? = emptyList()
)
