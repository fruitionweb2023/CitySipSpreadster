package com.arp.citysipspreadster.model.deshboard.main


import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("business_on_board")
    val businessOnBoard: String,
    @SerializedName("earning_vs_reach")
    val earningVsReach: EarningVsReach,
    @SerializedName("my_dashboard")
    val myDashboard: MyDashboard,
    @SerializedName("my_wallet")
    val myWallet: Int,
    @SerializedName("total_earning")
    val totalEarning: String
)