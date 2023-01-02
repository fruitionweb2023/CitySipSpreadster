package com.arp.citysipspreadster.model.accounts


import com.google.gson.annotations.SerializedName

data class ProfileTrendingOffer(
    @SerializedName("business_id")
    val businessId: String,
    @SerializedName("business_name")
    val businessName: String,
    @SerializedName("business_offer_id")
    val businessOfferId: String,
    @SerializedName("image_video")
    val imageVideo: String,
    @SerializedName("percentage")
    val percentage: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("type")
    val type: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("promo_code")
    val promoCode: String
)