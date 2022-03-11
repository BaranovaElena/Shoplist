package com.example.shoplist.viewmodel.details

import com.example.shoplist.data.DetailRecipeEntity
import com.example.shoplist.data.Errors
import moxy.MvpPresenter
import moxy.MvpView
import moxy.viewstate.strategy.alias.AddToEndSingle
import moxy.viewstate.strategy.alias.Skip

class DetailsController {
    interface View: MvpView{
        @AddToEndSingle
        fun showRecipe(recipe: DetailRecipeEntity)
        @Skip
        fun showLoading()
        @Skip
        fun showError(errorType: Errors, message: String?)
    }

    interface Interactor{
        suspend fun getDetailsById(id: Int) : DetailRecipeEntity?
    }

    abstract class Presenter : MvpPresenter<View>(){
        abstract fun onViewCreated(mealId: Int)
    }
}