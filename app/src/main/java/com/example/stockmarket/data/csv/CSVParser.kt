package com.example.stockmarket.data.csv

import java.io.InputStream

interface CSVParser<T> {
    suspend fun parse(input: InputStream) : List<T>
}