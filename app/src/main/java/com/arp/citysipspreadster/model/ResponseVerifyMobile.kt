package com.arp.citysipspreadster.model


import com.google.gson.annotations.SerializedName

data class ResponseVerifyMobile(
    @SerializedName("error")
    val error: Boolean,
    @SerializedName("message")
    val message: String,
    @SerializedName("user_id")
    val userId: String
)