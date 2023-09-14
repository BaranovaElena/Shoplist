package com.example.shoplist.core.models

import com.example.shoplist.domain.models.Errors

sealed class LoadState<out T: Any> {
    data class Success<out T: Any>(val value: T) : LoadState<T>()
    data class Error(val errorType: Errors, val message: String?) : LoadState<Nothing>()
    object Loading : LoadState<Nothing>()
}