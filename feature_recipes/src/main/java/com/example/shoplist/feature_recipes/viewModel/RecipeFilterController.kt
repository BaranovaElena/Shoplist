package com.example.shoplist.feature_recipes.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.shoplist.domain.models.AreasEntity
import com.example.shoplist.domain.models.CategoriesEntity
import com.example.shoplist.domain.models.Filters
import com.example.shoplist.domain.models.MealsEntity
import com.example.shoplist.feature_recipes.models.LoadState

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