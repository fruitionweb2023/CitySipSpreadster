package com.arp.citysipspreadster.model.deshboard


import com.google.gson.annotations.SerializedName

data class ResponsePromotionList(
    @SerializedName("error")
    val error: Boolean,
    @SerializedName("message")
    val message: String,
    @SerializedName("offer_business_list")
    val offerBusinessList: List<OfferBusiness>
)