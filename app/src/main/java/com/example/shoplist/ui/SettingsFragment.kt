package com.example.shoplist.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.shoplist.R
import com.example.shoplist.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment(R.layout.fragment_settings) {
    private val binding by viewBinding(FragmentSettingsBinding::bind)

    companion object {
        const val BUNDLE_EXTRA_KEY = "THEME_BUNDLE_EXTRA_KEY"

        fun newInstance(theme: Int): SettingsFragment {
            val fragment = SettingsFragment()
            val args = Bundle()
            args.putInt(BUNDLE_EXTRA_KEY, theme)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.settingsThemesRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            val newTheme = when (checkedId) {
                R.id.settings_themes_radio_indigo -> Themes.INDIGO.ordinal
                R.id.settings_themes_radio_red -> Themes.ORANGE.ordinal
                else -> Themes.DEFAULT.ordinal
            }

            if (newTheme != arguments?.getInt(BUNDLE_EXTRA_KEY))
                setNewTheme(checkedId)
        }

        binding.settingsThemesRadioGroup.check(
            when (arguments?.getInt(BUNDLE_EXTRA_KEY)) {
                Themes.DEFAULT.ordinal -> R.id.settings_themes_radio_default
                Themes.INDIGO.ordinal -> R.id.settings_themes_radio_indigo
                Themes.ORANGE.ordinal -> R.id.settings_themes_radio_red
                else -> R.id.settings_themes_radio_default
            }
        )
    }

    private fun setNewTheme(checkedId: Int) {
        (requireActivity() as Controller).removeSettingsFragmentFromBackStack()

        (requireActivity() as Controller).saveTheme(
            when (checkedId) {
                R.id.settings_themes_radio_default -> Themes.DEFAULT.ordinal
                R.id.settings_themes_radio_indigo -> Themes.INDIGO.ordinal
                R.id.settings_themes_radio_red -> Themes.ORANGE.ordinal
                else -> Themes.DEFAULT.ordinal
            }
        )
    }

    interface Controller {
        fun saveTheme(theme: Int)
        fun removeSettingsFragmentFromBackStack()
    }
}
