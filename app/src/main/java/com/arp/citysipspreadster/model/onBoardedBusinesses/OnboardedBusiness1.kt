package com.arp.citysipspreadster.model.onBoardedBusinesses


import com.google.gson.annotations.SerializedName

data class OnboardedBusiness1(
    @SerializedName("address")
    val address: String,
    @SerializedName("amount")
    val amount: String,
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
    val name: String,

var isChecked: Boolean = false
)