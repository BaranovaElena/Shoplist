package com.example.shoplist.data_local.di

import androidx.room.Room
import com.example.shoplist.data_local.api.RecipesDao
import com.example.shoplist.data_local.api.RecipesDatabase
import com.example.shoplist.data_local.mappers.MealsMapper
import com.example.shoplist.data_local.repos.FavoriteMealRoomImpl
import com.example.shoplist.domain.repos.LoadingFavoritesMealRepo
import com.example.shoplist.domain.repos.SavingFavoriteMealRepo
import org.koin.dsl.module

val roomModule = module {
    single<RecipesDatabase> {
        Room.databaseBuilder(get(), RecipesDatabase::class.java, "recipes.db")
            .fallbackToDestructiveMigration()
            .build()
    }
    single<RecipesDao> { get<RecipesDatabase>().getDao() }
    factory { MealsMapper() }
    single<SavingFavoriteMealRepo> {
        FavoriteMealRoomImpl(get<RecipesDao>(), get<MealsMapper>())
    }
    single<LoadingFavoritesMealRepo> {
        FavoriteMealRoomImpl(get<RecipesDao>(), get<MealsMapper>())
    }
}