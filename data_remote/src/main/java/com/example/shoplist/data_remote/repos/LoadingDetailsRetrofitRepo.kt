package com.example.shoplist.data_remote.repos

import com.example.shoplist.data_remote.api.MealRetrofitService
import com.example.shoplist.data_remote.mappers.DetailRecipeMapper
import com.example.shoplist.domain.models.DetailRecipeEntity
import com.example.shoplist.domain.repos.LoadingDetailsRepo

class LoadingDetailsRetrofitRepo(
    private val service: MealRetrofitService,
    private val mapper: DetailRecipeMapper,
) : LoadingDetailsRepo {
    override suspend fun getDetailsById(id: Int): DetailRecipeEntity? {
        return service.getDetails(id)
            .await()
            .let(mapper::toDomain)
    }
}