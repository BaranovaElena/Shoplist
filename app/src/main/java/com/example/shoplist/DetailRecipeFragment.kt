package com.example.shoplist

import androidx.fragment.app.Fragment

class DetailRecipeFragment : Fragment(R.layout.fragment_detail_recipe) {

    companion object {
        fun newInstance(param1: String, param2: String) = DetailRecipeFragment()
    }
}