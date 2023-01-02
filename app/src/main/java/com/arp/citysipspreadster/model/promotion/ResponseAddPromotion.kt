package com.arp.citysipspreadster.model.promotion


import com.google.gson.annotations.SerializedName

data class ResponseAddPromotion(
    @SerializedName("error")
    val error: Boolean,
    @SerializedName("message")
    val message: String
)