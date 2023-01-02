package com.arp.citysipspreadster.model.notification


import com.google.gson.annotations.SerializedName

data class ResponseNotificationSettings(
    @SerializedName("error")
    val error: Boolean,
    @SerializedName("message")
    val message: String,
    @SerializedName("notification_setting")
    val notificationSetting: List<NotificationSetting>
)