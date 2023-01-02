package com.arp.citysipspreadster.model.accounts


import com.google.gson.annotations.SerializedName

data class ResponseMyWallet(
    @SerializedName("error")
    val error: Boolean,
    @SerializedName("histroy")
    val histroy: List<Histroy>,
    @SerializedName("message")
    val message: String,
    @SerializedName("wallet")
    val wallet: String,
    @SerializedName("withdraw_request")
    val withdrawRequest: List<WithdrawRequest>
)