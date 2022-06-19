package com.vkhooda24.coroutinesinaction

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vkhooda24.coroutinesinaction.service.CountriesAPI
import com.vkhooda24.coroutinesinaction.service.Country
import com.vkhooda24.coroutinesinaction.service.RetrofitBuilder
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
class MainViewModelRx : ViewModel() {

    private val compositeDisposable = CompositeDisposable()
    private val countriesAPI: CountriesAPI by lazy {
        RetrofitBuilder.getInstance().create(CountriesAPI::class.java)
    }
    private val countriesListMutableLiveData = MutableLiveData<List<Country>>()
    val countriesListLiveData: LiveData<List<Country>> = countriesListMutableLiveData

    fun getCountriesListSequentiallyWithRx() {
        val dispose = countriesAPI.getAllCountriesList()
            .map { countriesList ->
                {
                    val countryName = countriesList.getCountryName()
                    getCountryDetailWithRx(countryName)
                }
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe({
                println(it)
            }, {
                println(it)
            })
        compositeDisposable.add(dispose)
    }

    fun getCountriesListAsZipAsynchronouslyWithRx() {
        val disposable = countriesAPI.getAllCountriesList()
            .zipWith(countriesAPI.getCountryDetails("USA")
            ) { countriesList, countryDetails ->
                Pair(countriesList, countryDetails)
            }.subscribe({
                println("Countries List: ${it.first}")
                println("Countries List: ${it.second}")
            }, { Log.d("MainViewModelRx", it.message.toString()) })

        compositeDisposable.add(disposable)
    }

    fun getCountriesListAsynchronouslyWithRx() {
        getCountriesListWithRx()
        getCountryDetailWithRx()
    }

    fun getCountriesListWithRx() {
        val dispose = countriesAPI.getAllCountriesList()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe({
                countriesListMutableLiveData.postValue(it)
            }, {
                println(it)
            })
        compositeDisposable.add(dispose)
    }

    private fun getCountryDetailWithRx(countryName: String = "USA") {
        val dispose = countriesAPI.getCountryDetails(countryName).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe({
                println(it)
            }, {
                println(it)
            })
        compositeDisposable.add(dispose)
    }

    @OptIn(ExperimentalStdlibApi::class)
    fun getCountriesListWithCoroutines() {
        viewModelScope.launch(Dispatchers.IO) {
            countriesAPI.getCountriesList()
        }
    }
}