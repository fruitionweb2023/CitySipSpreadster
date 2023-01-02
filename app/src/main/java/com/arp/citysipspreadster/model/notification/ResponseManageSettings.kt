package com.arp.citysipspreadster.model.notification


import com.google.gson.annotations.SerializedName

data class ResponseManageSettings(
    @SerializedName("error")
    val error: Boolean,
    @SerializedName("message")
    val message: String
)