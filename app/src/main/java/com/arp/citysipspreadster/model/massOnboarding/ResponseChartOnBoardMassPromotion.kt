package com.arp.citysipspreadster.model.massOnboarding


import com.google.gson.annotations.SerializedName

data class ResponseChartOnBoardMassPromotion(
    @SerializedName("error")
    val error: Boolean,
    @SerializedName("message")
    val message: String,
    @SerializedName("total_count")
    val totalCount: String,
    @SerializedName("onboard_category_chart")
    val onboardCategoryChart: List<OnboardCategoryChart>
)