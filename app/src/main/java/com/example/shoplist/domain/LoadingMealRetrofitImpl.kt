package com.example.shoplist.domain

import com.example.shoplist.data.Filters
import com.example.shoplist.data.MealsEntity
import retrofit2.Callback

class LoadingMealRetrofitImpl(private val service: MealRetrofitService) : LoadingMealRepo {
    override fun getMealsByFilter(filter: Filters, value: String, callback: Callback<MealsEntity>) {
        when (filter) {
            Filters.CATEGORY -> service.getMealByFilterCategory(value).enqueue(callback)
            Filters.AREA -> service.getMealByFilterArea(value).enqueue(callback)
        }
    }
}