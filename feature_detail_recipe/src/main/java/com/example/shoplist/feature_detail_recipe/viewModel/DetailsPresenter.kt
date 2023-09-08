package com.example.shoplist.feature_detail_recipe.viewModel

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
    private val loadingScope = CoroutineScope(
        Dispatchers.IO + SupervisorJob() + CoroutineExceptionHandler { _, thr ->
            viewState.showError(Errors.LOAD_ERROR, thr.message)
        }
    )
    private val savingScope = CoroutineScope(
        Dispatchers.IO + SupervisorJob() + CoroutineExceptionHandler { _, thr ->
            viewState.showError(Errors.SAVING_ERROR, thr.message)
        }
    )

    override fun onViewCreated(mealId: Int) {
        viewState.showLoading()
        getRecipe(mealId)
    }

    override fun onAddIngredient(ingredient: Pair<String, String>) {
        savingScope.launch {
            try {
                interactor.saveIngredient(
                    mapper(
                        ingredient = ingredient,
                        recipeId = recipeId ?: 0,
                        recipeTitle = recipeTitle ?: "",
                    )
                )
                withContext(Dispatchers.Main) {
                    viewState.showIngredientAdded(ingredient)
                }
            } catch (thr: Throwable) {
                withContext(Dispatchers.Main) {
                    viewState.showError(Errors.SAVING_ERROR, thr.message)
                }
            }
        }
    }

    private fun getRecipe(id: Int) {
        loadingScope.launch {
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

    override fun onDestroy() {
        loadingScope.coroutineContext.cancelChildren()
        savingScope.coroutineContext.cancelChildren()
        super.onDestroy()
    }
}