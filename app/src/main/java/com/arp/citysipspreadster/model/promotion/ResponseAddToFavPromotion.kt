package com.arp.citysipspreadster.model.promotion


import com.google.gson.annotations.SerializedName

data class ResponseAddToFavPromotion(
    @SerializedName("error")
    val error: Boolean,
    @SerializedName("message")
    val message: String
)