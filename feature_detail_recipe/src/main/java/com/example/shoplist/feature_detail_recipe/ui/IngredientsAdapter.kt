package com.example.shoplist.feature_detail_recipe.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.shoplist.feature_detail_recipe.R
import com.example.shoplist.feature_detail_recipe.databinding.IngredientItemBinding

class IngredientsAdapter(
    private val callback: (Pair<String, String>) -> Unit,
) : RecyclerView.Adapter<IngredientsViewHolder>() {
    data class Ingredient(
        val name: String,
        val measure: String,
        var isReady: Boolean = false,
    )
    private var list: MutableList<Ingredient> = mutableListOf()

    fun updateData(map: Map<String, String>) {
        map.forEach {
            list.add(Ingredient(it.key, it.value))
        }
        notifyDataSetChanged()
    }

    fun setAllIngredientsReady() {
        list.forEach { it.isReady = true }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        IngredientsViewHolder(parent, callback)

    override fun onBindViewHolder(holder: IngredientsViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount() = list.size
}

class IngredientsViewHolder(
    parent: ViewGroup,
    private val onButtonClick: (Pair<String, String>) -> Unit,
) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.ingredient_item, parent, false)
) {
    private val binding by viewBinding(IngredientItemBinding::bind)

    fun bind(ingredient: IngredientsAdapter.Ingredient) = with(binding) {
        ingredientItemTitleTextView.text = ingredient.name
        ingredientItemMeasureTextView.text = ingredient.measure

        ingredientItemAddButton.apply {
            when (ingredient.isReady) {
                true -> if (isClickable) setReady()
                false -> setOnClickListener {
                    setReady()
                    onButtonClick(Pair(ingredient.name, ingredient.measure))
                    Toast.makeText(itemView.context, "not realized yet", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setReady() = binding.ingredientItemAddButton.apply {
        text = binding.root.context.getString(R.string.item_ingredient_add_button_text_ready)
        isClickable = false
    }
}