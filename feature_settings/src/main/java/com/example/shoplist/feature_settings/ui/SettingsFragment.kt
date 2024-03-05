package com.example.shoplist.feature_settings.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.shoplist.feature_settings.R
import com.example.shoplist.feature_settings.databinding.FragmentSettingsBinding
import com.example.shoplist.feature_settings.models.Themes

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

        binding.settingsThemesRadioGroup.apply {
            setOnCheckedChangeListener { _, checkedId ->
                val newTheme = when (checkedId) {
                    R.id.settings_themes_radio_coral -> Themes.CORAL.ordinal
                    R.id.settings_themes_radio_red -> Themes.MINT.ordinal
                    else -> Themes.DEFAULT.ordinal
                }

                if (newTheme != arguments?.getInt(BUNDLE_EXTRA_KEY))
                    setNewTheme(checkedId)
            }

            check(
                when (arguments?.getInt(BUNDLE_EXTRA_KEY)) {
                    Themes.DEFAULT.ordinal -> R.id.settings_themes_radio_default
                    Themes.CORAL.ordinal -> R.id.settings_themes_radio_coral
                    Themes.MINT.ordinal -> R.id.settings_themes_radio_red
                    else -> R.id.settings_themes_radio_default
                }
            )
        }
    }

    private fun setNewTheme(checkedId: Int) {
        (requireActivity() as Controller).removeSettingsFragmentFromBackStack()

        (requireActivity() as Controller).saveTheme(
            when (checkedId) {
                R.id.settings_themes_radio_default -> Themes.DEFAULT.ordinal
                R.id.settings_themes_radio_coral -> Themes.CORAL.ordinal
                R.id.settings_themes_radio_red -> Themes.MINT.ordinal
                else -> Themes.DEFAULT.ordinal
            }
        )
    }

    interface Controller {
        fun saveTheme(theme: Int)
        fun removeSettingsFragmentFromBackStack()
    }
}
