package com.example.shoplist

import android.app.Application
import com.example.shoplist.domain.MealRetrofitService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class App : Application() {
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://www.themealdb.com/api/json/v1/1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    val retrofitService: MealRetrofitService = retrofit.create(MealRetrofitService::class.java)
}