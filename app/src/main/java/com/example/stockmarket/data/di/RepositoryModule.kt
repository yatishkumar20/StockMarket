package com.example.stockmarket.data.di

import com.example.stockmarket.data.csv.CSVParser
import com.example.stockmarket.data.csv.CompanyListingParser
import com.example.stockmarket.data.csv.IntradayInfoParser
import com.example.stockmarket.data.error.ErrorHandlerImpl
import com.example.stockmarket.data.repository.*
import com.example.stockmarket.domain.error.ErrorHandler
import com.example.stockmarket.domain.model.CompanyListing
import com.example.stockmarket.domain.model.IntradayInfo
import com.example.stockmarket.domain.repository.StockRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindStockLocalDataSource(
        stockLocalDataSourceImpl: StockLocalDataSourceImpl
    ) : StockLocalDataSource

    @Binds
    @Singleton
    abstract fun bindStockNetworkDataSource(
        stockNetworkDataSourceImpl: StockNetworkDataSourceImpl
    ) : StockNetworkDataSource

    @Binds
    @Singleton
    abstract fun bindStockRepository(
        stockRepositoryImpl: StockRepositoryImpl
    ) : StockRepository

    @Binds
    @Singleton
    abstract fun bindErrorHandler(
        errorHandlerImpl: ErrorHandlerImpl
    ) : ErrorHandler

    @Binds
    @Singleton
    abstract fun bindCompanyListingParser(
        companyListingParser: CompanyListingParser
    ) : CSVParser<CompanyListing>

    @Binds
    @Singleton
    abstract fun bindIntradayInfoParser(
        intradayInfoParser: IntradayInfoParser
    ) : CSVParser<IntradayInfo>

}