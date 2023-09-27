package com.example.shoplist.feature_recipes.ui

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.shoplist.core.models.LoadState
import com.example.shoplist.core.ui.showErrorMessage
import com.example.shoplist.core.ui.setVisibility
import com.example.shoplist.domain.models.MealsEntity
import com.example.shoplist.domain.models.AreasEntity
import com.example.shoplist.domain.models.CategoriesEntity
import com.example.shoplist.domain.models.Filters
import com.example.shoplist.feature_meal_item.ui.MealsAdapter
import com.example.shoplist.feature_recipes.R
import com.example.shoplist.feature_recipes.databinding.FragmentRecipeFilterBinding
import com.example.shoplist.feature_recipes.viewModel.RecipeFilerViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class RecipeFilterFragment : Fragment(R.layout.fragment_recipe_filter) {
    private val binding by viewBinding(FragmentRecipeFilterBinding::bind)
    private val model: RecipeFilerViewModel by viewModel()
    private val recyclerAdapter = MealsAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(model) {
            categoriesLoadStateLiveData.observe(viewLifecycleOwner) { renderFilterLoadState(it) }
            areasLoadStateLiveData.observe(viewLifecycleOwner) { renderFilterLoadState(it) }
            mealsLoadStateLiveData.observe(viewLifecycleOwner) { renderMealsLoadState(it) }
        }

        defineListeners()

        binding.recipeFilterRecyclerView.apply {
            adapter = recyclerAdapter
            layoutManager = GridLayoutManager(
                context,
                resources.getInteger(R.integer.recipes_recycle_span_count)
            )
        }

        binding.recipeFilterChipGroup.apply {
            check(getChildAt(0).id)
        }
    }

    override fun onDestroyView() {
        binding.recipeFilterRecyclerView.adapter = null
        super.onDestroyView()
    }

    private fun renderFilterLoadState(state: LoadState<Any>) {
        binding.recipeFilterProgressBar.setVisibility(state !is LoadState.Error)
        when (state) {
            is LoadState.Success -> fillSpinner(state)
            is LoadState.Error -> showErrorMessage(requireContext(), state.errorType, state.message)
            is LoadState.Loading -> {}
        }
    }

    private fun renderMealsLoadState(state: LoadState<MealsEntity>) {
        binding.recipeFilterProgressBar.setVisibility(state is LoadState.Loading)
        when (state) {
            is LoadState.Success<MealsEntity> -> {
                state.value.meals?.let(recyclerAdapter::updateList)
            }
            is LoadState.Error -> showErrorMessage(requireContext(), state.errorType, state.message)
            is LoadState.Loading -> {}
        }
    }

    private fun defineListeners() = with(binding) {
        recipeFilterChipGroup.setOnCheckedStateChangeListener { _, checkedIds ->
            model.onChipChecked(
                when (checkedIds.first()) {
                    R.id.recipe_filter_chip_category -> Filters.CATEGORY
                    R.id.recipe_filter_chip_area -> Filters.AREA
                    else -> Filters.CATEGORY
                }
            )
        }

        recipeFilterSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                model.onFilterValueSelected(parent?.getItemAtPosition(position).toString())
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun fillSpinner(state: LoadState.Success<Any>) {
        val valuesArray = mutableListOf<String>()
        when (state.value) {
            is CategoriesEntity -> {
                for (value in (state.value as? CategoriesEntity)?.categories ?: emptyList()) {
                    valuesArray.add(value.title)
                }
            }
            is AreasEntity -> {
                for (value in (state.value as? AreasEntity)?.areas ?: emptyList()) {
                    valuesArray.add(value.title)
                }
            }
        }

        binding.recipeFilterSpinner.adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            valuesArray
        ).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
    }

    companion object {
        fun newInstance() = RecipeFilterFragment()
    }
}