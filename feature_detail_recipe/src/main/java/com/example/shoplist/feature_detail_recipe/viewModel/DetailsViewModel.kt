package com.example.shoplist.feature_detail_recipe.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.shoplist.core.models.LoadState
import com.example.shoplist.domain.models.DetailRecipeEntity
import com.example.shoplist.domain.models.Errors
import com.example.shoplist.domain.repos.LoadingDetailsRepo
import com.example.shoplist.domain.repos.SavingShoplistRepo
import com.example.shoplist.feature_detail_recipe.mapper.IngredientMapper
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

abstract class DetailsViewModel: ViewModel() {
    abstract val loadingState: LiveData<LoadState<DetailRecipeEntity>>
    abstract val savingState: LiveData<LoadState<Pair<String, String>>>

    abstract fun onViewCreated(mealId: Int)
    abstract fun onAddIngredient(ingredient: Pair<String, String>)
}

class DetailsViewModelImpl(
    private val loadingDetailsRepo: LoadingDetailsRepo,
    private val savingShoplistRepo: SavingShoplistRepo,
    private val mapper: IngredientMapper,
) : DetailsViewModel() {

    override val loadingState = MutableLiveData<LoadState<DetailRecipeEntity>>()
    override val savingState = MutableLiveData<LoadState<Pair<String, String>>>()

    private var recipeId: Int? = null
    private var recipeTitle: String? = null
    private val loadingScope = CoroutineScope(
        Dispatchers.IO + SupervisorJob() + CoroutineExceptionHandler { _, thr ->
            loadingState.postValue(LoadState.Error(Errors.LOAD_ERROR, thr.message))
        }
    )
    private val savingScope = CoroutineScope(
        Dispatchers.IO + SupervisorJob() + CoroutineExceptionHandler { _, thr ->
            savingState.postValue(LoadState.Error(Errors.SAVING_ERROR, thr.message))
        }
    )

    override fun onViewCreated(mealId: Int) {
        loadingState.value = LoadState.Loading
        getRecipe(mealId)
    }

    override fun onAddIngredient(ingredient: Pair<String, String>) {
        savingState.value = LoadState.Loading
        savingScope.launch {
            try {
                savingShoplistRepo.saveIngredient(
                    mapper(
                        ingredient = ingredient,
                        recipeId = recipeId ?: 0,
                        recipeTitle = recipeTitle ?: "",
                    )
                )
                withContext(Dispatchers.Main) {
                    savingState.value = LoadState.Success(ingredient)
                }
            } catch (thr: Throwable) {
                withContext(Dispatchers.Main) {
                    savingState.value = LoadState.Error(Errors.SAVING_ERROR, thr.message)
                }
            }
        }
    }

    private fun getRecipe(id: Int) {
        loadingScope.launch {
            try {
                val recipe = loadingDetailsRepo.getDetailsById(id)
                withContext(Dispatchers.Main) {
                    recipe?.let {
                        recipeId = id
                        recipeTitle = recipe.title
                        loadingState.value = LoadState.Success(recipe)
                    } ?: run {
                        loadingState.value = LoadState.Error(Errors.SERVER_ERROR, null)
                    }
                }
            } catch (thr: Throwable) {
                withContext(Dispatchers.Main) {
                    loadingState.value = LoadState.Error(Errors.SERVER_ERROR, thr.message)
                }
            }
        }
    }

    override fun onCleared() {
        loadingScope.coroutineContext.cancelChildren()
        savingScope.coroutineContext.cancelChildren()
        super.onCleared()
    }
}