package com.example.shoplist.domain.repos

import com.example.shoplist.domain.models.DetailRecipeEntity

interface LoadingDetailsRepo {
    suspend fun getDetailsById(id: Int) : DetailRecipeEntity?
}