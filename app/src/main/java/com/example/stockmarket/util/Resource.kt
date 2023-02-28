package com.example.stockmarket.util

import com.example.stockmarket.domain.common.ErrorEntity

sealed class Resource<T>(
    val data: T? = null,
    val message: String? = null,
    val errorEntity: ErrorEntity? = null
) {
    class Success<T>(data: T?) : Resource<T>(data)
    class Error<T>(message: String? = null, data: T? = null, errorEntity: ErrorEntity? = null) :
        Resource<T>(data, message, errorEntity)

    class Loading<T>(val isLoading: Boolean = true) : Resource<T>(null)
}
