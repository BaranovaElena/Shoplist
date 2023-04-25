package com.example.shoplist.data_remote.repos

import com.example.shoplist.data_remote.api.MealRetrofitService
import com.example.shoplist.domain.models.DetailRecipesEntity
import com.example.shoplist.domain.repos.LoadingDetailsRepo

class LoadingDetailsRetrofitRepo(private val service: MealRetrofitService) : LoadingDetailsRepo {
    override suspend fun getDetailsById(id: Int): DetailRecipesEntity {
        return service.getDetails(id).await()
    }
}