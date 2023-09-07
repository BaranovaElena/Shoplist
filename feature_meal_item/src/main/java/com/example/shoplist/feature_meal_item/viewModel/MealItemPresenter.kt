package com.example.shoplist.feature_meal_item.viewModel

import com.example.shoplist.domain.models.MealShortEntity
import com.example.shoplist.domain.repos.LoadingFavoritesMealRepo
import com.example.shoplist.domain.repos.SavingFavoriteMealRepo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MealItemPresenter(
    private val savingRepo: SavingFavoriteMealRepo,
    private val gettingRepo: LoadingFavoritesMealRepo,
) : MealItemController.Presenter {
    private var view: MealItemController.View? = null
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    override fun onAttached(view: MealItemController.View) {
        this.view = view
    }

    override fun onDetached() {
        scope.coroutineContext.cancelChildren()
        this.view = null
    }

    override fun onLiked(meal: MealShortEntity) {
        scope.launch {
            savingRepo.saveMeal(meal)
        }
    }

    override fun onDisliked(meal: MealShortEntity) {
        scope.launch {
            savingRepo.deleteMeal(meal)
        }
    }

    override fun onBound(meal: MealShortEntity) {
        setFavorite(meal)
    }

    private fun setFavorite(meal: MealShortEntity) {
        scope.launch {
            gettingRepo.isMealExists(meal.id).collect { res ->
                withContext(Dispatchers.Main) {
                    if (res) {
                        view?.setLiked()
                    } else {
                        view?.setDisliked()
                    }
                }
            }
        }
    }
}