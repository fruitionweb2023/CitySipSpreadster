package com.arp.citysipspreadster.model.massOnboarding


import com.google.gson.annotations.SerializedName

data class ResponsemassOnboardingCategory(
    @SerializedName("error")
    val error: Boolean,
    @SerializedName("message")
    val message: String,
    @SerializedName("onboard_category_list")
    val onboardCategoryList: List<OnboardCategory>
)