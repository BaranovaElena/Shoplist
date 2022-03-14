package com.example.shoplist.ui.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.shoplist.R
import com.example.shoplist.databinding.IngredientItemBinding

class IngredientsAdapter : RecyclerView.Adapter<IngredientsViewHolder>() {
    data class Ingredient(val name: String, val measure: String)
    private var list: MutableList<Ingredient> = mutableListOf()

    fun updateData(map: Map<String, String>) {
        map.forEach {
            list.add(Ingredient(it.key, it.value))
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        IngredientsViewHolder(parent)

    override fun onBindViewHolder(holder: IngredientsViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount() = list.size
}

class IngredientsViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.ingredient_item, parent, false)
) {
    private val binding by viewBinding(IngredientItemBinding::bind)

    fun bind(ingredient: IngredientsAdapter.Ingredient) {
        binding.ingredientItemTitleTextView.text = ingredient.name
        binding.ingredientItemMeasureTextView.text = ingredient.measure

        binding.ingredientItemAddButton.setOnClickListener {
            Toast.makeText(itemView.context, "not realized yet", Toast.LENGTH_SHORT).show()
        }
    }
}