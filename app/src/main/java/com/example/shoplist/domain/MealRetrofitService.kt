package com.example.shoplist.domain;

import com.example.shoplist.data.MealsEntity
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

private const val FILTER_URL = "filter.php?"

interface MealRetrofitService {
    @GET(FILTER_URL)
    fun getMealByFilterCategory(@Query("c") category: String): Call<MealsEntity>
    @GET(FILTER_URL)
    fun getMealByFilterArea(@Query("a") area: String): Call<MealsEntity>
}
