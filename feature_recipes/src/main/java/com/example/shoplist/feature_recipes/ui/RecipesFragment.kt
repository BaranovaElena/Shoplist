package com.example.shoplist.feature_recipes.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.shoplist.core.ui.FragmentFactory
import com.example.shoplist.feature_recipes.R
import com.example.shoplist.feature_recipes.databinding.FragmentRecipesBinding
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

        with(binding) {
            recipesViewPager.adapter = RecipeViewPagerAdapter(
                fragmentManager = childFragmentManager,
                lifecycle = viewLifecycleOwner.lifecycle,
                fragments = fragments
            )

            TabLayoutMediator(recipesTabLayout, recipesViewPager) { tab, pos ->
                tab.text = fragments[pos].title
            }.attach()
        }
    }
}