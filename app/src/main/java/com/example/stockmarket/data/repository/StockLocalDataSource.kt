package com.example.stockmarket.data.repository

import com.example.stockmarket.data.local.CompanyListingEntity
import com.example.stockmarket.domain.model.CompanyListing
import com.example.stockmarket.util.Resource

interface StockLocalDataSource {
    suspend fun searchCompanyListing(query: String) : Resource<List<CompanyListing>>
    suspend fun insertCompanyListing(companyListingEntities: List<CompanyListingEntity>)
    suspend fun clearCompanyListing()
}