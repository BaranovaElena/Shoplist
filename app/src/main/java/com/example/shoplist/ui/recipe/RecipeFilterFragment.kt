package com.example.shoplist.ui.recipe

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.shoplist.R
import com.example.shoplist.databinding.FragmentRecipeFilterBinding

class RecipeFilterFragment : Fragment(R.layout.fragment_recipe_filter) {
    private val binding by viewBinding(FragmentRecipeFilterBinding::bind)

    companion object {
        fun newInstance() = RecipeFilterFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recipeFilterChipGroup.setOnCheckedChangeListener { _, checkedId ->
            val filter: Filters
            when (checkedId) {
                R.id.recipe_filter_chip_category -> filter = Filters.CATEGORY
                R.id.recipe_filter_chip_area -> filter = Filters.AREA
            }
            //todo viewModel.chipChecked(filter)
        }

    }
}

enum class Filters {
    CATEGORY, AREA
}