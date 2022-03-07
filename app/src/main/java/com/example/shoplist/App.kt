package com.example.shoplist

import android.app.Application
import com.example.shoplist.di.retrofitModule
import com.example.shoplist.di.viewModelModule
import org.koin.core.context.startKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            modules(retrofitModule, viewModelModule)
        }
    }
}