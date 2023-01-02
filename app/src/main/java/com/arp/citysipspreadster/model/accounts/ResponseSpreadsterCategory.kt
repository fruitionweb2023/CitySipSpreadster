package com.arp.citysipspreadster.model.accounts


import com.google.gson.annotations.SerializedName

data class ResponseSpreadsterCategory(
    @SerializedName("error")
    val error: Boolean,
    @SerializedName("facebook")
    val facebook: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("instagram")
    val instagram: String,
    @SerializedName("message")
    val message: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("profile")
    val profile: Any,
    @SerializedName("profile_category_list")
    val profileCategoryList: List<ProfileCategory>,
    @SerializedName("twitter")
    val twitter: String,
    @SerializedName("youtube")
    val youtube: String
)