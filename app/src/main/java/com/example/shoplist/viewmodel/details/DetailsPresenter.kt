package com.example.shoplist.viewmodel.details

import com.example.shoplist.data.Errors
import kotlinx.coroutines.*

class DetailsPresenter(
    private val interactor: DetailsController.Interactor
    ) : DetailsController.Presenter() {
    private val scope =
        CoroutineScope(Dispatchers.Main + SupervisorJob() + CoroutineExceptionHandler { _, thr ->
            viewState.showError(Errors.LOAD_ERROR, thr.message)
        })

    override fun onViewCreated(mealId: Int) {
        viewState.showLoading()
        getRecipe(mealId)
    }

    private fun getRecipe(id: Int) {
        scope.launch {
            withContext(Dispatchers.IO) {
                try {
                    val recipe = interactor.getDetailsById(id)
                    recipe?.let {
                        withContext(Dispatchers.Main) {
                            viewState.showRecipe(recipe)
                        }

                    } ?: viewState.showError(Errors.SERVER_ERROR, null)
                } catch (thr: Throwable) {
                    viewState.showError(Errors.SERVER_ERROR, thr.message)
                }
            }
        }
    }

    override fun onDestroy() {
        scope.coroutineContext.cancelChildren()
        super.onDestroy()
    }
}