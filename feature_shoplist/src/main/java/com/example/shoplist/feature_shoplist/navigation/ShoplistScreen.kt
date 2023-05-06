package com.example.shoplist.feature_shoplist.navigation

import com.example.shoplist.feature_shoplist.ui.ShoplistFragment
import com.github.terrakok.cicerone.androidx.FragmentScreen

object ShoplistScreen {
    operator fun invoke() = FragmentScreen { ShoplistFragment.newInstance() }
}