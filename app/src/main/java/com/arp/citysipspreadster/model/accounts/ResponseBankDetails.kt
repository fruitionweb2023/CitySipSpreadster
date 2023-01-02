package com.arp.citysipspreadster.model.accounts


import com.google.gson.annotations.SerializedName

data class ResponseBankDetails(
    @SerializedName("acc_name")
    val accName: String,
    @SerializedName("acc_number")
    val accNumber: String,
    @SerializedName("bank_name")
    val bankName: String,
    @SerializedName("error")
    val error: Boolean,
    @SerializedName("id")
    val id: String,
    @SerializedName("ifsc_code")
    val ifscCode: String,
    @SerializedName("message")
    val message: String
)