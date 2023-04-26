package com.example.shoplist.domain.models

data class DetailRecipeEntity(
    val id: Int = 0,
    val title: String = "",
    val category: String = "",
    val area: String = "",
    val instructions: String = "",
    val imageUrl: String = "",
    val videoUrl: String = "",
    val ingredients: Map<String, String> = emptyMap()
)