package com.example.stockmarket.data.di

import com.example.stockmarket.BuildConfig
import com.example.stockmarket.data.remote.StockApi
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    fun provideBaseUrl() = BuildConfig.BASE_URL

    @Provides
    @Singleton
    fun provideRetrofit(): StockApi = Retrofit.Builder().apply {
        baseUrl(BuildConfig.BASE_URL)
        client(
            OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BASIC
                }).build()
        )
        addConverterFactory(
            GsonConverterFactory.create(GsonBuilder().create())
        )
    }.build().create(StockApi::class.java)

}
