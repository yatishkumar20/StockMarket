package com.example.stockmarket.presentation.company_info

import com.example.stockmarket.domain.common.ErrorEntity
import com.example.stockmarket.domain.model.CompanyInfo
import com.example.stockmarket.domain.model.IntradayInfo

data class CompanyInfoState(
    val stockInfo : List<IntradayInfo> = emptyList(),
    val company : CompanyInfo? = null,
    val isLoading: Boolean = false,
    val error: ErrorEntity? = null
)
