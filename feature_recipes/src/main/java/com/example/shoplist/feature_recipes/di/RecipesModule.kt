package com.example.shoplist.feature_recipes.di

import com.example.shoplist.domain.repos.LoadingMealRepo
import com.example.shoplist.feature_recipes.viewModel.RecipeFilerViewModel
import com.example.shoplist.feature_recipes.viewModel.RecipeFilerViewModelImpl
import com.example.shoplist.feature_recipes.viewModel.RecipeSearchViewModel
import com.example.shoplist.feature_recipes.viewModel.RecipeSearchViewModelImpl
import org.koin.dsl.module

val recipesModule = module {
    factory<RecipeFilerViewModel> { RecipeFilerViewModelImpl(get<LoadingMealRepo>()) }
    factory<RecipeSearchViewModel> { RecipeSearchViewModelImpl(get<LoadingMealRepo>()) }
}
