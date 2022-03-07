package com.example.shoplist.ui.recipe

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.shoplist.App
import com.example.shoplist.R
import com.example.shoplist.data.Filters
import com.example.shoplist.data.MealsEntity
import com.example.shoplist.databinding.FragmentRecipeFilterBinding
import com.example.shoplist.domain.LoadingMealRepo
import com.example.shoplist.domain.LoadingMealRetrofitImpl
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RecipeFilterFragment : Fragment(R.layout.fragment_recipe_filter) {
    private val binding by viewBinding(FragmentRecipeFilterBinding::bind)
    private val app by lazy { requireActivity().application as App }
    private lateinit var loadingMealRepo: LoadingMealRepo

    companion object {
        fun newInstance() = RecipeFilterFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val app: App = requireActivity().application as App

        loadingMealRepo = LoadingMealRetrofitImpl(app.retrofitService)

        binding.recipeFilterChipGroup.setOnCheckedChangeListener { _, checkedId ->
            val filter: Filters
            when (checkedId) {
                R.id.recipe_filter_chip_category -> { getMeal(Filters.CATEGORY) }
                R.id.recipe_filter_chip_area -> {getMeal(Filters.AREA)}
            }
            //todo viewModel.chipChecked(filter)
        }
    }

    private fun getMeal(filter: Filters) {
        val callback = object : Callback<MealsEntity> {
            override fun onResponse(call: Call<MealsEntity>, response: Response<MealsEntity>) {
                val res = response.body()?.meals
                Toast.makeText(context, res?.get(0)?.title, Toast.LENGTH_SHORT).show()
            }

            override fun onFailure(call: Call<MealsEntity>, t: Throwable) {
                Toast.makeText(context, t.message, Toast.LENGTH_SHORT).show()
            }
        }
        loadingMealRepo.getMealsByFilter(filter, "Seafood", callback)
    }
}