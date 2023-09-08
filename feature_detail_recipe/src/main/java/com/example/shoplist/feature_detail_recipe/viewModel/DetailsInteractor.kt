package com.example.shoplist.feature_detail_recipe.viewModel

import com.example.shoplist.domain.models.DetailRecipeEntity
import com.example.shoplist.domain.models.ShoplistEntity
import com.example.shoplist.domain.repos.LoadingDetailsRepo
import com.example.shoplist.domain.repos.SavingShoplistRepo

class DetailsInteractor(
    private val loadingDetailsRepo: LoadingDetailsRepo,
    private val savingShoplistRepo: SavingShoplistRepo,
) : DetailsController.Interactor {
    override suspend fun getDetailsById(id: Int): DetailRecipeEntity? =
        loadingDetailsRepo.getDetailsById(id)

    override suspend fun saveIngredient(ingredient: ShoplistEntity) {
        savingShoplistRepo.saveIngredient(ingredient)
    }
}