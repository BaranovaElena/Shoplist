package com.example.shoplist.viewmodel.recipe

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.shoplist.data.AreasEntity
import com.example.shoplist.data.CategoriesEntity
import com.example.shoplist.data.Filters
import com.example.shoplist.data.LoadState
import com.example.shoplist.data.MealsEntity

class RecipeFilterController {
    interface View {
        fun renderFilterLoadState(state: LoadState<Any>)
        fun renderMealsLoadState(state: LoadState<MealsEntity>)
    }

    abstract class BaseViewModel(
        protected var mealsLoadStateLiveDataMutable: MutableLiveData<LoadState<MealsEntity>> = MutableLiveData(),
        protected var categoriesLoadStateLiveDataMutable: MutableLiveData<LoadState<CategoriesEntity>> = MutableLiveData(),
        protected var areasLoadStateLiveDataMutable: MutableLiveData<LoadState<AreasEntity>> = MutableLiveData(),
        val mealsLoadStateLiveData: LiveData<LoadState<MealsEntity>> = mealsLoadStateLiveDataMutable,
        val categoriesLoadStateLiveData: LiveData<LoadState<CategoriesEntity>> = categoriesLoadStateLiveDataMutable,
        val areasLoadStateLiveData: LiveData<LoadState<AreasEntity>> = areasLoadStateLiveDataMutable
    ) : ViewModel() {
        abstract fun onChipChecked(filter: Filters)
        abstract fun onFilterValueSelected(filterValue: String)
    }
}