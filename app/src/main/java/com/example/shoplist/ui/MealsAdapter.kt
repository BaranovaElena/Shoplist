package com.example.shoplist.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.example.shoplist.R
import com.example.shoplist.data.MealShortEntity
import com.example.shoplist.databinding.RecipeItemBinding
import com.example.shoplist.domain.favorites.SavingFavoriteMealRepo
import kotlinx.coroutines.*
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
    private val repo: SavingFavoriteMealRepo by inject(SavingFavoriteMealRepo::class.java)
    private val scope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    fun bind(meal: MealShortEntity) {
        binding.recipeItemTitleTextView.text = meal.title
        Glide
            .with(itemView.context)
            .load(meal.imageUrl)
            .into(binding.recipeItemImageView)
        binding.recipeItemLikeButton.setOnClickListener {
            scope.launch {
                withContext(Dispatchers.IO) {
                    repo.saveMeal(meal)
                }
            }
            binding.recipeItemLikeButton.setImageResource(R.drawable.ic_favorite_full)
        }
    }
}