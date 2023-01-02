package com.arp.citysipspreadster.model.accounts


import com.google.gson.annotations.SerializedName

data class ResponseSpreadsterProfile(
    @SerializedName("error")
    val error: Boolean,
    @SerializedName("message")
    val message: String,
    @SerializedName("profile_offer_list")
    val profileOfferList: List<ProfileOffer>,
    @SerializedName("profile_trending_offer_list")
    val profileTrendingOfferList: List<ProfileTrendingOffer>
)