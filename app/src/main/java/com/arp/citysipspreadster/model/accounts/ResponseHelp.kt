package com.arp.citysipspreadster.model.accounts


import com.google.gson.annotations.SerializedName

data class ResponseHelp(
    @SerializedName("error")
    val error: Boolean,
    @SerializedName("help_list")
    val helpList: List<Help>,
    @SerializedName("message")
    val message: String
)