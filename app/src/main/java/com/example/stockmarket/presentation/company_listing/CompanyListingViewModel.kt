package com.example.stockmarket.presentation.company_listing

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stockmarket.domain.usecases.GetCompanyListingUseCase
import com.example.stockmarket.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class CompanyListingViewModel @Inject constructor(
    private val getCompanyListing: GetCompanyListingUseCase
) : ViewModel() {

    var state by mutableStateOf(CompanyListingState())
    private var searchJob: Job? = null

    init {
        getCompanyListings()
    }

    fun onEvent(event: CompanyListingEvent) {
        when (event) {
            is CompanyListingEvent.Refresh -> {
                getCompanyListings(fetchFromRemote = true)
            }
            is CompanyListingEvent.OnSearchQueryChange -> {
                state = state.copy(
                    searchQuery = event.query
                )
                searchJob?.cancel()
                searchJob = viewModelScope.launch {
                    delay(500L)
                    getCompanyListings()
                }
            }
        }
    }

    private fun getCompanyListings(
        query: String = state.searchQuery.lowercase(Locale.ROOT),
        fetchFromRemote: Boolean = false
    ) {
        viewModelScope.launch {
            getCompanyListing(fetchFromRemote, query)
                .collect { result ->

                    when (result) {
                        is Resource.Success -> {
                            result.data?.let { list ->
                                state = state.copy(
                                    companies = list
                                )
                            }
                        }

                        is Resource.Error -> Unit

                        is Resource.Loading -> {
                            state = state.copy(
                                isLoading = result.isLoading
                            )
                        }
                    }
                }
        }
    }

}