package com.example.shoplist.feature_meal_item.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.example.feature_meal_item.R
import com.example.feature_meal_item.databinding.RecipeItemBinding
import com.example.shoplist.core.ui.GlideLoadListener
import com.example.shoplist.domain.models.MealShortEntity
import com.example.shoplist.feature_detail_recipe.navigation.DetailRecipeScreen
import com.example.shoplist.feature_meal_item.viewModel.MealItemController
import com.github.terrakok.cicerone.Router
import org.koin.java.KoinJavaComponent.inject

class MealsAdapter : RecyclerView.Adapter<MealsViewHolder>() {
    private var list: List<MealShortEntity> = emptyList()
    private var presenters: MutableList<MealItemController.Presenter> = mutableListOf()

    fun updateList(list: List<MealShortEntity>) {
        this.list = list
        for (meal in list) {
            val p: MealItemController.Presenter by inject(MealItemController.Presenter::class.java)
            presenters.add(p)
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MealsViewHolder =
        MealsViewHolder(parent)
    override fun onBindViewHolder(holder: MealsViewHolder, position: Int) {
        holder.bind(list[position], presenters[position])
    }
    override fun getItemCount() = list.size

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        for (presenter in presenters) {
            presenter.onDetached()
        }
        super.onDetachedFromRecyclerView(recyclerView)
    }
}

class MealsViewHolder(parent: ViewGroup) : MealItemController.View, RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.recipe_item, parent, false)
) {
    private val binding by viewBinding(RecipeItemBinding::bind)
    private val router: Router by inject(Router::class.java)

    fun bind(
        meal: MealShortEntity,
        presenter: MealItemController.Presenter,
    ) {
        presenter.onAttached(this)
        binding.recipeItemTitleTextView.text = meal.title
        Glide
            .with(itemView.context)
            .load(meal.imageUrl)
            .listener(
                GlideLoadListener(
                    onLoadingComplete = {
                        itemView.setOnClickListener {
                            router.navigateTo(DetailRecipeScreen(meal.id))
                        }
                    },
                    onLoadingFail = {
                        itemView.setOnClickListener {
                            router.navigateTo(DetailRecipeScreen(meal.id))
                        }
                        binding.recipeItemErrorTextView.visibility = View.VISIBLE
                    }
                )
            )
            .into(binding.recipeItemImageView)

        setLikeButtonListener(meal, presenter)
        presenter.onBound(meal)
    }

    private fun setLikeButtonListener(
        meal: MealShortEntity,
        presenter: MealItemController.Presenter
    ) = with(binding.recipeItemLikeButton) {
        setOnClickListener {
            when (isSelected) {
                false -> {
                    presenter.onLiked(meal)
                    setLiked()
                }
                true -> {
                    presenter.onDisliked(meal)
                    setDisliked()
                }
            }
        }
    }

    override fun setLiked() = with(binding.recipeItemLikeButton) {
        isSelected = true
        setImageResource(R.drawable.ic_favorite_full)
    }

    override fun setDisliked() = with(binding.recipeItemLikeButton){
        isSelected = false
        setImageResource(R.drawable.ic_favorite_border)
    }
}