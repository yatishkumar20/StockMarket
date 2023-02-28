package com.example.stockmarket.domain.usecases

import com.example.stockmarket.domain.repository.StockRepository
import javax.inject.Inject

class GetCompanyListingUseCase @Inject constructor(
    private val repository: StockRepository
) {
    suspend operator fun invoke(fetchFromRemote: Boolean, query: String) =
        repository.getCompanyListing(fetchFromRemote, query)
}