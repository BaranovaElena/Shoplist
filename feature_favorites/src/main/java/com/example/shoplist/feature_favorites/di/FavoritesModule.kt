package com.example.shoplist.feature_favorites.di

import com.example.shoplist.domain.repos.LoadingFavoritesMealRepo
import com.example.shoplist.feature_favorites.viewModel.FavoritesController
import com.example.shoplist.feature_favorites.viewModel.FavoritesViewModel
import org.koin.dsl.module

val favoritesModule = module {
    factory<FavoritesController.BaseViewModel> {
        FavoritesViewModel(get<LoadingFavoritesMealRepo>())
    }
}