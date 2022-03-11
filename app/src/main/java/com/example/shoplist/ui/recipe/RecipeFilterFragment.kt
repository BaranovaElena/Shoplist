package com.example.shoplist.ui.recipe

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.shoplist.R
import com.example.shoplist.data.*
import com.example.shoplist.databinding.FragmentRecipeFilterBinding
import com.example.shoplist.ui.MealsAdapter
import com.example.shoplist.ui.favorites.FavoritesFragment
import com.example.shoplist.ui.showErrorMessage
import com.example.shoplist.viewmodel.recipe.RecipeFilterController
import org.koin.androidx.viewmodel.ext.android.viewModel

class RecipeFilterFragment : Fragment(R.layout.fragment_recipe_filter), RecipeFilterController.View {
    private val binding by viewBinding(FragmentRecipeFilterBinding::bind)
    private val model: RecipeFilterController.BaseViewModel by viewModel()
    private val recyclerAdapter = MealsAdapter(::recyclerViewItemCallback)

    companion object {
        fun newInstance() = RecipeFilterFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        model.categoriesLoadStateLiveData.observe(viewLifecycleOwner) { renderFilterLoadState(it) }
        model.areasLoadStateLiveData.observe(viewLifecycleOwner) { renderFilterLoadState(it) }
        model.mealsLoadStateLiveData.observe(viewLifecycleOwner) { renderMealsLoadState(it)}

        defineListeners()

        binding.recipeFilterRecyclerView.adapter = recyclerAdapter
        binding.recipeFilterRecyclerView.layoutManager =
            GridLayoutManager(context, resources.getInteger(R.integer.recipes_recycle_span_count))
    }

    private fun defineListeners() {
        binding.recipeFilterChipGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.recipe_filter_chip_category -> { model.onChipChecked(Filters.CATEGORY) }
                R.id.recipe_filter_chip_area -> { model.onChipChecked(Filters.AREA) }
            }
        }

        binding.recipeFilterSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                model.onFilterValueSelected(parent?.getItemAtPosition(position).toString())
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    override fun renderFilterLoadState(state: LoadState<Any>) {
        when (state) {
            is LoadState.Success -> {
                binding.recipeFilterProgressBar.visibility = View.GONE
                fillSpinner(state)
            }
            is LoadState.Error -> {
                binding.recipeFilterProgressBar.visibility = View.GONE
                showErrorMessage(requireContext(), state)
            }
            is LoadState.Loading -> { binding.recipeFilterProgressBar.visibility = View.VISIBLE }
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
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            valuesArray
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.recipeFilterSpinner.adapter = adapter
    }

    override fun renderMealsLoadState(state: LoadState<MealsEntity>) {
        when (state) {
            is LoadState.Success<MealsEntity> -> {
                binding.recipeFilterProgressBar.visibility = View.GONE
                recyclerAdapter.updateList(state.value.meals)
            }
            is LoadState.Error -> {
                binding.recipeFilterProgressBar.visibility = View.GONE
                showErrorMessage(requireContext(), state)
            }
            is LoadState.Loading -> { binding.recipeFilterProgressBar.visibility = View.VISIBLE }
        }
    }

    private fun recyclerViewItemCallback(mealId: Int) {
        (requireActivity() as FavoritesFragment.Contract).openDetailScreen(mealId)
    }

    interface Contract{
        fun openDetailScreen(mealId: Int)
    }
}