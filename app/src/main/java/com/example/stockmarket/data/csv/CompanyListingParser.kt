package com.example.stockmarket.data.csv

import com.example.stockmarket.data.di.IoDispatcher
import com.example.stockmarket.domain.model.CompanyListing
import com.opencsv.CSVReader
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.io.InputStreamReader
import javax.inject.Inject

class CompanyListingParser @Inject constructor(
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : CSVParser<CompanyListing> {
    override suspend fun parse(input: InputStream): List<CompanyListing> {
        val csvReader = CSVReader(InputStreamReader(input))
        return withContext(dispatcher) {
            csvReader.readAll()
                .drop(1)
                .mapNotNull { line ->
                    val symbol = line.getOrNull(0)
                    val name = line.getOrNull(1)
                    val exchange = line.getOrNull(2)
                    CompanyListing(
                        symbol = symbol ?: return@mapNotNull null,
                        name = name ?: return@mapNotNull null,
                        exchange = exchange ?: return@mapNotNull null
                    )
                }
                .also {
                    csvReader.close()
                }
        }

    }

}