package com.arp.citysipspreadster.model


import com.google.gson.annotations.SerializedName

data class ResponseNewBusiness(
    @SerializedName("error")
    val error: Boolean,
    @SerializedName("message")
    val message: String
)