package com.example.stockmarket.domain.usecases

import com.example.stockmarket.domain.repository.StockRepository
import javax.inject.Inject

class GetIntradayInfoUseCase @Inject constructor(
    private val repository: StockRepository
) {
    suspend operator fun invoke(symbol: String) =
        repository.getIntradayInfo(symbol)
}