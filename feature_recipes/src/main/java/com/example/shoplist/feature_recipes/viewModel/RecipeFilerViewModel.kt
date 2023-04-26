package com.example.shoplist.feature_recipes.viewModel

import com.example.shoplist.domain.models.Errors
import com.example.shoplist.domain.models.Filters
import com.example.shoplist.domain.repos.LoadingMealRepo
import com.example.shoplist.feature_recipes.models.LoadState
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RecipeFilerViewModel(private val repo: LoadingMealRepo): RecipeFilterController.BaseViewModel() {
    private var currentFilter: Filters? = null
    private val categoryScope =
        CoroutineScope(Dispatchers.Main + SupervisorJob() + CoroutineExceptionHandler { _, thr ->
            categoriesLoadStateLiveDataMutable.postValue(LoadState.Error(Errors.LOAD_ERROR, thr.message))
        })
    private val areaScope =
        CoroutineScope(Dispatchers.Main + SupervisorJob() + CoroutineExceptionHandler { _, thr ->
            areasLoadStateLiveDataMutable.postValue(LoadState.Error(Errors.LOAD_ERROR, thr.message))
        })
    private val mealScope =
        CoroutineScope(Dispatchers.Main + SupervisorJob() + CoroutineExceptionHandler { _, thr ->
            mealsLoadStateLiveDataMutable.postValue(LoadState.Error(Errors.LOAD_ERROR, thr.message))
    })

    override fun onChipChecked(filter: Filters) {
        if (currentFilter != filter) {
            currentFilter = filter
            getDataList(filter)
        }
    }

    override fun onFilterValueSelected(filterValue: String) {
        mealsLoadStateLiveDataMutable.postValue(LoadState.Loading)
        currentFilter?.let { getMeals(it, filterValue) }
    }

    private fun getDataList(filter: Filters) {
        val scope = when (filter) {
            Filters.CATEGORY -> categoryScope
            Filters.AREA -> areaScope
        }
        scope.launch {
            withContext(Dispatchers.IO) {
                when (filter) {
                    Filters.CATEGORY -> getCategories()
                    Filters.AREA -> getAreas()
                }
            }
        }
    }

    private suspend fun getAreas() {
        areasLoadStateLiveDataMutable.postValue(
            try {
                LoadState.Success(repo.getAreas())
            } catch (thr: Throwable) {
                LoadState.Error(Errors.SERVER_ERROR, thr.message)
            }
        )
    }

    private suspend fun getCategories() {
        categoriesLoadStateLiveDataMutable.postValue(
            try {
                LoadState.Success(repo.getCategories())
            } catch (thr: Throwable) {
                LoadState.Error(Errors.SERVER_ERROR, thr.message)
            }
        )
    }

    private fun getMeals(filter: Filters, filterValue: String) {
        mealScope.launch {
            withContext(Dispatchers.IO) {
                mealsLoadStateLiveDataMutable.postValue(
                    try {
                        LoadState.Success(repo.getMealsByFilter(filter, filterValue))
                    } catch (thr: Throwable) {
                        LoadState.Error(Errors.SERVER_ERROR, thr.message)
                    }
                )
            }
        }
    }

    override fun onCleared() {
        categoryScope.coroutineContext.cancelChildren()
        areaScope.coroutineContext.cancelChildren()
        mealScope.coroutineContext.cancelChildren()
        super.onCleared()
    }
}