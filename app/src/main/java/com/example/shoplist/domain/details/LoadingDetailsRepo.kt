package com.example.shoplist.domain.details

import com.example.shoplist.data.DetailRecipesEntity

interface LoadingDetailsRepo {
    suspend fun getDetailsById(id: Int) : DetailRecipesEntity
}