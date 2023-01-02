package com.arp.citysipspreadster.model.accounts


import com.google.gson.annotations.SerializedName

data class Histroy(
    @SerializedName("amount")
    val amount: String,
    @SerializedName("date_time")
    val dateTime: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("notes")
    val notes: String
)