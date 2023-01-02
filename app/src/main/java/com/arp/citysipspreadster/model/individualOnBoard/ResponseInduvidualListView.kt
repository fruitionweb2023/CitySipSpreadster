package com.arp.citysipspreadster.model.individualOnBoard


import com.google.gson.annotations.SerializedName

data class ResponseInduvidualListView(
    @SerializedName("error")
    val error: Boolean,
    @SerializedName("message")
    val message: String,
    @SerializedName("onboard_business_lead")
    val onboardBusinessLead: List<OnboardBusinessLead>
)