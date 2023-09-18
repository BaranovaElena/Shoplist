package com.example.shoplist.feature_detail_recipe.di

import com.example.shoplist.domain.repos.LoadingDetailsRepo
import com.example.shoplist.domain.repos.SavingShoplistRepo
import com.example.shoplist.feature_detail_recipe.mapper.IngredientMapper
import com.example.shoplist.feature_detail_recipe.viewModel.DetailsViewModel
import com.example.shoplist.feature_detail_recipe.viewModel.DetailsViewModelImpl
import org.koin.dsl.module

val detailRecipeModule = module {
    factory { IngredientMapper() }
    factory<DetailsViewModel> {
        DetailsViewModelImpl(
            get<LoadingDetailsRepo>(),
            get<SavingShoplistRepo>(),
            get<IngredientMapper>(),
        )
    }
}