package com.arp.citysipspreadster.model.deshboard.main


import com.google.gson.annotations.SerializedName

data class MyDashboard(
    @SerializedName("active_promotion")
    val activePromotion: String,
    @SerializedName("archive_promotion")
    val archivePromotion: String,
    @SerializedName("inactive_promotion")
    val inactivePromotion: String,
    @SerializedName("invited_business")
    val invitedBusiness: String,
    @SerializedName("new_promotion")
    val newPromotion: String,
    @SerializedName("onboarded_business")
    val onboardedBusiness: String,
    @SerializedName("reminder_business")
    val reminderBusiness: String,
    @SerializedName("responded_business")
    val respondedBusiness: String
)