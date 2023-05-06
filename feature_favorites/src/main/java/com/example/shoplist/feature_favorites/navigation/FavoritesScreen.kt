package com.example.shoplist.feature_favorites.navigation

import com.example.shoplist.feature_favorites.ui.FavoritesFragment
import com.github.terrakok.cicerone.androidx.FragmentScreen

object FavoritesScreen {
    operator fun invoke() = FragmentScreen { FavoritesFragment.newInstance() }
}