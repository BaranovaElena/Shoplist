package com.example.shoplist.feature_recipes.di

import com.example.shoplist.domain.repos.LoadingMealRepo
import com.example.shoplist.feature_recipes.viewModel.RecipeFilerViewModel
import com.example.shoplist.feature_recipes.viewModel.RecipeFilterController
import com.example.shoplist.feature_recipes.viewModel.RecipeSearchViewModel
import com.example.shoplist.feature_recipes.viewModel.RecipeSearchViewModelImpl
import org.koin.dsl.module

val recipesModule = module {
    factory<RecipeFilterController.BaseViewModel> { RecipeFilerViewModel(get<LoadingMealRepo>()) }
    factory<RecipeSearchViewModel> { RecipeSearchViewModelImpl(get<LoadingMealRepo>()) }
}
