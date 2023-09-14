package com.example.shoplist.data_local.mappers

import com.example.shoplist.data_local.models.IngredientEntityResponse
import com.example.shoplist.domain.models.ShoplistEntity

class IngredientMapper {

    fun toDomain(response: IngredientEntityResponse) = ShoplistEntity(
        ingredientName = response.title,
        ingredientMeasure = ShoplistEntity.Measure(
            amount = response.measureAmount,
            title = response.measureTitle,
        ),
        recipe = ShoplistEntity.Recipe(
            id = response.recipeId,
            title = response.recipeTitle,
        ),
        isChecked = response.isChecked,
    )

    fun toApi(
        domain: ShoplistEntity,
        newAmount: Float? = null,
    ) = IngredientEntityResponse(
        title = domain.ingredientName,
        measureTitle = domain.ingredientMeasure.title,
        measureAmount = newAmount ?: domain.ingredientMeasure.amount,
        recipeId = domain.recipe.id,
        recipeTitle = domain.recipe.title,
        isChecked = domain.isChecked,
    )
}