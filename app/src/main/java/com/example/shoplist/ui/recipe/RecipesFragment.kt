package com.example.shoplist.ui.recipe

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.shoplist.R
import com.example.shoplist.databinding.FragmentRecipesBinding
import com.example.shoplist.ui.FragmentFactory
import com.google.android.material.tabs.TabLayoutMediator

class RecipesFragment : Fragment(R.layout.fragment_recipes) {
    private val binding by viewBinding(FragmentRecipesBinding::bind)

    companion object {
        fun newInstance() = RecipesFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fragments = listOf(
            FragmentFactory(getString(R.string.recipe_tab_filter)) { RecipeFilterFragment.newInstance() },
            FragmentFactory(getString(R.string.recipe_tab_search)) { RecipeSearchFragment.newInstance() }
        )

        with(binding.weatherViewPager) {
            adapter = RecipeViewPagerAdapter(requireActivity(), fragments)
        }

        TabLayoutMediator(binding.weatherTabLayout, binding.weatherViewPager) { tab, pos ->
            tab.text = fragments[pos].title
        }.attach()
    }
}