package com.example.shoplist

import android.app.Application
import com.example.shoplist.data_remote.di.retrofitModule
import com.example.shoplist.di.roomModule
import com.example.shoplist.di.viewModelModule
import com.example.shoplist.feature_detail_recipe.di.detailRecipeModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(applicationContext)
            modules(
                retrofitModule,
                viewModelModule,
                roomModule,
                detailRecipeModule,
            )
        }
    }
}