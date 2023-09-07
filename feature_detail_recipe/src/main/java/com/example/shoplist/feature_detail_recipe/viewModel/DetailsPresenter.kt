package com.example.shoplist.feature_detail_recipe.viewModel

import android.util.Log
import com.example.shoplist.domain.models.Errors
import com.example.shoplist.feature_detail_recipe.mapper.IngredientMapper
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailsPresenter(
    private val interactor: DetailsController.Interactor,
    private val mapper: IngredientMapper,
) : DetailsController.Presenter() {

    private var recipeId: Int? = null
    private var recipeTitle: String? = null
    private val scope = CoroutineScope(
        Dispatchers.Main + SupervisorJob() + CoroutineExceptionHandler { _, thr ->
            viewState.showError(Errors.LOAD_ERROR, thr.message)
        }
    )

    override fun onViewCreated(mealId: Int) {
        viewState.showLoading()
        getRecipe(mealId)
    }

    override fun onAddIngredient(ingredient: Pair<String, String>) {
        val entity = mapper(
            ingredient = ingredient,
            recipeId = recipeId ?: 0,
            recipeTitle = recipeTitle ?: "",
        )
        Log.d("@@@",
            "entity: name = ${entity.ingredientName}, measure = ${entity.ingredientMeasure.title}: ${entity.ingredientMeasure.amount}, recipe = ${entity.recipe.title}(${entity.recipe.id})"
        )
        scope.launch {
            withContext(Dispatchers.IO) {
                try {
                    interactor.saveIngredient(entity)
                    withContext(Dispatchers.Main) {
                        viewState.showIngredientAddedMsg(ingredient)
                    }
                } catch (thr: Throwable) {
                    withContext(Dispatchers.Main) {
                        viewState.showError(Errors.LOAD_ERROR,thr.message)
                    }
                }
            }
        }
    }

    private fun getRecipe(id: Int) {
        scope.launch {
            withContext(Dispatchers.IO) {
                try {
                    val recipe = interactor.getDetailsById(id)
                    withContext(Dispatchers.Main) {
                        recipe?.let {
                            recipeId = id
                            recipeTitle = recipe.title
                            viewState.showRecipe(recipe)
                        }
                            ?: viewState.showError(Errors.SERVER_ERROR, null)
                    }
                } catch (thr: Throwable) {
                    withContext(Dispatchers.Main) {
                        viewState.showError(Errors.SERVER_ERROR, thr.message)
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        scope.coroutineContext.cancelChildren()
        super.onDestroy()
    }
}