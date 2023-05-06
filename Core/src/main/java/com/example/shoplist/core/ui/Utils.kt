package com.example.shoplist.core.ui

import android.content.Context
import android.view.View
import android.widget.Toast
import com.example.shoplist.core.R
import com.example.shoplist.domain.models.Errors

fun showErrorMessage(context: Context, errorType: Errors, errorMessage: String? = null) {
    Toast.makeText(
        context,
        errorMessage ?: context.getString(
            when (errorType) {
                Errors.SERVER_ERROR -> { R.string.server_error_message }
                Errors.LOAD_ERROR -> { R.string.load_error_message }
            }
        ),
        Toast.LENGTH_LONG
    ).show()
}

fun View.setVisibility(isVisible: Boolean) {
    if (isVisible) this.visibility = View.VISIBLE
    else this.visibility = View.GONE
}
