package com.example.shoplist.feature_shoplist.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.shoplist.core.models.LoadState
import com.example.shoplist.domain.models.Errors
import com.example.shoplist.domain.models.ShoplistEntity
import com.example.shoplist.domain.repos.LoadingShoplistRepo
import com.example.shoplist.domain.repos.SavingShoplistRepo
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

abstract class ShoplistViewModel : ViewModel() {
    abstract val loadingLiveData: LiveData<LoadState<List<ShoplistEntity>>>
    abstract val updatingLiveData: LiveData<LoadState<ShoplistEntity>>

    abstract fun onViewCreated()
    abstract fun onItemChecked(ingredientName: String)
    abstract fun onItemUnchecked(ingredientName: String)
    abstract fun onSelectAllClicked()
}

class ShoplistViewModelImpl(
    private val loadingRepo: LoadingShoplistRepo,
    private val savingRepo: SavingShoplistRepo,
) : ShoplistViewModel() {

    override val loadingLiveData = MutableLiveData<LoadState<List<ShoplistEntity>>>()
    override val updatingLiveData = MutableLiveData<LoadState<ShoplistEntity>>()
    private val list: MutableList<ShoplistEntity> = mutableListOf()

    private val loadingScope = CoroutineScope(
        Dispatchers.IO + SupervisorJob() + CoroutineExceptionHandler { _, thr ->
            loadingLiveData.postValue(LoadState.Error(Errors.LOAD_ERROR, thr.message))
        }
    )
    private val savingScope = CoroutineScope(
        Dispatchers.IO + SupervisorJob() + CoroutineExceptionHandler { _, thr ->
            updatingLiveData.postValue(LoadState.Error(Errors.SAVING_ERROR, thr.message))
        }
    )

    override fun onViewCreated() {
        loadingLiveData.value = LoadState.Loading
        loadIngredients()
    }

    override fun onItemChecked(ingredientName: String) =
        saveModifiedIngredient(ingredientName = ingredientName, isChecked = true)

    override fun onItemUnchecked(ingredientName: String) =
        saveModifiedIngredient(ingredientName = ingredientName, isChecked = false)

    override fun onSelectAllClicked() {
        list.forEach { entity ->
            if (!entity.isChecked) {
                saveModifiedIngredient(ingredientName = entity.ingredientName, isChecked = true)
            }
        }
    }

    private fun loadIngredients() {
        loadingScope.launch {
            try {
                loadingRepo.loadShoplist().collect { ingredients ->
                    list.clear()
                    list.addAll(ingredients)
                    withContext(Dispatchers.Main) {
                        loadingLiveData.value = LoadState.Success(ingredients)
                    }
                }
            } catch (ex: Exception) {
                withContext(Dispatchers.Main) {
                    loadingLiveData.value = LoadState.Error(Errors.LOAD_ERROR, ex.message)
                }
            }
        }
    }

    private fun saveModifiedIngredient(ingredientName: String, isChecked: Boolean) {
        updatingLiveData.value = LoadState.Loading
        val oldIngredient = list.firstOrNull { it.ingredientName == ingredientName }

        oldIngredient?.let {
            val newIngredient = oldIngredient.copy(isChecked = isChecked)
            savingScope.launch {
                try {
                    savingRepo.updateIngredient(newIngredient)
                    list.set(index = list.indexOf(oldIngredient), element = newIngredient)
                    withContext(Dispatchers.Main) {
                        updatingLiveData.value = LoadState.Success(newIngredient)
                    }
                } catch (ex: Exception) {
                    withContext(Dispatchers.Main) {
                        updatingLiveData.value = LoadState.Error(Errors.SAVING_ERROR, ex.message)
                    }
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