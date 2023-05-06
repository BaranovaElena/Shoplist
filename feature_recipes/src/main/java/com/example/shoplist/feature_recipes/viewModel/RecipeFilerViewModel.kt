package com.example.shoplist.feature_recipes.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.shoplist.domain.models.AreasEntity
import com.example.shoplist.domain.models.CategoriesEntity
import com.example.shoplist.domain.models.Errors
import com.example.shoplist.domain.models.Filters
import com.example.shoplist.domain.models.MealsEntity
import com.example.shoplist.domain.repos.LoadingMealRepo
import com.example.shoplist.feature_recipes.models.LoadState
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

abstract class RecipeFilerViewModel: ViewModel() {
    abstract val mealsLoadStateLiveData: LiveData<LoadState<MealsEntity>>
    abstract val categoriesLoadStateLiveData: LiveData<LoadState<CategoriesEntity>>
    abstract val areasLoadStateLiveData: LiveData<LoadState<AreasEntity>>

    abstract fun onChipChecked(filter: Filters)
    abstract fun onFilterValueSelected(filterValue: String)
}

class RecipeFilerViewModelImpl(
    private val repo: LoadingMealRepo,
): RecipeFilerViewModel() {

    override val mealsLoadStateLiveData = MutableLiveData<LoadState<MealsEntity>>()
    override val categoriesLoadStateLiveData = MutableLiveData<LoadState<CategoriesEntity>>()
    override val areasLoadStateLiveData = MutableLiveData<LoadState<AreasEntity>>()
    private var currentFilter: Filters? = null
    private val categoryScope =
        CoroutineScope(Dispatchers.Main + SupervisorJob() + CoroutineExceptionHandler { _, thr ->
            categoriesLoadStateLiveData.postValue(LoadState.Error(Errors.LOAD_ERROR, thr.message))
        })
    private val areaScope =
        CoroutineScope(Dispatchers.Main + SupervisorJob() + CoroutineExceptionHandler { _, thr ->
            areasLoadStateLiveData.postValue(LoadState.Error(Errors.LOAD_ERROR, thr.message))
        })
    private val mealScope =
        CoroutineScope(Dispatchers.Main + SupervisorJob() + CoroutineExceptionHandler { _, thr ->
            mealsLoadStateLiveData.postValue(LoadState.Error(Errors.LOAD_ERROR, thr.message))
    })

    override fun onChipChecked(filter: Filters) {
        if (currentFilter != filter) {
            currentFilter = filter
            getDataList(filter)
        }
    }

    override fun onFilterValueSelected(filterValue: String) {
        mealsLoadStateLiveData.postValue(LoadState.Loading)
        currentFilter?.let { getMeals(it, filterValue) }
    }

    private fun getDataList(filter: Filters) {
        val scope = when (filter) {
            Filters.CATEGORY -> {
                categoriesLoadStateLiveData.postValue(LoadState.Loading)
                categoryScope
            }
            Filters.AREA -> {
                areasLoadStateLiveData.postValue(LoadState.Loading)
                areaScope
            }
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
        areasLoadStateLiveData.postValue(
            try {
                LoadState.Success(repo.getAreas())
            } catch (thr: Throwable) {
                LoadState.Error(Errors.SERVER_ERROR, thr.message)
            }
        )
    }

    private suspend fun getCategories() {
        categoriesLoadStateLiveData.postValue(
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
                mealsLoadStateLiveData.postValue(
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