package com.example.shoplist.ui

import androidx.fragment.app.Fragment

data class FragmentFactory(
    val title: String,
    val factoryMethod: () -> Fragment
)
