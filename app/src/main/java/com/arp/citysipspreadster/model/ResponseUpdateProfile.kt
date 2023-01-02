package com.arp.citysipspreadster.model


import com.google.gson.annotations.SerializedName

data class ResponseUpdateProfile(
    @SerializedName("error")
    val error: Boolean,
    @SerializedName("message")
    val message: String
)