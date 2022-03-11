package com.example.shoplist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.shoplist.data.DetailRecipeEntity
import com.example.shoplist.data.Errors
import com.example.shoplist.data.LoadState
import com.example.shoplist.databinding.FragmentDetailRecipeBinding
import com.example.shoplist.ui.SettingsFragment
import com.example.shoplist.ui.showErrorMessage
import com.example.shoplist.viewmodel.details.DetailsController
import moxy.MvpAppCompatFragment
import moxy.ktx.moxyPresenter
import org.koin.android.ext.android.inject

class DetailRecipeFragment : MvpAppCompatFragment(), DetailsController.View{
    private val binding by viewBinding(FragmentDetailRecipeBinding::bind)
    private val p: DetailsController.Presenter by inject()
    private val presenter by moxyPresenter { p }

    companion object {
        private const val BUNDLE_EXTRA_KEY = "MEAL_ID_KEY"

        fun newInstance(mealId: Int) : DetailRecipeFragment {
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

        arguments?.getInt(SettingsFragment.BUNDLE_EXTRA_KEY)?.let { id ->
            presenter.onViewCreated(id)
        }
    }

    override fun showRecipe(recipe: DetailRecipeEntity) {
        binding.detailRecipeProgressBar.visibility = View.GONE
        binding.detailRecipeTitleTextView.text = recipe.title
        binding.detailRecipeAreaValueTextView.text = recipe.area
        binding.detailRecipeCategoryValueTextView.text = recipe.category
        binding.detailRecipeVideoTextView.text = recipe.videoUrl
        binding.detailRecipeInstructionsTextView.text = recipe.instructions
    }

    override fun showLoading() {
        binding.detailRecipeProgressBar.visibility = View.VISIBLE
    }

    override fun showError(errorType: Errors, message: String?) {
        binding.detailRecipeProgressBar.visibility = View.GONE
        showErrorMessage(requireContext(), LoadState.Error(errorType, message))
    }
}