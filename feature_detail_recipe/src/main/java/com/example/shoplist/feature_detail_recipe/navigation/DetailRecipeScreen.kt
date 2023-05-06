package com.example.shoplist.feature_detail_recipe.navigation

import com.example.shoplist.feature_detail_recipe.ui.DetailRecipeFragment
import com.github.terrakok.cicerone.androidx.FragmentScreen

object DetailRecipeScreen {
    operator fun invoke(mealId: Int) = FragmentScreen { DetailRecipeFragment.newInstance(mealId) }
}