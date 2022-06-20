package com.vkhooda24.coroutinesinaction

import androidx.lifecycle.LiveData
import com.vkhooda24.coroutinesinaction.service.Country

interface MainViewModel {

    val countriesListLiveData: LiveData<List<Country>>
    val countryDetailsLiveData: LiveData<Country>

    fun getCountriesList()
    fun getCountryDetails()
    fun getCountriesListAndDetailsWithZipOperator(countryName: String)
    fun getCountriesListAndDetailsAsynchronously(countryName: String)
}