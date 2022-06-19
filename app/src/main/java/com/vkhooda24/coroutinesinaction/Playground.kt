package com.vkhooda24.coroutinesinaction

import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.flow

suspend fun main() {

    val disposable = CompositeDisposable()
    val observable = Observable.create<String> { emitter -> emitter.onNext("Hello, RxSpace") }
    disposable.add(observable.subscribe({
        println(it)
    }, { t -> println(t.message) }))

    flow {
        emit("Flow space")
    }.collect { println(it) }

    coroutineScope {
        println("Coroutines space")
    }


    //Key and Element interface

    //Coroutine scope

    //Lifecycle

    //Continuation

    //Builders
    //Async
    //Launch

    //Suspend functions
    //withContext
    //coroutineScope

    //Dispatchers
    //Main
    //Default & IO

    //Exception handling

    //State management

    //RxJava vs Coroutines use case example

    //QA

}