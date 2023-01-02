package com.arp.citysipspreadster.model.deshboard


import com.google.gson.annotations.SerializedName

data class ResponsePromotionCategory(
    @SerializedName("category_list")
    val categoryList: List<Category>,
    @SerializedName("error")
    val error: Boolean,
    @SerializedName("message")
    val message: String
)