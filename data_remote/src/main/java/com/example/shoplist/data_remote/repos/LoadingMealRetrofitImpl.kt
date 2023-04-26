package com.example.shoplist.data_remote.repos

import com.example.shoplist.data_remote.api.MealRetrofitService
import com.example.shoplist.data_remote.mappers.AreasMapper
import com.example.shoplist.data_remote.mappers.CategoriesMapper
import com.example.shoplist.data_remote.mappers.MealsMapper
import com.example.shoplist.domain.models.AreasEntity
import com.example.shoplist.domain.models.CategoriesEntity
import com.example.shoplist.domain.models.Filters
import com.example.shoplist.domain.models.MealsEntity
import com.example.shoplist.domain.repos.LoadingMealRepo

class LoadingMealRetrofitImpl(
    private val service: MealRetrofitService,
    private val areasMapper: AreasMapper,
    private val categoriesMapper: CategoriesMapper,
    private val mealsMapper: MealsMapper,
) : LoadingMealRepo {
    override suspend fun getMealsByFilter(filter: Filters, value: String): MealsEntity {
        return when (filter) {
            Filters.CATEGORY -> service
                .getMealByFilterCategory(value)
                .await()
                .let(mealsMapper::toDomain)
            Filters.AREA -> service
                .getMealByFilterArea(value)
                .await()
                .let(mealsMapper::toDomain)
        }
    }

    override suspend fun getCategories(): CategoriesEntity {
        return service.getCategories()
            .await()
            .let(categoriesMapper::toDomain)
    }

    override suspend fun getAreas(): AreasEntity {
        return service.getAreas()
            .await()
            .let(areasMapper::toDomain)
    }

    override suspend fun getMealsBySearch(name: String): MealsEntity {
        return service.getMealBySearch(name)
            .await()
            .let(mealsMapper::toDomain)
    }
}