package com.example.shoplist.data_remote.mappers

import com.example.shoplist.data_remote.models.DetailRecipeEntityResponse
import com.example.shoplist.data_remote.models.DetailRecipesEntityResponse
import com.example.shoplist.domain.models.DetailRecipeEntity
import kotlin.reflect.full.memberProperties

class DetailRecipeMapper {

    fun toDomain(response: DetailRecipesEntityResponse): DetailRecipeEntity? {
        return when (response.recipe.isEmpty()) {
            true -> null
            else -> response.recipe.first().let { recipe ->
                DetailRecipeEntity(
                    recipe.id,
                    recipe.title,
                    recipe.category,
                    recipe.area,
                    recipe.instructions,
                    recipe.imageUrl,
                    recipe.videoUrl,
                    fillIngredients(recipe)
                )
            }
        }
    }

    private fun fillIngredients(recipe: DetailRecipeEntityResponse): Map<String, String> {
        val ingredients: MutableMap<String, String> = mutableMapOf()

        DetailRecipeEntityResponse::class.memberProperties.forEach { prop ->
            if (prop.name.contains("ingredient") &&
                !(prop.get(recipe) as? String).isNullOrEmpty()
            ) {
                (prop.get(recipe) as? String)?.let { key ->
                    ingredients[key] =
                        DetailRecipeEntityResponse::class.memberProperties.find {
                            it.name == prop.name.replace(
                                oldValue = "ingredient",
                                newValue = "measure",
                                ignoreCase = true
                            )
                        }
                            ?.get(recipe) as? String ?: ""
                }
            }
        }

        return ingredients
    }
}
