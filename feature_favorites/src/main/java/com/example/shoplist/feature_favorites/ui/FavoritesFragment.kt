package com.example.shoplist.feature_favorites.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.shoplist.core.ui.showErrorMessage
import com.example.shoplist.domain.models.MealShortEntity
import com.example.shoplist.feature_favorites.R
import com.example.shoplist.feature_favorites.databinding.FragmentFavoritesBinding
import com.example.shoplist.feature_favorites.models.LoadState
import com.example.shoplist.feature_meal_item.ui.MealsAdapter
import com.example.shoplist.feature_favorites.viewModel.FavoritesController
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoritesFragment : Fragment(R.layout.fragment_favorites), FavoritesController.View {
    private val binding by viewBinding(FragmentFavoritesBinding::bind)
    private val recyclerAdapter = MealsAdapter()
    private val model: FavoritesController.BaseViewModel by viewModel()

    companion object {
        fun newInstance() = FavoritesFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.favoritesRecyclerView.apply {
            adapter = recyclerAdapter
            layoutManager = GridLayoutManager(
                context,
                resources.getInteger(R.integer.recipes_recycle_span_count)
            )
        }

        with(model) {
            loadStateLiveData.observe(viewLifecycleOwner) { renderMealsList(it) }
            onViewCreated()
        }
    }

    override fun renderMealsList(state: LoadState<List<MealShortEntity>>) {
        when (state) {
            is LoadState.Success -> {
                recyclerAdapter.updateList(state.value)
            }
            is LoadState.Error -> {
                showErrorMessage(requireContext(), state.errorType, state.message)
            }
            is LoadState.Loading -> {}
        }
    }
}