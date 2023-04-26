package com.example.shoplist.feature_meal_item.viewModel

import com.example.shoplist.domain.models.MealShortEntity

class MealItemController {
    interface View {
        fun setLiked()
        fun setDisliked()
    }

    interface Presenter {
        fun onAttached(view: View)
        fun onDetached()
        fun onLiked(meal: MealShortEntity)
        fun onDisliked(meal: MealShortEntity)
        fun onBound(meal: MealShortEntity)
    }
}