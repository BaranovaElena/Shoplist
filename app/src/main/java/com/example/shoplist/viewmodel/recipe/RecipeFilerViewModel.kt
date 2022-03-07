package com.example.shoplist.viewmodel.recipe

import com.example.shoplist.data.*
import com.example.shoplist.domain.LoadingMealRepo
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RecipeFilerViewModel(private val repo: LoadingMealRepo): RecipeFilterController.BaseViewModel() {
    private var currentFilter: Filters? = null

    override fun onChipChecked(filter: Filters) {
        currentFilter = filter
        when (filter) {
            Filters.CATEGORY -> {
                categoriesLoadStateLiveDataMutable.postValue(LoadState.Loading)
                getCategoriesList()
            }
            Filters.AREA -> {
                areasLoadStateLiveDataMutable.postValue(LoadState.Loading)
                getAreasList()
            }
        }
    }

    private fun getAreasList() {
        val callback = object : Callback<AreasEntity> {
            override fun onResponse(call: Call<AreasEntity>, response: Response<AreasEntity>) {
                val body = response.body() ?: AreasEntity()
                areasLoadStateLiveDataMutable.postValue(
                    if (response.isSuccessful) {
                        LoadState.Success(body)
                    } else {
                        LoadState.Error(Errors.SERVER_ERROR, null)
                    }
                )
            }

            override fun onFailure(call: Call<AreasEntity>, t: Throwable) {
                areasLoadStateLiveDataMutable.postValue(LoadState.Error(Errors.LOAD_ERROR, t.message))
            }
        }
        repo.getAreas(callback)
    }

    private fun getCategoriesList() {
        val callback = object : Callback<CategoriesEntity> {
            override fun onResponse(call: Call<CategoriesEntity>, response: Response<CategoriesEntity>) {
                val body = response.body() ?: CategoriesEntity()
                categoriesLoadStateLiveDataMutable.postValue(
                    if (response.isSuccessful) {
                        LoadState.Success(body)
                    } else {
                        LoadState.Error(Errors.SERVER_ERROR, null)
                    }
                )
            }

            override fun onFailure(call: Call<CategoriesEntity>, t: Throwable) {
                categoriesLoadStateLiveDataMutable.postValue(LoadState.Error(Errors.LOAD_ERROR, t.message))
            }
        }
        repo.getCategories(callback)
    }

    override fun onFilterValueSelected(filterValue: String) {
        mealsLoadStateLiveDataMutable.postValue(LoadState.Loading)
        getMeals(filterValue)
    }

    private fun getMeals(filterValue: String) {
        val callback = object : Callback<MealsEntity> {
            override fun onResponse(call: Call<MealsEntity>, response: Response<MealsEntity>) {
                val body = response.body() ?: MealsEntity()
                mealsLoadStateLiveDataMutable.postValue(
                    if (response.isSuccessful) {
                        LoadState.Success(body)
                    } else {
                        LoadState.Error(Errors.SERVER_ERROR, null)
                    }
                )
            }

            override fun onFailure(call: Call<MealsEntity>, t: Throwable) {
                mealsLoadStateLiveDataMutable.postValue(LoadState.Error(Errors.LOAD_ERROR, t.message))
            }
        }

        currentFilter?.let {
            repo.getMealsByFilter(it, filterValue, callback)
        }
    }
}