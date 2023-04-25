package com.example.shoplist.feature_detail_recipe.viewModel

import com.example.shoplist.domain.models.DetailRecipeEntity
import com.example.shoplist.domain.models.DetailRecipesEntity
import com.example.shoplist.domain.repos.LoadingDetailsRepo

class DetailsInteractor(private val repo: LoadingDetailsRepo) : DetailsController.Interactor {
    override suspend fun getDetailsById(id: Int): DetailRecipeEntity? {
        val body = repo.getDetailsById(id)
        if (body.recipe.isEmpty())
            return null

        return DetailRecipeEntity(
            body.recipe[0].id,
            body.recipe[0].title,
            body.recipe[0].category,
            body.recipe[0].area,
            body.recipe[0].instructions,
            body.recipe[0].imageUrl,
            body.recipe[0].videoUrl,
            fillIngridients(body)
        )
    }

    private fun fillIngridients(body: DetailRecipesEntity) : Map<String, String> {
        val ingredients: MutableMap<String, String> = mutableMapOf()

        if (body.recipe[0].ingredient1 != "") {
            ingredients[body.recipe[0].ingredient1] = body.recipe[0].measure1
        } else return ingredients
        if (body.recipe[0].ingredient2 != "") {
            ingredients[body.recipe[0].ingredient2] = body.recipe[0].measure2
        } else return ingredients
        if (body.recipe[0].ingredient3 != "") {
            ingredients[body.recipe[0].ingredient3] = body.recipe[0].measure3
        } else return ingredients
        if (body.recipe[0].ingredient4 != "") {
            ingredients[body.recipe[0].ingredient4] = body.recipe[0].measure4
        } else return ingredients
        if (body.recipe[0].ingredient5 != "") {
            ingredients[body.recipe[0].ingredient5] = body.recipe[0].measure5
        } else return ingredients
        if (body.recipe[0].ingredient6 != "") {
            ingredients[body.recipe[0].ingredient6] = body.recipe[0].measure6
        } else return ingredients
        if (body.recipe[0].ingredient7 != "") {
            ingredients[body.recipe[0].ingredient7] = body.recipe[0].measure7
        } else return ingredients
        if (body.recipe[0].ingredient8 != "") {
            ingredients[body.recipe[0].ingredient8] = body.recipe[0].measure8
        } else return ingredients
        if (body.recipe[0].ingredient9 != "") {
            ingredients[body.recipe[0].ingredient9] = body.recipe[0].measure9
        } else return ingredients
        if (body.recipe[0].ingredient10 != "") {
            ingredients[body.recipe[0].ingredient10] = body.recipe[0].measure10
        } else return ingredients
        if (body.recipe[0].ingredient11 != "") {
            ingredients[body.recipe[0].ingredient11] = body.recipe[0].measure11
        } else return ingredients
        if (body.recipe[0].ingredient12 != "") {
            ingredients[body.recipe[0].ingredient12] = body.recipe[0].measure12
        } else return ingredients
        if (body.recipe[0].ingredient13 != "") {
            ingredients[body.recipe[0].ingredient13] = body.recipe[0].measure13
        } else return ingredients
        if (body.recipe[0].ingredient14 != "") {
            ingredients[body.recipe[0].ingredient14] = body.recipe[0].measure14
        } else return ingredients
        if (body.recipe[0].ingredient15 != "") {
            ingredients[body.recipe[0].ingredient15] = body.recipe[0].measure15
        } else return ingredients
        if (body.recipe[0].ingredient16 != "") {
            ingredients[body.recipe[0].ingredient16] = body.recipe[0].measure16
        } else return ingredients
        if (body.recipe[0].ingredient17 != "") {
            ingredients[body.recipe[0].ingredient17] = body.recipe[0].measure17
        } else return ingredients
        if (body.recipe[0].ingredient18 != "") {
            ingredients[body.recipe[0].ingredient18] = body.recipe[0].measure18
        } else return ingredients
        if (body.recipe[0].ingredient19 != "") {
            ingredients[body.recipe[0].ingredient19] = body.recipe[0].measure19
        } else return ingredients
        if (body.recipe[0].ingredient20 != "") {
            ingredients[body.recipe[0].ingredient20] = body.recipe[0].measure20
        } else return ingredients

        return ingredients
    }
}