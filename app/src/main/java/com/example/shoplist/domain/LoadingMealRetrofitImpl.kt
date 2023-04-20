package com.example.shoplist.domain

import com.example.shoplist.data.AreasEntity
import com.example.shoplist.data.CategoriesEntity
import com.example.shoplist.data.Filters
import com.example.shoplist.data.MealsEntity

class LoadingMealRetrofitImpl(private val service: MealRetrofitService) : LoadingMealRepo {
    override suspend fun getMealsByFilter(filter: Filters, value: String): MealsEntity {
        return when (filter) {
            Filters.CATEGORY -> service.getMealByFilterCategory(value).await()
            Filters.AREA -> service.getMealByFilterArea(value).await()
        }
    }

    override suspend fun getCategories(): CategoriesEntity {
        return service.getCategories().await()
    }

    override suspend fun getAreas(): AreasEntity {
        return service.getAreas().await()
    }

    override suspend fun getMealsBySearch(name: String): MealsEntity {
        return service.getMealBySearch(name).await()
    }
}