package com.vkhooda24.coroutinesinaction

import com.vkhooda24.coroutinesinaction.service.Country

fun List<Country>.getCountryName() =
    this.find { country -> country.name.common == "USA" }?.name?.common
        ?: "USA"
