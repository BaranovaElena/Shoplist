package com.example.shoplist.ui

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.shoplist.R
import com.example.shoplist.databinding.ActivityMainBinding
import com.example.shoplist.feature_favorites.navigation.FavoritesScreen
import com.example.shoplist.feature_recipes.navigation.RecipesScreen
import com.example.shoplist.feature_settings.models.Themes
import com.example.shoplist.feature_settings.navigation.SettingsScreen
import com.example.shoplist.feature_settings.ui.SettingsFragment
import com.example.shoplist.feature_shoplist.navigation.ShoplistScreen
import com.github.terrakok.cicerone.NavigatorHolder
import com.github.terrakok.cicerone.Router
import com.github.terrakok.cicerone.androidx.AppNavigator
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.koin.android.ext.android.inject

private const val SHARED_PREFERENCES_NAME = "settings"

class MainActivity : AppCompatActivity(),
    SettingsFragment.Controller {

    private val navigator by lazy { AppNavigator(this, R.id.container) }
    private val router: Router by inject()
    private val navigatorHolder: NavigatorHolder by inject()

    private val binding by viewBinding(ActivityMainBinding::bind, R.id.activity_container)
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

        bottomNavigationView.apply {
            setOnItemSelectedListener { item -> setNavigation(item) }
            selectedItemId = R.id.nav_recipes
        }
    }

    override fun onResumeFragments() {
        super.onResumeFragments()
        navigatorHolder.setNavigator(navigator)
    }

    override fun onPause() {
        navigatorHolder.removeNavigator()
        super.onPause()
    }

    private fun setNavigation(item: MenuItem): Boolean {
        router.newRootScreen(
            when (item.itemId) {
                R.id.nav_shop_list -> ShoplistScreen()
                R.id.nav_recipes -> RecipesScreen()
                R.id.nav_favorites -> FavoritesScreen()
                else -> RecipesScreen()
            }
        )
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.app_bar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.app_bar_settings -> {
                router.navigateTo(SettingsScreen(currentTheme))
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
}