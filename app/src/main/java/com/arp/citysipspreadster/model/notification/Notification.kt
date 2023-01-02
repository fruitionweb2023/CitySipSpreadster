package com.arp.citysipspreadster.model.notification


import com.google.gson.annotations.SerializedName

data class Notification(
    @SerializedName("cat_id")
    val catId: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("notification")
    val notification: String,
    @SerializedName("sid")
    val sid: String,
    @SerializedName("text_color")
    val textColor: String
)