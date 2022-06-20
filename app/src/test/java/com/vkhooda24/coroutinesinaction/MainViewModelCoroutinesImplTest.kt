package com.vkhooda24.coroutinesinaction

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.vkhooda24.coroutinesinaction.service.Country
import com.vkhooda24.coroutinesinaction.service.Flags
import com.vkhooda24.coroutinesinaction.service.Languages
import com.vkhooda24.coroutinesinaction.service.Name
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import org.mockito.kotlin.any

@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModelCoroutinesImplTest {

    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: MainViewModel
    private val testDispatcher = UnconfinedTestDispatcher()

    private val mainRepo: MainRepo by lazy {
        Mockito.mock(MainRepoImpl::class.java)
    }

    @Before
    fun setup() {
        viewModel = MainViewModelCoroutinesImpl(mainRepo, testDispatcher)
        //use given test dispatcher
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `fetch countries list test`() = runTest {
        Mockito.`when`(mainRepo.getCountriesList()).thenReturn(getCountriesList())
        viewModel.getCountriesList()

        val countriesList = viewModel.countriesListLiveData.value
        assertEquals("List size", 1, countriesList?.size)
        assertEquals("Country name", "USA", countriesList?.get(0)?.name?.common)
    }

    @Test
    fun `fetch country details test`() = runTest {
        Mockito.`when`(mainRepo.getCountriesList()).thenReturn(getCountriesList())
        Mockito.`when`(mainRepo.getGivenCountryDetails(any())).thenReturn(getCountriesList())

        viewModel.getCountryDetails()

        val countryDetails = viewModel.countryDetailsLiveData.value
        assertEquals("Country name", "USA", countryDetails?.name?.common)
        assertEquals("Country population", 332_000_000, countryDetails?.population)
    }


    private fun getCountriesList() = listOf(Country(Name("USA", "USA"),
        listOf("Washington D.C."),
        "Pacific",
        "",
        Languages("", "Eng", ""),
        listOf(),
        listOf(),
        332_000_000,
        Flags("", "")))
}