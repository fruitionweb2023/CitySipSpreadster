package com.arp.citysipspreadster.model.deshboard.main


import com.google.gson.annotations.SerializedName

data class ResponseDeshBoardMain(
    @SerializedName("data")
    val `data`: Data,
    @SerializedName("error")
    val error: Boolean,
    @SerializedName("message")
    val message: String
)