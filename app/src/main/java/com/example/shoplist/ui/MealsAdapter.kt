package com.example.shoplist.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.example.shoplist.R
import com.example.shoplist.data.MealShortEntity
import com.example.shoplist.databinding.RecipeItemBinding
import com.example.shoplist.viewmodel.mealitem.MealItemController
import com.example.shoplist.viewmodel.mealitem.MealItemPresenter

class MealsAdapter : RecyclerView.Adapter<MealsViewHolder>() {
    private var list: List<MealShortEntity> = emptyList()
    private var presenters: MutableList<MealItemController.Presenter> = mutableListOf()
    fun updateList(list: List<MealShortEntity>) {
        this.list = list
        for (meal in list) {
            presenters.add(MealItemPresenter())
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
    LayoutInflater.from(parent.context).inflate(R.layout.recipe_item, parent, false))
{
    private val binding by viewBinding(RecipeItemBinding::bind)

    fun bind(meal: MealShortEntity, presenter: MealItemController.Presenter) {
        presenter.onAttached(this)
        binding.recipeItemTitleTextView.text = meal.title
        Glide
            .with(itemView.context)
            .load(meal.imageUrl)
            .into(binding.recipeItemImageView)

        setLikeButtonListener(meal, presenter)
        presenter.onBound(meal)
    }

    private fun setLikeButtonListener(
        meal: MealShortEntity,
        presenter: MealItemController.Presenter
    ) {
        binding.recipeItemLikeButton.setOnClickListener {
            when (binding.recipeItemLikeButton.isSelected) {
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

    override fun setLiked() {
        binding.recipeItemLikeButton.isSelected = true
        binding.recipeItemLikeButton.setImageResource(R.drawable.ic_favorite_full)
    }

    override fun setDisliked() {
        binding.recipeItemLikeButton.isSelected = false
        binding.recipeItemLikeButton.setImageResource(R.drawable.ic_favorite_border)
    }
}