package com.example.shoplist.feature_shoplist.di

import com.example.shoplist.domain.repos.LoadingShoplistRepo
import com.example.shoplist.feature_shoplist.viewModel.ShoplistViewModel
import com.example.shoplist.feature_shoplist.viewModel.ShoplistViewModelImpl
import org.koin.dsl.module

val shoplistModule = module {
    factory<ShoplistViewModel> { ShoplistViewModelImpl(get<LoadingShoplistRepo>()) }
}
