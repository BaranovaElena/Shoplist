package com.example.shoplist.ui.recipe

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.shoplist.R
import com.example.shoplist.data.AreasEntity
import com.example.shoplist.data.CategoriesEntity
import com.example.shoplist.data.Filters
import com.example.shoplist.data.LoadState
import com.example.shoplist.data.MealsEntity
import com.example.shoplist.databinding.FragmentRecipeFilterBinding
import com.example.shoplist.ui.MealsAdapter
import com.example.shoplist.ui.favorites.FavoritesFragment
import com.example.shoplist.ui.showErrorMessage
import com.example.shoplist.viewmodel.recipe.RecipeFilterController
import org.koin.androidx.viewmodel.ext.android.viewModel

class RecipeFilterFragment : Fragment(R.layout.fragment_recipe_filter),
    RecipeFilterController.View {
    private val binding by viewBinding(FragmentRecipeFilterBinding::bind)
    private val model: RecipeFilterController.BaseViewModel by viewModel()
    private val recyclerAdapter = MealsAdapter(::recyclerViewItemCallback)

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

    override fun renderFilterLoadState(state: LoadState<Any>) =
        with(binding.recipeFilterProgressBar) {
            when (state) {
                is LoadState.Success -> {
                    visibility = View.GONE
                    fillSpinner(state)
                }
                is LoadState.Error -> {
                    visibility = View.GONE
                    showErrorMessage(requireContext(), state)
                }
                is LoadState.Loading -> {
                    visibility = View.VISIBLE
                }
            }
        }

    override fun renderMealsLoadState(state: LoadState<MealsEntity>) =
        with(binding.recipeFilterProgressBar) {
            when (state) {
                is LoadState.Success<MealsEntity> -> {
                    visibility = View.GONE
                    recyclerAdapter.updateList(state.value.meals)
                }
                is LoadState.Error -> {
                    visibility = View.GONE
                    showErrorMessage(requireContext(), state)
                }
                is LoadState.Loading -> {
                    visibility = View.VISIBLE
                }
            }
        }

    private fun defineListeners() = with(binding) {
        recipeFilterChipGroup.setOnCheckedChangeListener { _, checkedId ->
            model.onChipChecked(
                when (checkedId) {
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
                for (value in state.value.categories) {
                    valuesArray.add(value.title)
                }
            }
            is AreasEntity -> {
                for (value in state.value.areas) {
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

    private fun recyclerViewItemCallback(mealId: Int) {
        (requireActivity() as FavoritesFragment.Contract).openDetailScreen(mealId)
    }

    interface Contract {
        fun openDetailScreen(mealId: Int)
    }

    companion object {
        fun newInstance() = RecipeFilterFragment()
    }
}