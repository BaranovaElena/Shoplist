package com.example.shoplist.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.example.shoplist.R
import com.example.shoplist.data.MealShortEntity
import com.example.shoplist.databinding.RecipeItemBinding
import com.example.shoplist.domain.favorites.LoadingFavoritesMealRepo
import com.example.shoplist.domain.favorites.SavingFavoriteMealRepo
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import org.koin.java.KoinJavaComponent.inject

class MealsAdapter : RecyclerView.Adapter<MealsViewHolder>() {
    private var list: List<MealShortEntity> = emptyList()
    fun updateList(list: List<MealShortEntity>) {
        this.list = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MealsViewHolder =
        MealsViewHolder(parent)
    override fun onBindViewHolder(holder: MealsViewHolder, position: Int) {
        holder.bind(list[position])
    }
    override fun getItemCount() = list.size
}

class MealsViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.recipe_item, parent, false))
{
    private val binding by viewBinding(RecipeItemBinding::bind)
    private val savingRepo: SavingFavoriteMealRepo by inject(SavingFavoriteMealRepo::class.java)
    private val gettingRepo: LoadingFavoritesMealRepo by inject(LoadingFavoritesMealRepo::class.java)
    private val scope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    fun bind(meal: MealShortEntity) {
        binding.recipeItemTitleTextView.text = meal.title
        Glide
            .with(itemView.context)
            .load(meal.imageUrl)
            .into(binding.recipeItemImageView)

        setLikeButtonListener(meal)
        setFavorite(meal)
    }

    private fun setFavorite(meal: MealShortEntity) {
        scope.launch {
            withContext(Dispatchers.IO) {
                try {
                    gettingRepo.isMealExists(meal.id).collect { res ->
                        if (res) {
                            binding.recipeItemLikeButton.isSelected = true
                            binding.recipeItemLikeButton.setImageResource(R.drawable.ic_favorite_full)
                        } else {
                            binding.recipeItemLikeButton.isSelected = false
                            binding.recipeItemLikeButton.setImageResource(R.drawable.ic_favorite_border)
                        }
                    }
                } catch (thr: Throwable) {

                }
            }
        }
    }

    private fun setLikeButtonListener(meal: MealShortEntity) {
        binding.recipeItemLikeButton.setOnClickListener {
            when (binding.recipeItemLikeButton.isSelected) {
                false -> {
                    binding.recipeItemLikeButton.isSelected = true
                    scope.launch {
                        withContext(Dispatchers.IO) {
                            savingRepo.saveMeal(meal)
                        }
                    }
                    binding.recipeItemLikeButton.setImageResource(R.drawable.ic_favorite_full)
                }
                true -> {
                    binding.recipeItemLikeButton.isSelected = false
                    scope.launch {
                        withContext(Dispatchers.IO) {
                            savingRepo.deleteMeal(meal)
                        }
                    }
                    binding.recipeItemLikeButton.setImageResource(R.drawable.ic_favorite_border)
                }
            }
        }
    }
}