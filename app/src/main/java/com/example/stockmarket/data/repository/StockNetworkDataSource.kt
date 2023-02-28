package com.example.stockmarket.data.repository

import com.example.stockmarket.domain.model.CompanyInfo
import com.example.stockmarket.domain.model.CompanyListing
import com.example.stockmarket.domain.model.IntradayInfo
import com.example.stockmarket.util.Resource

interface StockNetworkDataSource {
    suspend fun getCompanyListing() : Resource<List<CompanyListing>>
    suspend fun getIntradayInfo(symbol: String) : Resource<List<IntradayInfo>>
    suspend fun getCompanyInfoInfo(symbol: String) : Resource<CompanyInfo>
}