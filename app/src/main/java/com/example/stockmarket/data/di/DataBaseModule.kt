package com.example.stockmarket.data.di

import android.content.Context
import androidx.room.Room
import com.example.stockmarket.data.local.StockDao
import com.example.stockmarket.data.local.StockDataBase
import com.example.stockmarket.util.AppConstants.STOCK_DB
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataBaseModule {

    @Singleton
    @Provides
    fun provideDataBase(
        @ApplicationContext app: Context
    ): StockDataBase {
        return Room.databaseBuilder(
            app,
            StockDataBase::class.java,
            STOCK_DB
        )
            .build()
    }

    @Singleton
    @Provides
    fun provideDao(db: StockDataBase): StockDao {
        return db.dao
    }

}