package com.example.shoplist.domain

import com.example.shoplist.data.AreasEntity
import com.example.shoplist.data.CategoriesEntity
import com.example.shoplist.data.Filters
import com.example.shoplist.data.MealsEntity

interface LoadingMealRepo {
    suspend fun getMealsByFilter(filter: Filters, value: String) : MealsEntity
    suspend fun getCategories() : CategoriesEntity
    suspend fun getAreas() : AreasEntity
    suspend fun getMealsBySearch(name: String) : MealsEntity
}