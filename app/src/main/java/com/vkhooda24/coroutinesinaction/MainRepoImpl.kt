package com.vkhooda24.coroutinesinaction

import com.vkhooda24.coroutinesinaction.service.CountriesAPI
import com.vkhooda24.coroutinesinaction.service.Country
import io.reactivex.Single

open class MainRepoImpl(private val countriesAPI: CountriesAPI) : MainRepo {
    override fun getAllCountriesList(): Single<List<Country>> {
        return countriesAPI.getAllCountriesList()
    }

    override fun getCountryDetails(countryName: String): Single<List<Country>> {
        return countriesAPI.getCountryDetails(countryName)
    }

    override suspend fun getCountriesList(): List<Country> =
        countriesAPI.getCountriesList()

    override suspend fun getGivenCountryDetails(countryName: String): List<Country> {
        return countriesAPI.getGivenCountryDetails(countryName)
    }

}