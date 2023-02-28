package com.example.stockmarket.data.repository

import com.example.stockmarket.data.di.IoDispatcher
import com.example.stockmarket.data.local.CompanyListingEntity
import com.example.stockmarket.data.local.StockDao
import com.example.stockmarket.data.local.StockDataBase
import com.example.stockmarket.data.mapper.toCompanyListing
import com.example.stockmarket.domain.error.ErrorHandler
import com.example.stockmarket.domain.model.CompanyListing
import com.example.stockmarket.util.AppConstants
import com.example.stockmarket.util.Resource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class StockLocalDataSourceImpl @Inject constructor(
    private val db: StockDataBase,
    private val errorHandler: ErrorHandler,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : StockLocalDataSource {

    private val dao: StockDao = db.dao
    override suspend fun searchCompanyListing(query: String): Resource<List<CompanyListing>> =
        withContext(dispatcher) {
            try {
                val companyList = dao.searchCompanyListing(query).map { it.toCompanyListing() }
                return@withContext Resource.Success(companyList)
            } catch (e: Exception) {
                return@withContext getExceptionResult(e)
            }
        }


    override suspend fun insertCompanyListing(companyListingEntities: List<CompanyListingEntity>) {
        withContext(dispatcher) {
            dao.insertCompanyListing(companyListingEntities)
        }
    }

    override suspend fun clearCompanyListing() {
        withContext(dispatcher) {
            dao.clearCompanyListings()
        }
    }

    private fun getExceptionResult(e: Exception): Resource.Error<List<CompanyListing>> {
        return Resource.Error(
            e.localizedMessage ?: AppConstants.UNKNOWN_ERROR,
            errorEntity = errorHandler.getError(e)
        )
    }
}