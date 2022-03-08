package com.example.shoplist.domain.favorites

import com.example.shoplist.data.MealShortEntity
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.flow.Flow

interface LoadingFavoritesMealRepo {
    suspend fun loadMeals() : Flow<List<MealShortEntity>>
    suspend fun isMealExists(id: Int): Flow<Boolean>
}