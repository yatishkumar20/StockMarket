package com.example.stockmarket.domain.usecases

import com.example.stockmarket.domain.model.CompanyInfo
import com.example.stockmarket.domain.repository.StockRepository
import com.example.stockmarket.util.Resource
import javax.inject.Inject

class GetCompanyInfoUseCase @Inject constructor(
    private val repository: StockRepository
) {
    suspend operator fun invoke(symbol: String): Resource<CompanyInfo> =
        repository.getCompanyInfo(symbol)
}