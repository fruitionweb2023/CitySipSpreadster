package com.arp.citysipspreadster.model.countryList


import com.google.gson.annotations.SerializedName

data class ResponseGetCountry(
    @SerializedName("country_list")
    val countryList: List<Country>,
    @SerializedName("error")
    val error: Boolean,
    @SerializedName("message")
    val message: String
)