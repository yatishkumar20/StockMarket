package com.example.stockmarket.presentation.company_info

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stockmarket.domain.usecases.GetCompanyInfoUseCase
import com.example.stockmarket.domain.usecases.GetIntradayInfoUseCase
import com.example.stockmarket.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CompanyInfoViewModel @Inject constructor(
    private val companyInfoUseCase: GetCompanyInfoUseCase,
    private val intradayInfoUseCase: GetIntradayInfoUseCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    var state by mutableStateOf(CompanyInfoState())

    init {
        viewModelScope.launch {
            val symbol = savedStateHandle.get<String>("symbol") ?: return@launch
            state = state.copy(isLoading = true)
            val companyInfo = async { companyInfoUseCase(symbol) }
            val intradayInfo = async { intradayInfoUseCase(symbol) }
            when(val result = companyInfo.await()) {
                is Resource.Success -> {
                    state = state.copy(
                        company = result.data,
                        isLoading = false,
                        error = null
                    )
                }
                is Resource.Error -> {
                    state = state.copy(
                        error = result.errorEntity,
                        isLoading = false,
                        company = null
                    )
                }
                else -> Unit
            }

            when(val result = intradayInfo.await()) {
                is Resource.Success -> {
                    state = state.copy(
                        stockInfo = result.data ?: emptyList(),
                        isLoading = false,
                        error = null
                    )
                }
                is Resource.Error -> {
                    state = state.copy(
                        error = result.errorEntity,
                        isLoading = false,
                        stockInfo = emptyList()
                    )
                }
                else -> Unit
            }
        }

    }

}