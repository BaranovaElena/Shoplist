package com.example.shoplist.domain.details

import com.example.shoplist.data.DetailRecipesEntity
import com.example.shoplist.domain.MealRetrofitService

class LoadingDetailsRetrofitRepo(private val service: MealRetrofitService) : LoadingDetailsRepo {
    override suspend fun getDetailsById(id: Int): DetailRecipesEntity {
        return service.getDetails(id).await()
    }
}