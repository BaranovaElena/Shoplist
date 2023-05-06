package com.example.shoplist.feature_recipes.navigation

import com.example.shoplist.feature_recipes.ui.RecipesFragment
import com.github.terrakok.cicerone.androidx.FragmentScreen

object RecipesScreen {
    operator fun invoke() = FragmentScreen { RecipesFragment.newInstance() }
}