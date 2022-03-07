package com.example.shoplist.ui.recipe

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.shoplist.R
import com.example.shoplist.data.*
import com.example.shoplist.databinding.FragmentRecipeFilterBinding
import com.example.shoplist.viewmodel.recipe.RecipeFilterController
import org.koin.androidx.viewmodel.ext.android.viewModel

class RecipeFilterFragment : Fragment(R.layout.fragment_recipe_filter), RecipeFilterController.View {
    private val binding by viewBinding(FragmentRecipeFilterBinding::bind)
    private val model: RecipeFilterController.BaseViewModel by viewModel()

    companion object {
        fun newInstance() = RecipeFilterFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        model.categoriesLoadStateLiveData.observe(viewLifecycleOwner) { renderCategoriesLoadState(it) }
        model.areasLoadStateLiveData.observe(viewLifecycleOwner) { renderAreasLoadState(it) }
        model.mealsLoadStateLiveData.observe(viewLifecycleOwner) { renderMealsLoadState(it)}

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

    override fun renderCategoriesLoadState(state: LoadState<CategoriesEntity>) {
        when (state) {
            is LoadState.Success<CategoriesEntity> -> {
                binding.recipeFilterProgressBar.visibility = View.GONE
                val valuesArray = mutableListOf<String>()
                for (value in state.value.categories) {
                    valuesArray.add(value.title)
                }
                val adapter = ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_spinner_item,
                    valuesArray
                )
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                binding.recipeFilterSpinner.adapter = adapter
            }
            is LoadState.Error -> {
                binding.recipeFilterProgressBar.visibility = View.GONE
                showErrorMessage(state)
            }
            is LoadState.Loading -> { binding.recipeFilterProgressBar.visibility = View.VISIBLE }
        }
    }

    override fun renderAreasLoadState(state: LoadState<AreasEntity>) {
        when (state) {
            is LoadState.Success<AreasEntity> -> {
                binding.recipeFilterProgressBar.visibility = View.GONE
                val valuesArray = mutableListOf<String>()
                for (value in state.value.areas) {
                    valuesArray.add(value.title)
                }
                val adapter = ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_spinner_item,
                    valuesArray
                )
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                binding.recipeFilterSpinner.adapter = adapter
            }
            is LoadState.Error -> {
                binding.recipeFilterProgressBar.visibility = View.GONE
                showErrorMessage(state)
            }
            is LoadState.Loading -> { binding.recipeFilterProgressBar.visibility = View.VISIBLE }
        }
    }

    override fun renderMealsLoadState(state: LoadState<MealsEntity>) {
        when (state) {
            is LoadState.Success<MealsEntity> -> {
                binding.recipeFilterProgressBar.visibility = View.GONE
                Toast.makeText(context, state.value.meals[0].title, Toast.LENGTH_SHORT).show()
            }
            is LoadState.Error -> {
                binding.recipeFilterProgressBar.visibility = View.GONE
                showErrorMessage(state)
            }
            is LoadState.Loading -> { binding.recipeFilterProgressBar.visibility = View.VISIBLE }
        }
    }

    private fun showErrorMessage(state: LoadState.Error) {
        Toast.makeText(
            context,
            state.message ?: getString(
                when (state.error) {
                    Errors.SERVER_ERROR -> { R.string.server_error_message }
                    Errors.LOAD_ERROR -> { R.string.load_error_message }
                }
            ),
            Toast.LENGTH_SHORT
        ).show()
    }
}