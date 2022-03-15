package com.example.shoplist.domain

import com.example.shoplist.data.AreasEntity
import com.example.shoplist.data.CategoriesEntity
import com.example.shoplist.data.DetailRecipesEntity
import com.example.shoplist.data.MealsEntity
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Query

private const val FILTER_URL = "filter.php?"
private const val CATEGORIES_URL = "list.php?c=list"
private const val AREAS_URL = "list.php?a=list"
private const val DETAILS_URL = "lookup.php?"

interface MealRetrofitService {
    @GET(FILTER_URL)
    fun getMealByFilterCategory(@Query("c") category: String): Deferred<MealsEntity>
    @GET(FILTER_URL)
    fun getMealByFilterArea(@Query("a") area: String): Deferred<MealsEntity>
    @GET(CATEGORIES_URL)
    fun getCategories(): Deferred<CategoriesEntity>
    @GET(AREAS_URL)
    fun getAreas(): Deferred<AreasEntity>
    @GET(DETAILS_URL)
    fun getDetails(@Query("i") id: Int): Deferred<DetailRecipesEntity>
}
