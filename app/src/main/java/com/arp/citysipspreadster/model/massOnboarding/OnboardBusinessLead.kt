package com.arp.citysipspreadster.model.massOnboarding


import com.google.gson.annotations.SerializedName

data class OnboardBusinessLead(
    @SerializedName("c_id")
    val cId: String,
    @SerializedName("contact")
    val contact: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("latitude")
    val latitude: String,
    @SerializedName("longitude")
    val longitude: String,
    @SerializedName("name")
    val name: String
)