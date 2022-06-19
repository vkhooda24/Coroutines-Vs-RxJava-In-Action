package com.vkhooda24.coroutinesinaction.service

import com.google.gson.annotations.SerializedName

data class Country(
    val name: Name,
    val capital: List<String>,
    val region: String,
    val subregion: String,
    val languages: Languages,
    val borders: List<String>,
    val continents: List<String>,
    val population: Number,
    val flags: Flags,
)

data class Name(
    var common: String,
    val official: String,
)

data class Languages(
    @SerializedName("spa") val spa: String,
    @SerializedName("eng") val eng: String,
    @SerializedName("hin") val hin: String,
)

data class Flags(
    @SerializedName("svg") val svg: String,
    @SerializedName("png") val png: String,
)