package com.example.shoplist.data_local.repos

import com.example.shoplist.data_local.api.RecipesDao
import com.example.shoplist.data_local.mappers.MealsMapper
import com.example.shoplist.domain.models.MealShortEntity
import com.example.shoplist.domain.repos.LoadingFavoritesMealRepo
import com.example.shoplist.domain.repos.SavingFavoriteMealRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class FavoriteMealRoomImpl(
    private val dao: RecipesDao,
    private val mapper: MealsMapper,
): SavingFavoriteMealRepo,
    LoadingFavoritesMealRepo {
    override suspend fun loadMeals(): Flow<List<MealShortEntity>> {
        return flow {
            emit (dao.getAllMeals().map(mapper::toDomain))
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun isMealExists(id: Int): Flow<Boolean> {
        return flow {
            emit (dao.isMealExist(id))
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun saveMeal(meal: MealShortEntity) {
        dao.saveMeal(mapper.toApi(meal))
    }

    override suspend fun deleteMeal(meal: MealShortEntity) {
        dao.deleteMeal(mapper.toApi(meal))
    }
}