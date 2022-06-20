package com.vkhooda24.coroutinesinaction

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vkhooda24.coroutinesinaction.service.Country
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class MainViewModelRxImpl(private val mainRepo: MainRepo) : ViewModel(), MainViewModel {

    companion object {
        private val TAG: String = MainViewModelCoroutinesImpl::class.java.simpleName
    }

    private val compositeDisposable = CompositeDisposable()
    private val countriesListMutableLiveData = MutableLiveData<List<Country>>()
    private val countryDetailsMutableLiveData = MutableLiveData<Country>()
    override val countriesListLiveData: LiveData<List<Country>> = countriesListMutableLiveData
    override val countryDetailsLiveData: LiveData<Country> = countryDetailsMutableLiveData

    override fun getCountriesList() {
        val dispose = mainRepo.getAllCountriesList()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe({
                postCountriesList(it)
            }, {
                Log.d(TAG, "Exception w/RxJava: $it")
            })
        compositeDisposable.add(dispose)
    }

    override fun getCountryDetails() {
        val dispose = mainRepo.getAllCountriesList()
            .flatMap { countriesList ->
                val countryName = countriesList.getCountryName()
                mainRepo.getCountryDetails(countryName)
            }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe({
                postCountryDetails(it)
            }, {
                Log.d(TAG, "Exception w/RxJava: $it")
            })
        compositeDisposable.add(dispose)
    }

    override fun getCountriesListAndDetailsWithZipOperator(countryName: String) {
        val disposable = mainRepo.getAllCountriesList()
            .zipWith(mainRepo.getCountryDetails(countryName)
            ) { countriesList, countryDetails ->
                Pair(countriesList, countryDetails)
            }.subscribeOn(Schedulers.io())
            .subscribe({
                postCountriesList(it.first)
                postCountryDetails(it.second)
            }, { Log.d(TAG, "Exception w/RxJava ${it.message.toString()}") })

        compositeDisposable.add(disposable)
    }

    override fun getCountriesListAndDetailsAsynchronously(countryName: String) {
        getCountriesList()
        getCountryDetailsWithRx(countryName)
    }

    private fun getCountryDetailsWithRx(countryName: String) {
        val dispose = mainRepo.getCountryDetails(countryName)
            .subscribeOn(Schedulers.io())
            .subscribe({
                postCountryDetails(it)
            }, {
                Log.d(TAG, "Exception w/RxJava ${it.message.toString()}")
            })
        compositeDisposable.add(dispose)
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