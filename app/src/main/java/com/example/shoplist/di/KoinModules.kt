package com.example.shoplist.di

import androidx.room.Room
import com.example.shoplist.domain.favorites.FavoriteMealRoomImpl
import com.example.shoplist.domain.favorites.LoadingFavoritesMealRepo
import com.example.shoplist.domain.favorites.RecipesDao
import com.example.shoplist.domain.favorites.RecipesDatabase
import com.example.shoplist.domain.favorites.SavingFavoriteMealRepo
import com.example.shoplist.domain.repos.LoadingMealRepo
import com.example.shoplist.viewmodel.favorites.FavoritesController
import com.example.shoplist.viewmodel.favorites.FavoritesViewModel
import com.example.shoplist.viewmodel.mealitem.MealItemController
import com.example.shoplist.viewmodel.mealitem.MealItemPresenter
import com.example.shoplist.viewmodel.recipe.RecipeFilerViewModel
import com.example.shoplist.viewmodel.recipe.RecipeFilterController
import com.example.shoplist.viewmodel.recipe.RecipeSearchViewModel
import com.example.shoplist.viewmodel.recipe.RecipeSearchViewModelImpl
import org.koin.dsl.module

val roomModule = module {
    single<RecipesDatabase> {
        Room.databaseBuilder(get(),RecipesDatabase::class.java, "recipes.db")
            .fallbackToDestructiveMigration()
            .build()
    }
    single<RecipesDao> { get<RecipesDatabase>().getDao() }
    single<SavingFavoriteMealRepo> { FavoriteMealRoomImpl(get<RecipesDao>()) }
    single<LoadingFavoritesMealRepo> { FavoriteMealRoomImpl(get<RecipesDao>()) }
}

val viewModelModule = module {
    factory<RecipeFilterController.BaseViewModel> { RecipeFilerViewModel(get<LoadingMealRepo>()) }
    factory<FavoritesController.BaseViewModel> { FavoritesViewModel(get<LoadingFavoritesMealRepo>()) }
    factory<MealItemController.Presenter> {
        MealItemPresenter(get<SavingFavoriteMealRepo>(), get<LoadingFavoritesMealRepo>())
    }
    factory<RecipeSearchViewModel> { RecipeSearchViewModelImpl(get<LoadingMealRepo>()) }
}
