package com.arp.citysipspreadster.model.promotion


import com.google.gson.annotations.SerializedName

data class MyPromotionOngoing(
    @SerializedName("business_name")
    val businessName: String,
    @SerializedName("business_offer_id")
    val businessOfferId: String,
    @SerializedName("percentage")
    val percentage: String,
    @SerializedName("title")
    val title: String
)