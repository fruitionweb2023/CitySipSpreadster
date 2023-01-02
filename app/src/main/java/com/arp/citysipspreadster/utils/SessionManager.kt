package com.arp.citysipspreadster.utils

import android.content.SharedPreferences
import android.content.Intent
import com.arp.citysipspreadster.activites.SpleshScreenActivity
import android.app.Activity
import android.content.Context
import com.arp.citysipspreadster.activites.LoginActivity
import com.arp.citysipspreadster.activites.NumberVerificationActivity

class SessionManager(var context: Context) {
    var pre: SharedPreferences
    var edit: SharedPreferences.Editor
    var Moad = 0
    var userUrl = "Pic_URL"
        get() = pre.getString(field, "")!!
        set(s1) {
            edit.putString(userUrl, s1)
            edit.commit()
        }
    private val username = "Username"
    private val profileImage = "ProfileImage"
    private val userId = "UserId"
    private val promotionId = "PromotionId"
    private val catId = "CatId"
     val isShowMassOnBoardingDialog = "IsShowMassOnBoardingDialog"
     val isShowIndividualOnBoardingDialog = "IsShowIndividualOnBoardingDialog"
     val isShowNewPromotionDialog = "IsShowNewPromotionDialog"

    private val storename = "Storename"

    val isLogin = "IsLogin"
    val isOnBoardingScreen = "IsOnBoardingScreen"
    private val businessType = "Type"
    private val usercontact = "usercontact"
    private val userlocation = "userlocation"
    private val gender = "gender"
    var token = "token"
        set(token) {
            edit.putString(this.token, token)
            edit.commit()
        }
    private val latitude = "latitude"
    private val longitude = "longitude"
    val isRestaurant = "isRestaurant"
    val isDoctor = "isDoctor"
    val isLawyer = "isLawyer"
    val isInsurance = "isInsurance"
    val isSpa = "isSpa"
    val isSaloon = "isSaloon"



    fun setOnBoardScreen(b: Boolean?) {
        edit.putBoolean(isOnBoardingScreen, b!!)
        edit.commit()
    }

    fun checkOnBoardScreen(): Boolean {
        return pre.getBoolean(isOnBoardingScreen, false)
    }

    fun setMassOnBoardingDialog(b: Boolean) {
        edit.putBoolean(isShowMassOnBoardingDialog, b)
        edit.commit()
    }

    fun getMassOnBoardingDialog(): Boolean {
        return pre.getBoolean(isShowMassOnBoardingDialog, false)
    }

    fun setIndividualOnBoardingDialog(b: Boolean) {
        edit.putBoolean(isShowIndividualOnBoardingDialog, b)
        edit.commit()
    }

    fun getIndividualOnBoardingDialog(): Boolean {
        return pre.getBoolean(isShowIndividualOnBoardingDialog, false)
    }

    fun setNewPromotionDialog(b: Boolean) {
        edit.putBoolean(isShowNewPromotionDialog, b)
        edit.commit()
    }

    fun getNewPromotionDialog(): Boolean {
        return pre.getBoolean(isShowNewPromotionDialog, false)
    }


    fun setLogin(b: Boolean?) {
        edit.putBoolean(isLogin, b!!)
        edit.commit()
    }

    fun checkLogin(): Boolean {
        return pre.getBoolean(isLogin, false)
    }



    fun setUserName(s1: String?) {
        edit.putString(username, s1)
        edit.commit()
    }

    fun getUsername(): String? {
        return pre.getString(username, "")
    }

    fun setUserId(s1: String?) {
        edit.putString(userId, s1)
        edit.commit()
    }

    fun getUserId(): String? {
        return pre.getString(userId, "")
    }

    fun setProfileImage(s1: String?) {
        edit.putString(profileImage, s1)
        edit.commit()
    }

    fun getProfileImage(): String? {
        return pre.getString(profileImage, "")
    }

    fun setLat(s1: String?) {
        edit.putString(latitude, s1)
        edit.commit()
    }

    fun getLat(): String? {
        return pre.getString(latitude, "")
    }

    fun setLng(s1: String?) {
        edit.putString(longitude, s1)
        edit.commit()
    }

    fun getLng(): String? {
        return pre.getString(longitude, "")
    }

    fun setPromotionId(s1: String?) {
        edit.putString(promotionId, s1)
        edit.commit()
    }

    fun getPromotionId(): String? {
        return pre.getString(promotionId, "")
    }

    fun setCatId(s1: String?) {
        edit.putString(catId, s1)
        edit.commit()
    }

    fun getCatId(): String? {
        return pre.getString(catId, "")
    }




    fun LogOut() {
        val intent = Intent(context, NumberVerificationActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        (context as Activity).finish()
        context.startActivity(intent)
    }
    companion object {
        private const val key_Name = "DA"
    }

    init {
        pre = context.getSharedPreferences(key_Name, 0)
        edit = pre.edit()
    }
}