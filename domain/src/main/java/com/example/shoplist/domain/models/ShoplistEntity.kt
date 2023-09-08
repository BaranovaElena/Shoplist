package com.example.shoplist.domain.models

data class ShoplistEntity(
    val ingredientName: String,
    val ingredientMeasure: Measure,
    val recipe: Recipe,
) {

    data class Measure(
        val amount: Float,
        val title: String,
    )

    data class Recipe(
        val id: Int,
        val title: String,
    )
}
