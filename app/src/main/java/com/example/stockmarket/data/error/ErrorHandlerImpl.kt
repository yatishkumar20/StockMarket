package com.example.stockmarket.data.error

import com.example.stockmarket.domain.common.ErrorEntity
import com.example.stockmarket.domain.error.ErrorHandler
import retrofit2.HttpException
import java.io.IOException
import java.net.HttpURLConnection
import javax.inject.Inject

class ErrorHandlerImpl @Inject constructor() : ErrorHandler {
    override fun getError(throwable: Throwable): ErrorEntity {
        return when (throwable) {
            is IOException -> ErrorEntity.Network
            is HttpException -> {
                when (throwable.code()) {
                    HttpURLConnection.HTTP_NOT_FOUND -> ErrorEntity.NotFound
                    HttpURLConnection.HTTP_FORBIDDEN -> ErrorEntity.AccessDenied
                    HttpURLConnection.HTTP_UNAVAILABLE -> ErrorEntity.ServiceUnavailable
                    else -> ErrorEntity.Network
                }
            }
            else -> ErrorEntity.Unknown
        }
    }
}