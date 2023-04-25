package com.example.shoplist.viewmodel.favorites

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.shoplist.data.LoadState
import com.example.shoplist.domain.models.MealShortEntity

class FavoritesController {
    interface View {
        fun renderMealsList(state: LoadState<List<MealShortEntity>>)
    }

    abstract class BaseViewModel(
        protected var loadStateLiveDataMutable: MutableLiveData<LoadState<List<MealShortEntity>>> = MutableLiveData(),
        val loadStateLiveData: LiveData<LoadState<List<MealShortEntity>>> = loadStateLiveDataMutable,
    ) : ViewModel() {
        abstract fun onViewCreated()
    }
}