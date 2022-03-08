package com.example.shoplist.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.shoplist.R
import com.example.shoplist.SettingsFragment
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
            R.id.nav_shop_list -> openFragment(ShoplistFragment.newInstance(), false)
            R.id.nav_recipes -> openFragment(RecipesFragment.newInstance(), false)
            R.id.nav_favorites -> openFragment(FavoritesFragment.newInstance(), false)
        }
        return true
    }

    private fun openFragment(fragmentInstance: Fragment, backstack: Boolean) {
        if (backstack) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, fragmentInstance)
                .addToBackStack(null)
                .commit()
        } else {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, fragmentInstance)
                .commitNow()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.app_bar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.app_bar_settings -> {
                openFragment(SettingsFragment.newInstance(), true)
                true
            }
            else -> false
        }
    }
}