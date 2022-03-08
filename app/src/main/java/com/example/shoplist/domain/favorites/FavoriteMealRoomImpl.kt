package com.example.shoplist.domain.favorites

import com.example.shoplist.data.MealShortEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class FavoriteMealRoomImpl(private val dao: RecipesDao): SavingFavoriteMealRepo, LoadingFavoritesMealRepo {
    override suspend fun loadMeals(): Flow<List<MealShortEntity>> {
        return flow {
            emit (dao.getAllMeals())
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun isMealExists(id: Int): Flow<Boolean> {
        return flow {
            emit (dao.isMealExist(id))
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun saveMeal(meal: MealShortEntity) {
        dao.saveMeal(meal)
    }

    override suspend fun deleteMeal(meal: MealShortEntity) {
        dao.deleteMeal(meal)
    }
}