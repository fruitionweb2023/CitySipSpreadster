package com.arp.citysipspreadster.model.deshboard


import com.google.gson.annotations.SerializedName

data class OfferBusinessSearch(
    @SerializedName("business_id")
    val businessId: String,
    @SerializedName("business_name")
    val businessName: String,
    @SerializedName("business_offer_id")
    val businessOfferId: String,
    @SerializedName("percentage")
    val percentage: String,
    @SerializedName("title")
    val title: String
)