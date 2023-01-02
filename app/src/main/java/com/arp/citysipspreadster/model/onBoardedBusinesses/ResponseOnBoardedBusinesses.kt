package com.arp.citysipspreadster.model.onBoardedBusinesses


import com.google.gson.annotations.SerializedName

data class ResponseOnBoardedBusinesses(
    @SerializedName("error")
    val error: Boolean,
    @SerializedName("message")
    val message: String,
    @SerializedName("onboarded_business1")
    val onboardedBusiness1: List<OnboardedBusiness1>,
    @SerializedName("onboarded_business2")
    val onboardedBusiness2: List<OnboardedBusiness1>,
    @SerializedName("onboarded_business3")
    val onboardedBusiness3: List<OnboardedBusiness1>,
    @SerializedName("onboarded_business4")
    val onboardedBusiness4: List<OnboardedBusiness1>
)