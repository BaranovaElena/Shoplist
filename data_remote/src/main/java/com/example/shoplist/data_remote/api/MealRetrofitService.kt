package com.example.shoplist.data_remote.api

import com.example.shoplist.data_remote.models.AreasEntityResponse
import com.example.shoplist.data_remote.models.CategoriesEntityResponse
import com.example.shoplist.data_remote.models.DetailRecipesEntityResponse
import com.example.shoplist.data_remote.models.MealsEntityResponse
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Query

private const val FILTER_URL = "filter.php?"
private const val CATEGORIES_URL = "list.php?c=list"
private const val AREAS_URL = "list.php?a=list"
private const val DETAILS_URL = "lookup.php?"
private const val SEARCH_URL = "search.php?"

interface MealRetrofitService {
    @GET(FILTER_URL)
    fun getMealByFilterCategory(@Query("c") category: String): Deferred<MealsEntityResponse>
    @GET(FILTER_URL)
    fun getMealByFilterArea(@Query("a") area: String): Deferred<MealsEntityResponse>
    @GET(CATEGORIES_URL)
    fun getCategories(): Deferred<CategoriesEntityResponse>
    @GET(AREAS_URL)
    fun getAreas(): Deferred<AreasEntityResponse>
    @GET(DETAILS_URL)
    fun getDetails(@Query("i") id: Int): Deferred<DetailRecipesEntityResponse>
    @GET(SEARCH_URL)
    fun getMealBySearch(@Query("s") name: String): Deferred<MealsEntityResponse>
}