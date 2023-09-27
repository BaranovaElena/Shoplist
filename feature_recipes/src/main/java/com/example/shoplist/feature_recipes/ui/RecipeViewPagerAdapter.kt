package com.example.shoplist.feature_recipes.ui

import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.shoplist.core.ui.FragmentFactory

class RecipeViewPagerAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
    private val fragments: List<FragmentFactory> = emptyList()
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount() = fragments.size
    override fun createFragment(position: Int) = fragments[position].factoryMethod()
}