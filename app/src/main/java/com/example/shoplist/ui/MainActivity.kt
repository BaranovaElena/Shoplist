package com.example.shoplist.ui

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.shoplist.ui.detail.DetailRecipeFragment
import com.example.shoplist.R
import com.example.shoplist.databinding.ActivityMainBinding
import com.example.shoplist.ui.favorites.FavoritesFragment
import com.example.shoplist.ui.recipe.RecipeFilterFragment
import com.example.shoplist.ui.recipe.RecipesFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

private const val SHARED_PREFERENCES_NAME = "settings"

class MainActivity : AppCompatActivity(), SettingsFragment.Controller, FavoritesFragment.Contract, RecipeFilterFragment.Contract {
    private val binding by viewBinding(ActivityMainBinding::bind , R.id.activity_container)
    private val bottomNavigationView: BottomNavigationView by lazy { binding.bottomNavigationView }
    private lateinit var sharedPreferences: SharedPreferences
    private val sharedValueNameTheme = "Theme"
    private var currentTheme = Themes.DEFAULT.ordinal

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
        if (sharedPreferences.contains(sharedValueNameTheme)) {
            currentTheme = sharedPreferences.getInt(sharedValueNameTheme, Themes.DEFAULT.ordinal)
            setAppTheme(currentTheme)
        }
        setContentView(R.layout.activity_main)

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
                openFragment(SettingsFragment.newInstance(currentTheme), true)
                true
            }
            else -> false
        }
    }

    override fun saveTheme(theme: Int) {
        saveThemeInSharedPreferences(theme)

        setAppTheme(theme)
        recreate()
    }

    private fun setAppTheme(theme: Int) {
        this.setTheme(
            when (theme) {
                Themes.DEFAULT.ordinal -> R.style.Theme_Shoplist
                Themes.INDIGO.ordinal -> R.style.Theme_Shoplist_Indigo
                Themes.ORANGE.ordinal -> R.style.Theme_Shoplist_Orange
                else -> R.style.Theme_Shoplist
            }
        )
    }

    private fun saveThemeInSharedPreferences(theme: Int) {
        sharedPreferences.edit().apply {
            putInt(sharedValueNameTheme, theme)
            apply()
        }
    }

    override fun removeSettingsFragmentFromBackStack() {
        supportFragmentManager.popBackStack()
    }

    override fun openDetailScreen(mealId: Int) {
        openFragment(DetailRecipeFragment.newInstance(mealId), true)
    }
}