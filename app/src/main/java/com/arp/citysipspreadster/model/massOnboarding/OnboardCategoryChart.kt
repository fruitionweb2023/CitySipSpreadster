package com.arp.citysipspreadster.model.massOnboarding


import com.google.gson.annotations.SerializedName

data class OnboardCategoryChart(
    @SerializedName("bg_color")
    val bgColor: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("image")
    val image: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("persantage")
    val persantage: String,
    @SerializedName("row_order")
    val rowOrder: String,
    @SerializedName("status")
    val status: String
)