package com.vkhooda24.coroutinesinaction

import com.vkhooda24.coroutinesinaction.service.Country
import io.reactivex.Single
import retrofit2.http.Path

interface MainRepo {

    fun getAllCountriesList(): Single<List<Country>>
    fun getCountryDetails(@Path("countryName") countryName: String): Single<List<Country>>

    suspend fun getCountriesList(): List<Country>
    suspend fun getGivenCountryDetails(@Path("countryName") countryName: String): List<Country>
}