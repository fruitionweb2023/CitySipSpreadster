package com.arp.citysipspreadster.model.individualOnBoard


import com.google.gson.annotations.SerializedName

data class OnboardBusinessLead(
    @SerializedName("address")
    val address: String,
    @SerializedName("amount")
    val amount: String,
    @SerializedName("c_id")
    val cId: String,
    @SerializedName("contact")
    val contact: String,
    @SerializedName("distance")
    val distance: Int,
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