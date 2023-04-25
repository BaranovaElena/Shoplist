package com.example.shoplist.domain.favorites

import com.example.shoplist.domain.models.MealShortEntity

interface SavingFavoriteMealRepo {
    suspend fun saveMeal(meal: MealShortEntity)
    suspend fun deleteMeal(meal: MealShortEntity)
}