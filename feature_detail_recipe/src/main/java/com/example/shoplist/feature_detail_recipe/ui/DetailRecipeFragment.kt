package com.example.shoplist.feature_detail_recipe.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.shoplist.core.ui.showErrorMessage
import com.example.shoplist.feature_detail_recipe.R
import com.example.shoplist.domain.models.DetailRecipeEntity
import com.example.shoplist.domain.models.Errors
import com.example.shoplist.feature_detail_recipe.databinding.FragmentDetailRecipeBinding
import com.example.shoplist.feature_detail_recipe.viewModel.DetailsController
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.BottomSheetCallback
import moxy.MvpAppCompatFragment
import moxy.ktx.moxyPresenter
import org.koin.android.ext.android.inject

class DetailRecipeFragment : MvpAppCompatFragment(), DetailsController.View {
    private val binding by viewBinding(FragmentDetailRecipeBinding::bind)
    private val p: DetailsController.Presenter by inject()
    private val presenter by moxyPresenter { p }
    private val ingredientsAdapter = IngredientsAdapter()

    companion object {
        private const val BUNDLE_EXTRA_KEY = "MEAL_ID_KEY"

        fun newInstance(mealId: Int): DetailRecipeFragment {
            val fragment = DetailRecipeFragment()
            val args = Bundle()
            args.putInt(BUNDLE_EXTRA_KEY, mealId)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_detail_recipe, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.getInt(BUNDLE_EXTRA_KEY)?.let { id ->
            presenter.onViewCreated(id)
        }

        binding.detailRecipeIngredientsRecyclerView.apply {
            this.adapter = ingredientsAdapter
            layoutManager = LinearLayoutManager(context)
        }

        setBottomSheetAnimation()
    }

    override fun showRecipe(recipe: DetailRecipeEntity) = with(binding) {
        detailRecipeProgressBar.visibility = View.GONE
        detailRecipeContainer.visibility = View.VISIBLE
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
    }

    override fun showLoading() = with(binding) {
        detailRecipeProgressBar.visibility = View.VISIBLE
        detailRecipeContainer.visibility = View.GONE
    }

    override fun showError(errorType: Errors, message: String?) {
        binding.detailRecipeProgressBar.visibility = View.GONE
        showErrorMessage(requireContext(), errorType, message)
    }

    private fun setBottomSheetAnimation() = BottomSheetBehavior
        .from(binding.bottomSheetFrameLayout)
        .addBottomSheetCallback(object : BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {}

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                binding.detailRecipeBottomsheetArrow.rotation = slideOffset * 180
            }
        })
}