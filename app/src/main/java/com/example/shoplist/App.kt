package com.example.shoplist

import android.app.Application
import com.example.shoplist.data_local.di.roomModule
import com.example.shoplist.data_remote.di.retrofitModule
import com.example.shoplist.feature_detail_recipe.di.detailRecipeModule
import com.example.shoplist.feature_favorites.di.favoritesModule
import com.example.shoplist.feature_meal_item.di.mealItemModule
import com.example.shoplist.feature_recipes.di.recipesModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(applicationContext)
            modules(
                retrofitModule,
                roomModule,
                detailRecipeModule,
                favoritesModule,
                mealItemModule,
                recipesModule,
            )
        }
    }
}