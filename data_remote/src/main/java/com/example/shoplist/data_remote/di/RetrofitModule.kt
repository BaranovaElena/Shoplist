package com.example.shoplist.data_remote.di

import com.example.shoplist.data_remote.api.MealRetrofitService
import com.example.shoplist.data_remote.repos.LoadingDetailsRetrofitRepo
import com.example.shoplist.data_remote.repos.LoadingMealRetrofitImpl
import com.example.shoplist.domain.repos.LoadingDetailsRepo
import com.example.shoplist.domain.repos.LoadingMealRepo
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val BASE_URL = "https://www.themealdb.com/api/json/v1/1/"

val retrofitModule = module {
    single<Retrofit> {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()
    }
    single<MealRetrofitService> { get<Retrofit>().create(MealRetrofitService::class.java) }
    single<LoadingMealRepo> { LoadingMealRetrofitImpl(get<MealRetrofitService>()) }
    single<LoadingDetailsRepo> {LoadingDetailsRetrofitRepo(get<MealRetrofitService>()) }
}