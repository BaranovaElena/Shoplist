package com.example.shoplist.domain.favorites

import com.example.shoplist.data.MealShortEntity

interface SavingFavoriteMealRepo {
    suspend fun saveMeal(meal: MealShortEntity)
}