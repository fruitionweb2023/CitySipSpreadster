package com.arp.citysipspreadster.model.cityList


import com.google.gson.annotations.SerializedName

data class Category(
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("state_id")
    val stateId: String
)