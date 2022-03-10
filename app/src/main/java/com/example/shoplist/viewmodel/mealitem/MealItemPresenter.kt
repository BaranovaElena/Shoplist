package com.example.shoplist.viewmodel.mealitem

import com.example.shoplist.data.MealShortEntity
import com.example.shoplist.domain.favorites.LoadingFavoritesMealRepo
import com.example.shoplist.domain.favorites.SavingFavoriteMealRepo
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import org.koin.java.KoinJavaComponent

class MealItemPresenter : MealItemController.Presenter {
    private var view: MealItemController.View? = null
    private val scope = CoroutineScope(Dispatchers.Main + SupervisorJob())
    private val savingRepo: SavingFavoriteMealRepo by KoinJavaComponent.inject(
        SavingFavoriteMealRepo::class.java
    )
    private val gettingRepo: LoadingFavoritesMealRepo by KoinJavaComponent.inject(
        LoadingFavoritesMealRepo::class.java
    )

    override fun onAttached(view: MealItemController.View) {
        this.view = view
    }

    override fun onDetached() {
        scope.coroutineContext.cancelChildren()
        this.view = null
    }

    override fun onLiked(meal: MealShortEntity) {
        scope.launch {
            withContext(Dispatchers.IO) {
                savingRepo.saveMeal(meal)
            }
        }
    }

    override fun onDisliked(meal: MealShortEntity) {
        scope.launch {
            withContext(Dispatchers.IO) {
                savingRepo.deleteMeal(meal)
            }
        }
    }

    override fun onBound(meal: MealShortEntity) {
        setFavorite(meal)
    }

    private fun setFavorite(meal: MealShortEntity) {
        scope.launch {
            withContext(Dispatchers.IO) {
                gettingRepo.isMealExists(meal.id).collect { res ->
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