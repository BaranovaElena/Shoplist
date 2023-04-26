package com.example.shoplist.feature_recipes.ui

import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.shoplist.core.ui.FragmentFactory

class RecipeViewPagerAdapter(
    fragmentActivity: FragmentActivity,
    private val fragments: List<FragmentFactory> = emptyList()): FragmentStateAdapter(fragmentActivity)  {
    override fun getItemCount() = fragments.size
    override fun createFragment(position: Int) = fragments[position].factoryMethod()
}