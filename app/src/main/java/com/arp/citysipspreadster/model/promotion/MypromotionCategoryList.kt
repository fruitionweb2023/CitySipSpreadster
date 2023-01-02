package com.arp.citysipspreadster.model.promotion


import com.google.gson.annotations.SerializedName

data class MypromotionCategoryList(
    @SerializedName("category_list")
    val categoryList: List<Category>,
    @SerializedName("error")
    val error: Boolean,
    @SerializedName("message")
    val message: String
)