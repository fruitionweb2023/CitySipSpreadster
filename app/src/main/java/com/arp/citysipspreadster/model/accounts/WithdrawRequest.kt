package com.arp.citysipspreadster.model.accounts


import com.google.gson.annotations.SerializedName

data class WithdrawRequest(
    @SerializedName("amount")
    val amount: String,
    @SerializedName("date_time")
    val dateTime: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("status")
    val status: String
)