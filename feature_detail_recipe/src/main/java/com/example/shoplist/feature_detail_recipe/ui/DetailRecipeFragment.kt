package com.example.shoplist.feature_detail_recipe.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.shoplist.core.models.LoadState
import com.example.shoplist.core.ui.setVisibility
import com.example.shoplist.core.ui.showErrorMessage
import com.example.shoplist.feature_detail_recipe.R
import com.example.shoplist.domain.models.DetailRecipeEntity
import com.example.shoplist.feature_detail_recipe.databinding.FragmentDetailRecipeBinding
import com.example.shoplist.feature_detail_recipe.viewModel.DetailsViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.BottomSheetCallback
import org.koin.androidx.viewmodel.ext.android.viewModel

class DetailRecipeFragment : Fragment(R.layout.fragment_detail_recipe) {
    private val binding by viewBinding(FragmentDetailRecipeBinding::bind)
    private val viewModel: DetailsViewModel by viewModel()
    private val ingredientsAdapter = IngredientsAdapter(::addIngredientToShoplist)
    private val ingredients: MutableMap<Pair<String, String>, Boolean> = mutableMapOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_detail_recipe, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.getInt(BUNDLE_EXTRA_KEY)?.let { id ->
            viewModel.onViewCreated(id)
        }

        binding.detailRecipeIngredientsRecyclerView.apply {
            this.adapter = ingredientsAdapter
            layoutManager = LinearLayoutManager(context)
        }

        setBottomSheetAnimation()

        binding.detailRecipeAddAllButton.setOnClickListener {
            ingredientsAdapter.setAllIngredientsReady()
            addAllToShoplist()
        }
        binding.detailRecipeLikeImageView.setOnClickListener {
            it.isSelected = !it.isSelected
            binding.detailRecipeLikeImageView.setImageResource(
                if (it.isSelected) R.drawable.ic_favorite_full
                else R.drawable.ic_favorite_border
            )
            viewModel.onLikeClicked(it.isSelected)
        }

        viewModel.loadingState.observe(viewLifecycleOwner) { renderLoadingState(it) }
        viewModel.savingState.observe(viewLifecycleOwner) { renderSavingState(it) }
    }

    override fun onDestroyView() {
        binding.detailRecipeIngredientsRecyclerView.adapter = null
        super.onDestroyView()
    }

    private fun renderSavingState(state: LoadState<Pair<String, String>>) = when (state) {
        is LoadState.Error -> showErrorMessage(binding.root.context, state.errorType, state.message)
        is LoadState.Success -> showIngredientAdded(state.value)
        else -> {}
    }

    private fun renderLoadingState(state: LoadState<Pair<DetailRecipeEntity, Boolean>>) = with(binding) {
        detailRecipeProgressBar.setVisibility(state is LoadState.Loading)
        detailRecipeContainer.setVisibility(state !is LoadState.Loading)

        when (state) {
            is LoadState.Error -> showErrorMessage(root.context, state.errorType, state.message)
            is LoadState.Success -> showRecipe(state.value.first, state.value.second)
            else -> {}
        }
    }

    private fun showRecipe(recipe: DetailRecipeEntity, isFavorite: Boolean) = with(binding) {
        recipe.ingredients.forEach {
            ingredients[it.toPair()] = false
        }

        detailRecipeTitleTextView.text = recipe.title

        Glide
            .with(detailRecipeImageView.context)
            .load(recipe.imageUrl)
            .error(R.drawable.image_placeholder)
            .transform(
                RoundedCorners(
                    resources.getDimensionPixelSize(R.dimen.recipe_item_image_corner_radius)
                )
            )
            .into(detailRecipeImageView)

        detailRecipeAreaValueTextView.text = recipe.area
        detailRecipeCategoryValueTextView.text = recipe.category
        detailRecipeVideoTextView.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(recipe.videoUrl)))
        }
        detailRecipeInstructionsTextView.text = recipe.instructions
        ingredientsAdapter.updateData(recipe.ingredients)
        if (isFavorite)
            detailRecipeLikeImageView.setImageResource(R.drawable.ic_favorite_full)
    }

    private fun showIngredientAdded(ingredient: Pair<String, String>) {
        ingredients[ingredient] = true
    }

    private fun setBottomSheetAnimation() = BottomSheetBehavior
        .from(binding.bottomSheetFrameLayout)
        .addBottomSheetCallback(object : BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {}

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                binding.detailRecipeBottomsheetArrow.rotation = slideOffset * 180
            }
        })

    private fun addIngredientToShoplist(ingredient: Pair<String, String>) {
        viewModel.onAddIngredient(ingredient)
    }

    private fun addAllToShoplist() = ingredients.forEach {
        if (!it.value) {
            addIngredientToShoplist(it.key)
        }
    }

    companion object {
        private const val BUNDLE_EXTRA_KEY = "MEAL_ID_KEY"

        fun newInstance(mealId: Int) = DetailRecipeFragment().apply {
            arguments = Bundle().apply { putInt(BUNDLE_EXTRA_KEY, mealId) }
        }
    }
}