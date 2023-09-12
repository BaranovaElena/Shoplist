package com.example.shoplist.feature_shoplist.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.shoplist.core.models.LoadState
import com.example.shoplist.core.ui.setVisibility
import com.example.shoplist.core.ui.showErrorMessage
import com.example.shoplist.domain.models.ShoplistEntity
import com.example.shoplist.feature_shoplist.R
import com.example.shoplist.feature_shoplist.databinding.FragmentShoplistBinding
import com.example.shoplist.feature_shoplist.viewModel.ShoplistViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class ShoplistFragment : Fragment(R.layout.fragment_shoplist) {

    private val binding by viewBinding(FragmentShoplistBinding::bind)
    private val recyclerAdapter = ShoplistAdapter(::onItemChecked, ::onItemUnchecked)
    private val viewModel: ShoplistViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            shoplistListRv.apply {
                adapter = recyclerAdapter
                layoutManager = LinearLayoutManager(context)
                addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))

            }
            shoplistSelectAllBtn.setOnClickListener {  }
            shoplistClearSelectedBtn.setOnClickListener {  }
        }

        with(viewModel) {
            loadingLiveData.observe(viewLifecycleOwner) { renderLoadState(it) }
            onViewCreated()
        }
    }

    private fun renderLoadState(state: LoadState<List<ShoplistEntity>>?) = with(binding) {
        shoplistProgressView.setVisibility(state is LoadState.Loading)
        when(state) {
            is LoadState.Error -> showErrorMessage(root.context, state.errorType, state.message)
            is LoadState.Success -> recyclerAdapter.updateData(state.value)
            else -> {}
        }
    }

    private fun onItemUnchecked(ingredientName: String) {
        Toast.makeText(requireContext(), "uncheck $ingredientName", Toast.LENGTH_SHORT).show()
    }

    private fun onItemChecked(ingredientName: String) {
        Toast.makeText(requireContext(), "check $ingredientName", Toast.LENGTH_SHORT).show()
    }

    companion object {
        fun newInstance() = ShoplistFragment()
    }
}