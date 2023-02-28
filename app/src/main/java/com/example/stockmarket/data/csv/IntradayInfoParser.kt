package com.example.stockmarket.data.csv

import com.example.stockmarket.data.di.IoDispatcher
import com.example.stockmarket.data.mapper.toIntradayInfo
import com.example.stockmarket.data.remote.dto.IntradayInfoDto
import com.example.stockmarket.domain.model.CompanyListing
import com.example.stockmarket.domain.model.IntradayInfo
import com.opencsv.CSVReader
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.io.InputStreamReader
import java.time.LocalDateTime
import javax.inject.Inject

class IntradayInfoParser @Inject constructor(
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : CSVParser<IntradayInfo> {
    override suspend fun parse(input: InputStream): List<IntradayInfo> {
        val csvReader = CSVReader(InputStreamReader(input))
        return withContext(dispatcher) {
            csvReader.readAll()
                .drop(1)
                .mapNotNull { line ->
                    val time = line.getOrNull(0) ?: return@mapNotNull null
                    val close = line.getOrNull(4) ?: return@mapNotNull null
                    val dto = IntradayInfoDto(
                        timestamp = time,
                        close = close.toDouble())
                    dto.toIntradayInfo()
                }
                .filter {
                    it.date.dayOfMonth == LocalDateTime.now().minusDays(1).dayOfMonth
                }
                .sortedBy {
                    it.date.hour
                }
                .also {
                    csvReader.close()
                }
        }

    }

}