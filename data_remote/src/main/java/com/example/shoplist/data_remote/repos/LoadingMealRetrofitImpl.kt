package com.example.shoplist.data_remote.repos

import com.example.shoplist.domain.models.AreasEntity
import com.example.shoplist.domain.models.CategoriesEntity
import com.example.shoplist.domain.models.Filters
import com.example.shoplist.domain.models.MealsEntity

class LoadingMealRetrofitImpl(private val service: com.example.shoplist.data_remote.api.MealRetrofitService) :
    com.example.shoplist.domain.repos.LoadingMealRepo {
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