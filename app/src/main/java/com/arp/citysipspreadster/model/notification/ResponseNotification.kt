package com.arp.citysipspreadster.model.notification


import com.google.gson.annotations.SerializedName

data class ResponseNotification(
    @SerializedName("error")
    val error: Boolean,
    @SerializedName("message")
    val message: String,
    @SerializedName("notification_list")
    val notificationList: List<Notification>
)