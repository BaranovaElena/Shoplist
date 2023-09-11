package com.example.shoplist.domain.models

data class ShoplistEntity(
    val ingredientName: String,
    val ingredientMeasure: Measure,
    val recipe: Recipe,
    //val isChecked: Boolean,
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
