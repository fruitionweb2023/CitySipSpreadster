package com.arp.citysipspreadster.model.accounts


import com.google.gson.annotations.SerializedName

data class Help(
    @SerializedName("description")
    val description: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("title")
    val title: String
)