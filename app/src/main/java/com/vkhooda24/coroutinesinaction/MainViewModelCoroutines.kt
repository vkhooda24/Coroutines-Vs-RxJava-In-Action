package com.vkhooda24.coroutinesinaction

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vkhooda24.coroutinesinaction.service.CountriesAPI
import com.vkhooda24.coroutinesinaction.service.Country
import com.vkhooda24.coroutinesinaction.service.RetrofitBuilder
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
class MainViewModelCoroutines : ViewModel() {

    private val countriesAPI: CountriesAPI by lazy {
        RetrofitBuilder.getInstance().create(CountriesAPI::class.java)
    }
    private val TAG: String = MainViewModelCoroutines::class.java.simpleName

    private val countriesListMutableLiveData = MutableLiveData<List<Country>>()
    val countriesListLiveData: LiveData<List<Country>> = countriesListMutableLiveData


    fun getCountriesListSequentiallyWithCoroutines() {
        viewModelScope.launch(Dispatchers.IO) {
            val countriesList = countriesAPI.getCountriesList()
            val countryName = countriesList.getCountryName()
            val countryDetails = countriesAPI.getGivenCountryDetails(countryName)
        }
    }

    fun getCountriesListAsZipAsynchronouslyWithCoroutines() {

        val errorHandler = CoroutineExceptionHandler { _, t ->
            println(TAG.plus(" Exception: $t"))
        }

        viewModelScope.launch(errorHandler) {
            val countriesList = viewModelScope.async(Dispatchers.IO) {
                countriesAPI.getCountriesList()
            }

            val details = viewModelScope.async(Dispatchers.IO) {
                countriesAPI.getGivenCountryDetails("USA")
            }

            countriesListMutableLiveData.postValue(countriesList.await())
            println("Country details: $details")

        }
    }

    fun getCountriesListAsynchronouslyWithCoroutines() {
        getCountriesListWithCoroutines()
        getCountryDetailWithCoroutines()
    }

    @OptIn(ExperimentalStdlibApi::class)
    fun getCountriesListWithCoroutines() {
        viewModelScope.launch(Dispatchers.IO) {
            val countriesList = countriesAPI.getCountriesList()
            countriesListMutableLiveData.postValue(countriesList)
        }
    }

    private fun getCountryDetailWithCoroutines(countryName: String = "USA") {
        viewModelScope.launch(Dispatchers.IO) {
            val details = countriesAPI.getGivenCountryDetails(countryName)
        }
    }

    suspend fun getCountriesList(): List<Country> {
        return countriesAPI.getCountriesList()
    }
}