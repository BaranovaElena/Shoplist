package com.example.shoplist.domain.repos

import com.example.shoplist.domain.models.DetailRecipesEntity

interface LoadingDetailsRepo {
    suspend fun getDetailsById(id: Int) : DetailRecipesEntity
}