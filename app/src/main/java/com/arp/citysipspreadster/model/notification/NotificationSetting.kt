package com.arp.citysipspreadster.model.notification


import com.google.gson.annotations.SerializedName

data class NotificationSetting(
    @SerializedName("cat_id")
    val catId: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("status")
    val status: Int,
    @SerializedName("title")
    val title: String

)