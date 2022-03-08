package com.example.shoplist.ui

import android.content.Context
import android.widget.Toast
import com.example.shoplist.R
import com.example.shoplist.data.Errors
import com.example.shoplist.data.LoadState

fun showErrorMessage(context: Context, error: LoadState.Error) {
    Toast.makeText(
        context,
        error.message ?: context.getString(
            when (error.errorType) {
                Errors.SERVER_ERROR -> { R.string.server_error_message }
                Errors.LOAD_ERROR -> { R.string.load_error_message }
            }
        ),
        Toast.LENGTH_SHORT
    ).show()
}

enum class Themes {
    DEFAULT, INDIGO, ORANGE
}