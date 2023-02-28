package com.example.stockmarket.data.repository

import com.example.stockmarket.data.mapper.toCompanyListingEntity
import com.example.stockmarket.domain.common.ErrorEntity
import com.example.stockmarket.domain.model.CompanyInfo
import com.example.stockmarket.domain.model.CompanyListing
import com.example.stockmarket.domain.model.IntradayInfo
import com.example.stockmarket.domain.repository.StockRepository
import com.example.stockmarket.util.AppConstants.UNKNOWN_ERROR
import com.example.stockmarket.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class StockRepositoryImpl @Inject constructor(
    private val localDataSource: StockLocalDataSource,
    private val networkDataSource: StockNetworkDataSource,
) : StockRepository {
    override suspend fun getCompanyListing(
        fetchFromRemote: Boolean,
        query: String
    ): Flow<Resource<List<CompanyListing>>> {
        return flow {
            emit(Resource.Loading(true))
            val localListing = localDataSource.searchCompanyListing(query)
            when (localListing) {
                is Resource.Success -> emit(Resource.Success(data = localListing.data))
                is Resource.Error -> emit(
                    Resource.Error(
                        message = localListing.message,
                        errorEntity = localListing.errorEntity
                    )
                )
                else -> emit(
                    Resource.Error(
                        data = null,
                        message = UNKNOWN_ERROR,
                        errorEntity = ErrorEntity.Unknown
                    )
                )
            }

            val isDbEmpty = localListing.data?.isEmpty() ?: true && query.isBlank()
            val shouldLoadFromCache = !isDbEmpty && !fetchFromRemote
            if (shouldLoadFromCache) {
                emit(Resource.Loading(false))
                return@flow
            }

            when (val networkList = networkDataSource.getCompanyListing()) {
                is Resource.Success -> {
                    networkList.data?.let { list ->
                        localDataSource.clearCompanyListing()
                        localDataSource.insertCompanyListing(list.map { it.toCompanyListingEntity() })
                        Resource.Success(
                            data = localDataSource.searchCompanyListing("").data
                        )

                        emit(Resource.Loading(false))
                    }
                }

                is Resource.Error -> emit(
                    Resource.Error(
                        message = networkList.message,
                        data = networkList.data,
                        errorEntity = networkList.errorEntity
                    )
                )

                else -> emit(
                    Resource.Error(
                        message = UNKNOWN_ERROR,
                        errorEntity = ErrorEntity.Unknown
                    )
                )
            }
        }
    }

    override suspend fun getIntradayInfo(symbol: String): Resource<List<IntradayInfo>> {
        return networkDataSource.getIntradayInfo(symbol)
    }

    override suspend fun getCompanyInfo(symbol: String): Resource<CompanyInfo> {
        return networkDataSource.getCompanyInfoInfo(symbol)
    }

}