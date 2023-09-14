package com.example.shoplist.domain.repos

import com.example.shoplist.domain.models.ShoplistEntity

interface SavingShoplistRepo {
    suspend fun saveIngredient(ingredient: ShoplistEntity)
    suspend fun deleteIngredient(ingredient: ShoplistEntity)
    suspend fun updateIngredient(ingredient: ShoplistEntity)
}