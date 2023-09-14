package com.example.shoplist.feature_favorites.viewModel

import com.example.shoplist.core.models.LoadState
import com.example.shoplist.domain.models.Errors
import com.example.shoplist.domain.repos.LoadingFavoritesMealRepo
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FavoritesViewModel(private val repo: LoadingFavoritesMealRepo) : FavoritesController.BaseViewModel() {
    private val scope =
        CoroutineScope(Dispatchers.Main + SupervisorJob() + CoroutineExceptionHandler { _, thr ->
            loadStateLiveDataMutable.postValue(LoadState.Error(Errors.LOAD_ERROR, thr.message))
        })

    override fun onViewCreated() {
        loadStateLiveDataMutable.postValue(LoadState.Loading)
        getMealsList()
    }

    private fun getMealsList() {
        scope.launch {
            withContext(Dispatchers.IO) {
                try {
                    repo.loadMeals().collect { meals ->
                        loadStateLiveDataMutable.postValue(LoadState.Success(meals))
                    }
                } catch (thr: Throwable) {
                    loadStateLiveDataMutable.postValue(LoadState.Error(Errors.LOAD_ERROR,thr.message))
                }
            }
        }
    }

    override fun onCleared() {
        scope.coroutineContext.cancelChildren()
        super.onCleared()
    }
}
