package com.example.shoplist.ui.favorites

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.shoplist.R
import com.example.shoplist.data.LoadState
import com.example.shoplist.data.MealShortEntity
import com.example.shoplist.databinding.FragmentFavoritesBinding
import com.example.shoplist.ui.MealsAdapter
import com.example.shoplist.ui.showErrorMessage
import com.example.shoplist.viewmodel.favorites.FavoritesController
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

        binding.favoritesRecyclerView.adapter = recyclerAdapter
        binding.favoritesRecyclerView.layoutManager =
            GridLayoutManager(context, resources.getInteger(R.integer.recipes_recycle_span_count))

        model.loadStateLiveData.observe(viewLifecycleOwner) { renderMealsList(it) }
        model.onViewCreated()
    }

    override fun renderMealsList(state: LoadState<List<MealShortEntity>>) {
        when (state) {
            is LoadState.Success -> {
                recyclerAdapter.updateList(state.value)
            }
            is LoadState.Error -> {
                showErrorMessage(requireContext(), state)
            }
            is LoadState.Loading -> {}
        }
    }
}