package com.example.shoplist.feature_shoplist.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.shoplist.domain.models.ShoplistEntity
import com.example.shoplist.feature_shoplist.R
import com.example.shoplist.feature_shoplist.databinding.ItemShoplistIngredientBinding

class ShoplistAdapter(
    private val onItemChecked: (String) -> Unit,
    private val onItemUnchecked: (String) -> Unit,
) : RecyclerView.Adapter<IngredientsViewHolder>() {
    data class Ingredient(
        val name: String,
        val measure: String,
        var isChecked: Boolean = false,
    )

    private var list: MutableList<Ingredient> = mutableListOf()

    fun updateData(ingredients: List<ShoplistEntity>) {
        ingredients.forEach {
            list.add(
                Ingredient(
                    name = it.ingredientName,
                    measure = it.ingredientMeasure.amount.toString() + " " + it.ingredientMeasure.title,
                    isChecked = false,
                )
            )
        }
        notifyDataSetChanged()
    }

    fun setAllIngredientsReady() {
        list.forEach { it.isChecked = true }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        IngredientsViewHolder(parent, onItemChecked, onItemUnchecked)

    override fun onBindViewHolder(holder: IngredientsViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount() = list.size
}

class IngredientsViewHolder(
    parent: ViewGroup,
    private val onItemChecked: (String) -> Unit,
    private val onItemUnchecked: (String) -> Unit,
) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.item_shoplist_ingredient, parent, false)
) {
    private val binding by viewBinding(ItemShoplistIngredientBinding::bind)

    fun bind(ingredient: ShoplistAdapter.Ingredient) = with(binding) {
        itemShoplistIngredientNameTv.text = ingredient.name
        itemShoplistIngredientAmountTv.text = ingredient.measure

        itemShoplistCheckbox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                onItemChecked(ingredient.name)
                setDisabledItem()
            }
            else {
                onItemUnchecked(ingredient.name)
                setEnabledItem()
            }
        }
    }

    private fun setEnabledItem() {
        binding.itemShoplistMainLayout.alpha = 1f
    }

    private fun setDisabledItem() {
        binding.itemShoplistMainLayout.alpha = 0.7f
    }
}