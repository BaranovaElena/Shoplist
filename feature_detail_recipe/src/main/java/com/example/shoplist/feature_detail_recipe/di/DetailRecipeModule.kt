package com.example.shoplist.feature_detail_recipe.di

import com.example.shoplist.domain.repos.LoadingDetailsRepo
import com.example.shoplist.feature_detail_recipe.mapper.IngredientMapper
import com.example.shoplist.feature_detail_recipe.viewModel.DetailsController
import com.example.shoplist.feature_detail_recipe.viewModel.DetailsInteractor
import com.example.shoplist.feature_detail_recipe.viewModel.DetailsPresenter
import org.koin.dsl.module

val detailRecipeModule = module {
    factory { IngredientMapper() }
    factory<DetailsController.Presenter> {
        DetailsPresenter(
            DetailsInteractor(get<LoadingDetailsRepo>()),
            get<IngredientMapper>()
        )
    }
}