package com.vkhooda24.coroutinesinaction

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vkhooda24.coroutinesinaction.service.Country
import kotlinx.coroutines.*

class MainViewModelCoroutinesImpl(
    private val mainRepo: MainRepo,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : ViewModel(), MainViewModel {

    companion object {
        private val TAG: String = MainViewModelCoroutinesImpl::class.java.simpleName
    }

    private val countriesListMutableLiveData = MutableLiveData<List<Country>>()
    private val countryDetailsMutableLiveData = MutableLiveData<Country>()
    override val countriesListLiveData: LiveData<List<Country>> = countriesListMutableLiveData
    override val countryDetailsLiveData: LiveData<Country> = countryDetailsMutableLiveData

    override fun getCountriesList() {
        viewModelScope.launch(dispatcher) {
            val countriesList = mainRepo.getCountriesList()
            postCountriesList(countriesList)
        }
    }

    override fun getCountryDetails() {
        viewModelScope.launch(dispatcher) {
            val countriesList = mainRepo.getCountriesList()
            val countryName = countriesList.getCountryName()
            val countryDetails = mainRepo.getGivenCountryDetails(countryName)
            postCountryDetails(countryDetails)
        }
    }

    override fun getCountriesListAndDetailsWithZipOperator(countryName: String) {

        val errorHandler = CoroutineExceptionHandler { _, t ->
            println(TAG.plus(" Exception w/Coroutine $t"))
        }

        viewModelScope.launch(dispatcher + errorHandler) {
            val countriesList = viewModelScope.async {
                mainRepo.getCountriesList()
            }

            val details = viewModelScope.async {
                mainRepo.getGivenCountryDetails(countryName)
            }

            postCountriesList(countriesList.await())
            postCountryDetails(details.await())
        }
    }

    override fun getCountriesListAndDetailsAsynchronously(countryName: String) {
        getCountriesList()
        getCountryDetails(countryName)
    }

    private fun getCountryDetails(countryName: String) {
        viewModelScope.launch(dispatcher) {
            val countryDetails = mainRepo.getGivenCountryDetails(countryName)
            postCountryDetails(countryDetails)
        }
    }

    private fun postCountriesList(countriesList: List<Country>) {
        countriesListMutableLiveData.postValue(countriesList)
    }

    private fun postCountryDetails(details: List<Country>) {
        details.firstOrNull().takeIf { it != null }?.let {
            countryDetailsMutableLiveData.postValue(it)
        }
    }
}