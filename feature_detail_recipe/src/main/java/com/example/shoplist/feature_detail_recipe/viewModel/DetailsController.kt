package com.example.shoplist.feature_detail_recipe.viewModel

import com.example.shoplist.domain.models.DetailRecipeEntity
import com.example.shoplist.domain.models.Errors
import com.example.shoplist.domain.models.ShoplistEntity
import moxy.MvpPresenter
import moxy.MvpView
import moxy.viewstate.strategy.alias.AddToEndSingle
import moxy.viewstate.strategy.alias.Skip

class DetailsController {
    interface View: MvpView{
        @AddToEndSingle
        fun showRecipe(recipe: DetailRecipeEntity)
        @AddToEndSingle
        fun showLoading()
        @Skip
        fun showError(errorType: Errors, message: String?)
        @Skip
        fun showIngredientAdded(ingredient: Pair<String, String>)
    }

    interface Interactor{
        suspend fun getDetailsById(id: Int) : DetailRecipeEntity?
        suspend fun saveIngredient(ingredient: ShoplistEntity)
    }

    abstract class Presenter : MvpPresenter<View>(){
        abstract fun onViewCreated(mealId: Int)
        abstract fun onAddIngredient(ingredient: Pair<String, String>)
    }
}