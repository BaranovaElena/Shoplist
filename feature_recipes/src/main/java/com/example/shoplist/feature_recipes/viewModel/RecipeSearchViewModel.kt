package com.example.shoplist.feature_recipes.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.shoplist.domain.models.Errors
import com.example.shoplist.domain.models.MealsEntity
import com.example.shoplist.feature_recipes.models.LoadState
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

abstract class RecipeSearchViewModel : ViewModel() {
    abstract val loadState: LiveData<LoadState<MealsEntity>>

    abstract fun onSearchClicked(name: String)
}

class RecipeSearchViewModelImpl(
    private val repo: com.example.shoplist.domain.repos.LoadingMealRepo,
) : RecipeSearchViewModel() {

    override val loadState = MutableLiveData<LoadState<MealsEntity>>()
    private val mealScope = CoroutineScope(
        Dispatchers.Main + SupervisorJob() + CoroutineExceptionHandler { _, thr ->
            loadState.postValue(LoadState.Error(Errors.LOAD_ERROR, thr.message))
        }
    )

    override fun onSearchClicked(name: String) {
        if (name.isNotEmpty()) {
            mealScope.launch {
                withContext(Dispatchers.IO) {
                    loadState.postValue(
                        try {
                            LoadState.Success(repo.getMealsBySearch(name))
                        } catch (thr: Throwable) {
                            LoadState.Error(Errors.SERVER_ERROR, thr.message)
                        }
                    )
                }
            }
        }
    }

    override fun onCleared() {
        mealScope.coroutineContext.cancelChildren()
        super.onCleared()
    }
}
