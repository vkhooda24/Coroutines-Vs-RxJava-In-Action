package com.vkhooda24.coroutinesinaction.service

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path


interface CountriesAPI {
    @GET("all")
    fun getAllCountriesList(): Single<List<Country>>

    @GET("name/{countryName}")
    fun getCountryDetails(@Path("countryName") countryName: String): Single<List<Country>>

    @GET("region/{regionName}")
    fun getRegionsCountries(@Path("regionName") regionName: String): Single<List<Country>>

    @GET("all")
    suspend fun getCountriesList(): List<Country>

    @GET("name/{countryName}")
    suspend fun getGivenCountryDetails(@Path("countryName") countryName: String): List<Country>

}