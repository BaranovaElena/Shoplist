package com.example.shoplist.feature_shoplist.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.shoplist.domain.models.Errors
import com.example.shoplist.domain.models.ShoplistEntity
import com.example.shoplist.domain.repos.LoadingShoplistRepo
import com.example.shoplist.feature_shoplist.models.LoadState
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

abstract class ShoplistViewModel : ViewModel() {
    abstract val loadingLiveData: LiveData<LoadState<List<ShoplistEntity>>>

    abstract fun onViewCreated()
    abstract fun onItemChecked(ingredientName: String)
    abstract fun onItemUnchecked(ingredientName: String)
}

class ShoplistViewModelImpl(
    private val loadingRepo: LoadingShoplistRepo,
) : ShoplistViewModel() {
    override val loadingLiveData = MutableLiveData<LoadState<List<ShoplistEntity>>>()

    private val loadingScope = CoroutineScope(
        Dispatchers.IO + SupervisorJob() + CoroutineExceptionHandler { _, thr ->
            loadingLiveData.postValue(LoadState.Error(Errors.LOAD_ERROR, thr.message))
        }
    )

    override fun onViewCreated() {
        loadingLiveData.value = LoadState.Loading
        loadIngredients()
    }

    override fun onItemChecked(ingredientName: String) {
        TODO("Not yet implemented")
    }

    override fun onItemUnchecked(ingredientName: String) {
        TODO("Not yet implemented")
    }

    private fun loadIngredients() = loadingScope.launch {
        try {
            loadingRepo.loadShoplist().collect {
                withContext(Dispatchers.Main) {
                    loadingLiveData.postValue(LoadState.Success(it))
                }
            }
        } catch (ex: Exception) {
            withContext(Dispatchers.Main) {
                loadingLiveData.postValue(LoadState.Error(Errors.LOAD_ERROR, ex.message))
            }
        }
    }

    override fun onCleared() {
        loadingScope.coroutineContext.cancelChildren()
        super.onCleared()
    }
}