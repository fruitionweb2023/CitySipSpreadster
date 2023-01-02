package com.arp.citysipspreadster.model.promotion


import com.google.gson.annotations.SerializedName

data class Category(
    @SerializedName("bg_color")
    val bgColor: String,
    @SerializedName("count")
    val count: Int,
    @SerializedName("id")
    val id: String,
    @SerializedName("image")
    val image: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("row_order")
    val rowOrder: String,
    @SerializedName("status")
    val status: String
)