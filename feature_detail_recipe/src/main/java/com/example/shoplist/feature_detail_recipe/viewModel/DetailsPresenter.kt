package com.example.shoplist.feature_detail_recipe.viewModel

import com.example.shoplist.domain.models.Errors
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailsPresenter(
    private val interactor: DetailsController.Interactor
) : DetailsController.Presenter() {

    private val scope = CoroutineScope(
        Dispatchers.Main + SupervisorJob() + CoroutineExceptionHandler { _, thr ->
            viewState.showError(Errors.LOAD_ERROR, thr.message)
        }
    )

    override fun onViewCreated(mealId: Int) {
        viewState.showLoading()
        getRecipe(mealId)
    }

    private fun getRecipe(id: Int) {
        scope.launch {
            withContext(Dispatchers.IO) {
                try {
                    val recipe = interactor.getDetailsById(id)
                    withContext(Dispatchers.Main) {
                        recipe?.let { viewState.showRecipe(recipe) }
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