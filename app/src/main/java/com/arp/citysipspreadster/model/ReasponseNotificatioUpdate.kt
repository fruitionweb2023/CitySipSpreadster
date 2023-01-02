package com.arp.citysipspreadster.model


import com.google.gson.annotations.SerializedName

data class ReasponseNotificatioUpdate(
    @SerializedName("error")
    val error: Boolean,
    @SerializedName("message")
    val message: String,
    @SerializedName("notification_setting")
    val notificationSetting: List<NotificationSetting>
)