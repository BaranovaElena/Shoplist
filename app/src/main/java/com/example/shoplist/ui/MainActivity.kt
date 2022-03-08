package com.example.shoplist.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.shoplist.R
import com.example.shoplist.databinding.ActivityMainBinding
import com.example.shoplist.ui.favorites.FavoritesFragment
import com.example.shoplist.ui.recipe.RecipesFragment
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity(R.layout.activity_main) {
    private val binding by viewBinding(ActivityMainBinding::bind , R.id.activity_container)
    private val bottomNavigationView: BottomNavigationView by lazy { binding.bottomNavigationView }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bottomNavigationView.setOnItemSelectedListener { item -> setNavigation(item) }
        bottomNavigationView.selectedItemId = R.id.nav_recipes
    }

    private fun setNavigation(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_shop_list -> openFragment(ShoplistFragment.newInstance())
            R.id.nav_recipes -> openFragment(RecipesFragment.newInstance())
            R.id.nav_favorites -> openFragment(FavoritesFragment.newInstance())
        }
        return true
    }

    private fun openFragment(fragmentInstance: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, fragmentInstance)
            .commitNow()
    }

}