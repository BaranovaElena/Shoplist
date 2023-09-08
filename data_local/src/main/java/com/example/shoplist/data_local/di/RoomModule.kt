package com.example.shoplist.data_local.di

import androidx.room.Room
import com.example.shoplist.data_local.api.IngredientsDao
import com.example.shoplist.data_local.api.IngredientsDatabase
import com.example.shoplist.data_local.api.RecipesDao
import com.example.shoplist.data_local.api.RecipesDatabase
import com.example.shoplist.data_local.mappers.IngredientMapper
import com.example.shoplist.data_local.mappers.MealsMapper
import com.example.shoplist.data_local.repos.FavoriteMealRoomImpl
import com.example.shoplist.data_local.repos.ShoplistRoomImpl
import com.example.shoplist.domain.repos.LoadingFavoritesMealRepo
import com.example.shoplist.domain.repos.LoadingShoplistRepo
import com.example.shoplist.domain.repos.SavingFavoriteMealRepo
import com.example.shoplist.domain.repos.SavingShoplistRepo
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

    single<IngredientsDatabase> {
        Room.databaseBuilder(get(), IngredientsDatabase::class.java, "ingredients.db")
            .fallbackToDestructiveMigration()
            .build()
    }
    single<IngredientsDao> { get<IngredientsDatabase>().getDao() }
    factory { IngredientMapper() }
    single<SavingShoplistRepo> {
        ShoplistRoomImpl(get<IngredientsDao>(), get<IngredientMapper>())
    }
    single<LoadingShoplistRepo> {
        ShoplistRoomImpl(get<IngredientsDao>(), get<IngredientMapper>())
    }
}