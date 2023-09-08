package com.example.shoplist.data_local.repos

import com.example.shoplist.data_local.api.IngredientsDao
import com.example.shoplist.data_local.mappers.IngredientMapper
import com.example.shoplist.domain.models.ShoplistEntity
import com.example.shoplist.domain.repos.LoadingShoplistRepo
import com.example.shoplist.domain.repos.SavingShoplistRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class ShoplistRoomImpl(
    private val dao: IngredientsDao,
    private val mapper: IngredientMapper,
) : LoadingShoplistRepo, SavingShoplistRepo {

    override suspend fun loadShoplist(): Flow<List<ShoplistEntity>> = flow {
        emit(dao.getAllIngredients().map(mapper::toDomain))
    }.flowOn(Dispatchers.IO)

    override suspend fun saveIngredient(ingredient: ShoplistEntity) {
        if (dao.isIngredientExists(ingredient.ingredientName)) {
            val previousAmount = dao.getIngredientByName(ingredient.ingredientName).measureAmount
            dao.saveIngredient(
                mapper.toApi(
                    domain = ingredient,
                    newAmount = ingredient.ingredientMeasure.amount + previousAmount,
                )
            )
        } else {
            dao.saveIngredient(mapper.toApi(ingredient))
        }
    }

    override suspend fun deleteIngredient(ingredient: ShoplistEntity) {
        val previousAmount = dao.getIngredientByName(ingredient.ingredientName).measureAmount
        if (previousAmount > ingredient.ingredientMeasure.amount) {
            dao.saveIngredient(
                mapper.toApi(
                    domain = ingredient,
                    newAmount = previousAmount - ingredient.ingredientMeasure.amount,
                )
            )
        } else {
            dao.deleteIngredientByTitle(ingredient.ingredientName)
        }
    }
}