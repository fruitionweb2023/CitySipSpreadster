package com.arp.citysipspreadster.model.deshboard


import com.google.gson.annotations.SerializedName

data class ResponsePromotionDetails(
    @SerializedName("business_name")
    val businessName: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("email_id")
    val emailId: String,
    @SerializedName("error")
    val error: Boolean,
    @SerializedName("image")
    val image: String,
    @SerializedName("message")
    val message: String,
    @SerializedName("percentage")
    val percentage: String,
    @SerializedName("phone_no")
    val phoneNo: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("rating")
    val rating: String,
    @SerializedName("address")
    val address: String
)