package com.example.shoplist.feature_meal_item.di

import com.example.shoplist.domain.repos.LoadingFavoritesMealRepo
import com.example.shoplist.domain.repos.SavingFavoriteMealRepo
import com.example.shoplist.feature_meal_item.viewModel.MealItemController
import com.example.shoplist.feature_meal_item.viewModel.MealItemPresenter
import org.koin.dsl.module

val mealItemModule = module {
    factory<MealItemController.Presenter> {
        MealItemPresenter(get<SavingFavoriteMealRepo>(), get<LoadingFavoritesMealRepo>())
    }
}