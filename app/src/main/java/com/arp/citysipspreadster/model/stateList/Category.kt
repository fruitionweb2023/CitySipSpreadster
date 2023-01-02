package com.arp.citysipspreadster.model.stateList


import com.google.gson.annotations.SerializedName

data class Category(
    @SerializedName("country_id")
    val countryId: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String
)