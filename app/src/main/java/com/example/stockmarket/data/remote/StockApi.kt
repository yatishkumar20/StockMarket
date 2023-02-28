package com.example.stockmarket.data.remote

import com.example.stockmarket.BuildConfig
import com.example.stockmarket.data.remote.dto.CompanyInfoDto
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Query

interface StockApi {

    @GET("query?function=LISTING_STATUS")
    suspend fun getListings(
        @Query("apikey") apiKey: String = BuildConfig.API_KEY
    ): ResponseBody

    @GET("query?function=TIME_SERIES_INTRADAY&interval=60min&datatype=csv")
    suspend fun getIntradayInfo(
        @Query("apikey") apiKey: String = BuildConfig.API_KEY,
        @Query("symbol") symbol: String
    ) : ResponseBody

    @GET("query?function=OVERVIEW")
    suspend fun getCompanyInfo(
        @Query("apikey") apiKey: String = BuildConfig.API_KEY,
        @Query("symbol") symbol: String
    ) : CompanyInfoDto
}