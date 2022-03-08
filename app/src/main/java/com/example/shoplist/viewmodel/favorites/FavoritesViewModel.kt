package com.example.shoplist.viewmodel.favorites

import com.example.shoplist.data.Errors
import com.example.shoplist.data.LoadState
import com.example.shoplist.domain.favorites.LoadingFavoritesMealRepo
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect

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
