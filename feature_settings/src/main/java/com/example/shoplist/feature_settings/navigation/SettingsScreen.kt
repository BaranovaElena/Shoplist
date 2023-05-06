package com.example.shoplist.feature_settings.navigation

import com.example.shoplist.feature_settings.ui.SettingsFragment
import com.github.terrakok.cicerone.androidx.FragmentScreen

object SettingsScreen {
    operator fun invoke(theme: Int) = FragmentScreen { SettingsFragment.newInstance(theme) }
}