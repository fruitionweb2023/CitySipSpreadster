package com.arp.citysipspreadster.model


import com.google.gson.annotations.SerializedName

data class ResponseGetProfile(
    @SerializedName("city")
    val city: String,
    @SerializedName("country")
    val country: String,
    @SerializedName("detail")
    val detail: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("error")
    val error: Boolean,
    @SerializedName("id")
    val id: String,
    @SerializedName("message")
    val message: String,
    @SerializedName("mobile")
    val mobile: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("profile")
    val profile: String,
    @SerializedName("state")
    val state: String,
    @SerializedName("city_id")
val city_id: String,
@SerializedName("state_id")
val state_id: String,
@SerializedName("country_id")
val country_id: String
)