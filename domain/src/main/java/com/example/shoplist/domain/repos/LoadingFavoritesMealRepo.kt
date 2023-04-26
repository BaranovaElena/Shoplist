package com.example.shoplist.domain.repos

import com.example.shoplist.domain.models.MealShortEntity
import kotlinx.coroutines.flow.Flow

interface LoadingFavoritesMealRepo {
    suspend fun loadMeals() : Flow<List<MealShortEntity>>
    suspend fun isMealExists(id: Int): Flow<Boolean>
}
