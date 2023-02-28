package com.example.stockmarket.viewmodel

import com.example.stockmarket.domain.usecases.GetCompanyListingUseCase
import com.example.stockmarket.presentation.company_listing.CompanyListingViewModel
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.stockmarket.core.CoroutineRule
import com.example.stockmarket.domain.repository.StockRepository
import com.example.stockmarket.presentation.company_listing.CompanyListingEvent
import com.example.stockmarket.repository.FakeStockRepository
import com.example.stockmarket.util.Resource
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

@ExperimentalCoroutinesApi
class CompanyListingViewModelTest {

    @get:Rule
    val testInstantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineRule = CoroutineRule()

    private lateinit var companyListingViewModel: CompanyListingViewModel
    private lateinit var companyListingUseCase: GetCompanyListingUseCase
    private lateinit var stockRepository: StockRepository

    @Before
    fun setUp() {
        stockRepository = FakeStockRepository()
        companyListingUseCase = GetCompanyListingUseCase(stockRepository)
        companyListingViewModel = CompanyListingViewModel(
            companyListingUseCase
        )
    }

    @Test
    fun `GIVEN companyListingUseCase WHEN called THEN should return company listing`() = runTest {
        advanceUntilIdle()
        val result = companyListingViewModel.state.companies
        assertEquals(1, result.size)
    }

    @Test
    fun `GIVEN companyListingUseCase WHEN called with Refresh event should return company listing`() = runTest {
        ///coEvery { companyListingUseCase(true, "") } returns flow { emit(Resource.Success(emptyList())) }
        companyListingViewModel.state = companyListingViewModel.state.copy(
            searchQuery = ""
        )
        companyListingViewModel.onEvent(CompanyListingEvent.Refresh)
        advanceUntilIdle()
        val result = companyListingViewModel.state.companies
        assertEquals(1, result.size)
    }

    @Test
    fun `GIVEN companyListingUseCase WHEN called with Search event should return company listing`() = runTest {
        ///coEvery { companyListingUseCase(false, "tesla") } returns flow { emit(Resource.Success(emptyList())) }
        companyListingViewModel.onEvent(CompanyListingEvent.OnSearchQueryChange("tesla"))
        advanceUntilIdle()
        val result = companyListingViewModel.state.companies
        assertEquals(1, result.size)
    }
}