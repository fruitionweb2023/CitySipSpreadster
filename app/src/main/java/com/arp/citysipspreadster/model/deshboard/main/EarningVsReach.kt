package com.arp.citysipspreadster.model.deshboard.main


import com.google.gson.annotations.SerializedName

data class EarningVsReach(
    @SerializedName("day")
    val day: List<String>,
    @SerializedName("earning")
    val earning: List<Int>,
    @SerializedName("reach")
    val reach: List<Int>
)