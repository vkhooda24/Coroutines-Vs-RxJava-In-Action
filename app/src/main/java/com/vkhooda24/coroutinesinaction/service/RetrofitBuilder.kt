package com.vkhooda24.coroutinesinaction.service

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitBuilder {

    private const val BASE_URL: String = "https://restcountries.com/v3.1/"

    fun getInstance(): Retrofit = Retrofit.Builder().apply {
        baseUrl(BASE_URL)
        addConverterFactory(GsonConverterFactory.create())
        addCallAdapterFactory(RxJava2CallAdapterFactory.create())
    }.build()
}