package com.example.stockmarket.domain.error

import com.example.stockmarket.domain.common.ErrorEntity

interface ErrorHandler {
    fun getError(throwable: Throwable) : ErrorEntity
}