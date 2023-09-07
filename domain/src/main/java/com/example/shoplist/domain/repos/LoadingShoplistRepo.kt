package com.example.shoplist.domain.repos

import com.example.shoplist.domain.models.ShoplistEntity
import kotlinx.coroutines.flow.Flow

interface LoadingShoplistRepo {
    suspend fun loadShoplist() : Flow<List<ShoplistEntity>>
}