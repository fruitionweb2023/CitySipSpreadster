package com.arp.citysipspreadster.model.promotion


import com.google.gson.annotations.SerializedName

data class ResponseMyPromotion(
    @SerializedName("error")
    val error: Boolean,
    @SerializedName("message")
    val message: String,
    @SerializedName("my_promotion_archive_list")
    val myPromotionArchiveList: List<MyPromotionArchive>,
    @SerializedName("my_promotion_ongoing_list")
    val myPromotionOngoingList: List<MyPromotionOngoing>
)