package com.arp.citysipspreadster.model.promotion


import com.google.gson.annotations.SerializedName

data class ResponseSharePromotionDetails(
    @SerializedName("conversation_ratio")
    val conversationRatio: Int,
    @SerializedName("date_time")
    val dateTime: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("earning")
    val earning: Int,
    @SerializedName("engagement")
    val engagement: Int,
    @SerializedName("error")
    val error: Boolean,
    @SerializedName("image_video")
    val imageVideo: String,
    @SerializedName("message")
    val message: String,
    @SerializedName("promo_code")
    val promoCode: String,
    @SerializedName("title")
    val title: String,

    @SerializedName("status")
    val status: String,
    @SerializedName("favourite_status")
    val favourite_status: String
)