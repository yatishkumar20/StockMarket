package com.example.stockmarket.data.repository

import com.example.stockmarket.data.csv.CSVParser
import com.example.stockmarket.data.csv.IntradayInfoParser
import com.example.stockmarket.data.di.IoDispatcher
import com.example.stockmarket.data.mapper.toCompanyInfo
import com.example.stockmarket.data.remote.StockApi
import com.example.stockmarket.domain.error.ErrorHandler
import com.example.stockmarket.domain.model.CompanyInfo
import com.example.stockmarket.domain.model.CompanyListing
import com.example.stockmarket.domain.model.IntradayInfo
import com.example.stockmarket.util.AppConstants.UNKNOWN_ERROR
import com.example.stockmarket.util.Resource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class StockNetworkDataSourceImpl @Inject constructor(
    private val api: StockApi,
    @IoDispatcher private val dispatcher: CoroutineDispatcher,
    private val companyListingParser: CSVParser<CompanyListing>,
    private val intradayInfoParser: CSVParser<IntradayInfo>,
    private val errorHandler: ErrorHandler
) : StockNetworkDataSource {
    override suspend fun getCompanyListing(): Resource<List<CompanyListing>> =
        withContext(dispatcher) {
            val remoteListing = try {
                val response = api.getListings()
                companyListingParser.parse(response.byteStream())
            } catch (e: Exception) {
                return@withContext Resource.Error(
                    message = e.localizedMessage ?: UNKNOWN_ERROR,
                    errorEntity = errorHandler.getError(e)
                )
            }
            return@withContext Resource.Success(remoteListing)
        }

    override suspend fun getIntradayInfo(symbol: String): Resource<List<IntradayInfo>> =
        withContext(dispatcher) {
            val result = try {
                val response = api.getIntradayInfo(symbol = symbol)
                intradayInfoParser.parse(response.byteStream())
            } catch (e: Exception) {
                return@withContext Resource.Error(
                    message = e.localizedMessage ?: UNKNOWN_ERROR,
                    errorEntity = errorHandler.getError(e)
                )
            }
            return@withContext Resource.Success(data = result)
        }


    override suspend fun getCompanyInfoInfo(symbol: String): Resource<CompanyInfo> =
        withContext(dispatcher) {
            val result = try {
               api.getCompanyInfo(symbol = symbol).toCompanyInfo()
            } catch (e: Exception) {
                return@withContext Resource.Error(
                    message = e.localizedMessage ?: UNKNOWN_ERROR,
                    errorEntity = errorHandler.getError(e)
                )
            }
            return@withContext Resource.Success(data = result)
        }
}