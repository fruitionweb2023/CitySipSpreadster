package com.arp.citysipspreadster.model.accounts


import com.google.gson.annotations.SerializedName

data class ResponseWidthrawMoney(
    @SerializedName("error")
    val error: Boolean,
    @SerializedName("message")
    val message: String
)