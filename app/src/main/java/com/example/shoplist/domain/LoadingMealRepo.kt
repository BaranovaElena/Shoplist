package com.example.shoplist.domain

import com.example.shoplist.data.AreasEntity
import com.example.shoplist.data.CategoriesEntity
import com.example.shoplist.data.Filters
import com.example.shoplist.data.MealsEntity
import retrofit2.Callback

interface LoadingMealRepo {
    fun getMealsByFilter(filter: Filters, value: String, callback: Callback<MealsEntity>)
    fun getCategories(callback: Callback<CategoriesEntity>)
    fun getAreas(callback: Callback<AreasEntity>)
}