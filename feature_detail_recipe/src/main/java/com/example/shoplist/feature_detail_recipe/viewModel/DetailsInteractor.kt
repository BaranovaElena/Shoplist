package com.example.shoplist.feature_detail_recipe.viewModel

import com.example.shoplist.domain.models.DetailRecipeEntity
import com.example.shoplist.domain.repos.LoadingDetailsRepo

class DetailsInteractor(private val repo: LoadingDetailsRepo) : DetailsController.Interactor {
    override suspend fun getDetailsById(id: Int): DetailRecipeEntity? =
        repo.getDetailsById(id)
}