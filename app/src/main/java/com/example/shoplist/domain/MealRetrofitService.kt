package com.example.shoplist.domain

import com.example.shoplist.data.AreasEntity
import com.example.shoplist.data.CategoriesEntity
import com.example.shoplist.data.MealsEntity
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

private const val FILTER_URL = "filter.php?"
private const val CATEGORIES_URL = "list.php?c=list"
private const val AREAS_URL = "list.php?a=list"

interface MealRetrofitService {
    @GET(FILTER_URL)
    fun getMealByFilterCategory(@Query("c") category: String): Call<MealsEntity>
    @GET(FILTER_URL)
    fun getMealByFilterArea(@Query("a") area: String): Call<MealsEntity>
    @GET(CATEGORIES_URL)
    fun getCategories(): Call<CategoriesEntity>
    @GET(AREAS_URL)
    fun getAreas(): Call<AreasEntity>
}
