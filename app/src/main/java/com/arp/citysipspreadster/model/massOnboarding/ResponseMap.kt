package com.arp.citysipspreadster.model.massOnboarding


import com.google.gson.annotations.SerializedName

data class ResponseMap(
    @SerializedName("error")
    val error: Boolean,
    @SerializedName("message")
    val message: String,
    @SerializedName("onboard_business_lead")
    val onboardBusinessLead: List<OnboardBusinessLead>
)