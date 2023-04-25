package com.example.shoplist.ui.recipe

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.shoplist.R
import com.example.shoplist.data.LoadState
import com.example.shoplist.domain.models.MealsEntity
import com.example.shoplist.databinding.FragmentRecipeSearchBinding
import com.example.shoplist.ui.MealsAdapter
import com.example.shoplist.ui.favorites.FavoritesFragment
import com.example.shoplist.ui.setVisibility
import com.example.shoplist.ui.showErrorMessage
import com.example.shoplist.viewmodel.recipe.RecipeSearchViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class RecipeSearchFragment : Fragment(R.layout.fragment_recipe_search) {

    private val binding by viewBinding(FragmentRecipeSearchBinding::bind)
    private val model: RecipeSearchViewModel by viewModel()
    private val recyclerAdapter = MealsAdapter(::recyclerViewItemCallback)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        model.loadState.observeForever(::renderSearchLoadState)

        with(binding) {
            recipeSearchRecyclerView.apply {
                adapter = recyclerAdapter
                layoutManager = GridLayoutManager(
                    context,
                    resources.getInteger(R.integer.recipes_recycle_span_count)
                )
            }

            recipeSearchButton.setOnClickListener {
                model.onSearchClicked(
                    name = recipeSearchInputEditText.text.toString()
                )
            }
        }
    }

    private fun renderSearchLoadState(state: LoadState<MealsEntity>) {
        with(binding) {
            recipeSearchProgressBar.setVisibility(state is LoadState.Loading)
            recipeSearchEmptyTextView.setVisibility(
                (state is LoadState.Success) && state.value.meals.isNullOrEmpty()
            )
        }

        when (state) {
            is LoadState.Success<MealsEntity> ->
                state.value.meals?.let(recyclerAdapter::updateList)
            is LoadState.Error -> showErrorMessage(requireContext(), state)
            is LoadState.Loading -> {}
        }
    }

    private fun recyclerViewItemCallback(mealId: Int) {
        (requireActivity() as FavoritesFragment.Contract).openDetailScreen(mealId)
    }

    companion object {
        fun newInstance() = RecipeSearchFragment()
    }
}
