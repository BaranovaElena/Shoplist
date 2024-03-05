package com.example.shoplist.feature_detail_recipe.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.shoplist.core.models.LoadState
import com.example.shoplist.domain.models.DetailRecipeEntity
import com.example.shoplist.domain.models.Errors
import com.example.shoplist.domain.models.MealShortEntity
import com.example.shoplist.domain.repos.LoadingDetailsRepo
import com.example.shoplist.domain.repos.LoadingFavoritesMealRepo
import com.example.shoplist.domain.repos.SavingFavoriteMealRepo
import com.example.shoplist.domain.repos.SavingShoplistRepo
import com.example.shoplist.feature_detail_recipe.mapper.IngredientMapper
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

abstract class DetailsViewModel : ViewModel() {
    abstract val loadingState: LiveData<LoadState<Pair<DetailRecipeEntity, Boolean>>>
    abstract val savingState: LiveData<LoadState<Pair<String, String>>>

    abstract fun onViewCreated(mealId: Int)
    abstract fun onAddIngredient(ingredient: Pair<String, String>)
    abstract fun onLikeClicked(isSelected: Boolean)
}

class DetailsViewModelImpl(
    private val loadingDetailsRepo: LoadingDetailsRepo,
    private val savingShoplistRepo: SavingShoplistRepo,
    private val savingFavoriteRepo: SavingFavoriteMealRepo,
    private val gettingFavoriteRepo: LoadingFavoritesMealRepo,
    private val mapper: IngredientMapper,
) : DetailsViewModel() {

    override val loadingState = MutableLiveData<LoadState<Pair<DetailRecipeEntity, Boolean>>>()
    override val savingState = MutableLiveData<LoadState<Pair<String, String>>>()

    private var recipe: DetailRecipeEntity? = null

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
                        recipeId = recipe?.id ?: 0,
                        recipeTitle = recipe?.title ?: "",
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

    override fun onLikeClicked(isSelected: Boolean) {
        val meal = recipe?.let {
            MealShortEntity(it.title, it.imageUrl, it.id)
        } ?: MealShortEntity()

        savingScope.launch {
            if (isSelected)
                savingFavoriteRepo.saveMeal(meal)
            else
                savingFavoriteRepo.deleteMeal(meal)
        }
    }

    private fun getRecipe(id: Int) {
        loadingScope.launch {
            try {
                recipe = loadingDetailsRepo.getDetailsById(id)
                recipe?.let {
                    gettingFavoriteRepo.isMealExists(it.id).collect { isFavorite ->
                        withContext(Dispatchers.Main) {
                            loadingState.value = LoadState.Success(Pair(it, isFavorite))
                        }
                    }
                } ?: run {
                    withContext(Dispatchers.Main) {
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