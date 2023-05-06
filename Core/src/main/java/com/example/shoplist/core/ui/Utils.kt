package com.example.shoplist.core.ui

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.Toast
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
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

class GlideLoadListener(
    private val onLoadingComplete: () -> Unit = {},
    private val onLoadingFail: (Exception?) -> Unit = {}
) : RequestListener<Drawable> {
    override fun onLoadFailed(
        e: GlideException?,
        model: Any?,
        target: Target<Drawable>?,
        isFirstResource: Boolean
    ): Boolean {
        onLoadingFail(e)
        return false
    }

    override fun onResourceReady(
        resource: Drawable?,
        model: Any?,
        target: Target<Drawable>?,
        dataSource: DataSource?,
        isFirstResource: Boolean
    ): Boolean {
        onLoadingComplete()
        return false
    }
}
