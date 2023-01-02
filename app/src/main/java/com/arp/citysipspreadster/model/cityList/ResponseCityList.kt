package com.arp.citysipspreadster.model.cityList


import com.google.gson.annotations.SerializedName

data class ResponseCityList(
    @SerializedName("category_list")
    val categoryList: List<Category>,
    @SerializedName("error")
    val error: Boolean,
    @SerializedName("message")
    val message: String
)