package com.example.shoplist.domain.repos

import com.example.shoplist.domain.models.AreasEntity
import com.example.shoplist.domain.models.CategoriesEntity
import com.example.shoplist.domain.models.Filters
import com.example.shoplist.domain.models.MealsEntity

interface LoadingMealRepo {
    suspend fun getMealsByFilter(filter: Filters, value: String) : MealsEntity
    suspend fun getCategories() : CategoriesEntity
    suspend fun getAreas() : AreasEntity
    suspend fun getMealsBySearch(name: String) : MealsEntity
}