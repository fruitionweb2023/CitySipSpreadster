package com.arp.citysipspreadster.model.stateList


import com.google.gson.annotations.SerializedName

data class ResponseGetStateList(
    @SerializedName("category_list")
    val categoryList: List<Category>,
    @SerializedName("error")
    val error: Boolean,
    @SerializedName("message")
    val message: String
)