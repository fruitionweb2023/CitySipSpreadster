package com.arp.citysipspreadster.utils

import com.arp.citysipspreadster.model.*
import com.arp.citysipspreadster.model.accounts.*
import com.arp.citysipspreadster.model.cityList.ResponseCityList
import com.arp.citysipspreadster.model.countryList.ResponseGetCountry
import com.arp.citysipspreadster.model.deshboard.*
import com.arp.citysipspreadster.model.deshboard.main.ResponseDeshBoardMain
import com.arp.citysipspreadster.model.individualOnBoard.ResponseInduvidualListView
import com.arp.citysipspreadster.model.massOnboarding.ResponseChartOnBoardMassPromotion
import com.arp.citysipspreadster.model.massOnboarding.ResponseMap
import com.arp.citysipspreadster.model.massOnboarding.ResponsemassOnboardingCategory
import com.arp.citysipspreadster.model.notification.ResponseManageSettings
import com.arp.citysipspreadster.model.notification.ResponseNotification
import com.arp.citysipspreadster.model.notification.ResponseNotificationSettings
import com.arp.citysipspreadster.model.onBoardedBusinesses.ResponseOnBoardedBusinesses
import com.arp.citysipspreadster.model.promotion.*
import com.arp.citysipspreadster.model.stateList.ResponseGetStateList
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface Api {

    @POST("send-otp.php")
    @FormUrlEncoded
    fun sendOtp(
        @Header("Authorization") authHeader: String?,
        @Field("accesskey") accesskey: String?,
        @Field("mobile") mobile: String?,
        @Field("otp") otp: String?
    ): Call<ResponseSendOtp?>?


    @POST("verify-mobile.php")
    @FormUrlEncoded
    fun verifyMobile(
        @Header("Authorization") authHeader: String?,
        @Field("accesskey") accesskey: String?,
        @Field("mobile") mobile: String?): Call<ResponseVerifyMobile?>?


    @POST("create-account.php")
    @FormUrlEncoded
    fun createAccount(
        @Header("Authorization") authHeader: String?,
        @Field("accesskey") accesskey: String?,
        @Field("name") name: String?,
        @Field("mobile") mobile: String?,
        @Field("about") about: String?,
        @Field("email") email: String?
    ): Call<ResponseCreateAccount?>?


    @POST("get_profile.php")
    @FormUrlEncoded
    fun getProfile(
        @Header("Authorization") authHeader: String?,
        @Field("accesskey") accesskey: String?,
        @Field("profile_id") profile_id: String?
    ): Call<ResponseGetProfile?>?

 @POST("update_profile.php")
    @FormUrlEncoded
    fun updateProfile(
        @Header("Authorization") authHeader: String?,
        @Field("accesskey") accesskey: String?,
        @Field("profile_id") profile_id: String?,
        @Field("name") name: String?,
        @Field("mobile") mobile: String?,
        @Field("email") email: String?,
        @Field("about") about: String?,
        @Field("country") country: String?,
        @Field("state") state: String?,
        @Field("city") city: String?,
        @Field("image") image: String?,
    ): Call<ResponseUpdateProfile?>?


    @POST("get_country.php")
    @FormUrlEncoded
    fun getCountry(
        @Header("Authorization") authHeader: String?,
        @Field("accesskey") accesskey: String?
    ): Call<ResponseGetCountry?>?

    @POST("get_state.php")
    @FormUrlEncoded
    fun getState(
        @Header("Authorization") authHeader: String?,
        @Field("accesskey") accesskey: String?,
        @Field("country_id") country_id: String?
    ): Call<ResponseGetStateList?>?

    @POST("get_city.php")
    @FormUrlEncoded
    fun getCity(
        @Header("Authorization") authHeader: String?,
        @Field("accesskey") accesskey: String?,
        @Field("state_id") state_id: String?
    ): Call<ResponseCityList>?

    @POST("offer_category.php")
    @FormUrlEncoded
    fun getPromotionCategory(
        @Header("Authorization") authHeader: String?,
        @Field("accesskey") accesskey: String?
    ): Call<ResponsePromotionCategory>?


    @POST("offer_business_list.php")
    @FormUrlEncoded
    fun getPromotionCategoryList(
        @Header("Authorization") authHeader: String?,
        @Field("accesskey") accesskey: String?,
        @Field("cat_id") cat_id: String?
    ): Call<ResponsePromotionList>?

 @POST("offer_business_search.php")
    @FormUrlEncoded
    fun getPromotionSearch(
        @Header("Authorization") authHeader: String?,
        @Field("accesskey") accesskey: String?,
        @Field("keyword") keyword: String?
    ): Call<ResponsePromotionSearch>?

 @POST("offer_business_detail.php")
    @FormUrlEncoded
    fun getPromotionCategoryDetails(
        @Header("Authorization") authHeader: String?,
        @Field("accesskey") accesskey: String?,
        @Field("business_offer_id") business_offer_id: String?
    ): Call<ResponsePromotionDetails>?

@POST("add_lead.php")
    @FormUrlEncoded
    fun addToLead(
        @Header("Authorization") authHeader: String?,
        @Field("accesskey") accesskey: String?,
        @Field("sid") sid: String?,
        @Field("offer_id") offer_id: String?
    ): Call<ResponseAddLead>?


    @Multipart
    @POST("promote.php")
    fun addPromotion(
        @Header("Authorization") authHeader: String?,
        @Part("accesskey") accesskey: RequestBody?,
        @Part("sid") sid: RequestBody?,
        @Part("offer_id") offer_id: RequestBody?,
        @Part("title") title: RequestBody?,
        @Part("description") description: RequestBody?,
        @Part("promo_code") promo_code: RequestBody?,
        @Part image: MultipartBody.Part?): Call<ResponseAddPromotion?>?


    @POST("promotion_detail.php")
    @FormUrlEncoded
    fun getSharePromotionDetails(
        @Header("Authorization") authHeader: String?,
        @Field("accesskey") accesskey: String?,
        @Field("promotion_id") promotion_id: String?,
        @Field("sid") sid: String?
    ): Call<ResponseSharePromotionDetails>?

    @POST("delete_promotion.php")
    @FormUrlEncoded
    fun deletePromotion(
        @Header("Authorization") authHeader: String?,
        @Field("accesskey") accesskey: String?,
        @Field("id") id: String?): Call<ResponseDeletePromotion>?

    @POST("favourite_promotion.php")
    @FormUrlEncoded
    fun addToFavourite(
        @Header("Authorization") authHeader: String?,
        @Field("accesskey") accesskey: String?,
        @Field("sid") sid: String?,
        @Field("promotion_id") promotion_id: String?,
        @Field("status") status: String?,
    ): Call<ResponseAddToFavPromotion>?


    @POST("my_promotion_category.php")
    @FormUrlEncoded
    fun getMyPromotionCategory(
        @Header("Authorization") authHeader: String?,
        @Field("accesskey") accesskey: String?,
        @Field("sid") sid: String?
    ): Call<MypromotionCategoryList>?

    @POST("my_promotion_list.php")
    @FormUrlEncoded
    fun getMyPromotion(
        @Header("Authorization") authHeader: String?,
        @Field("accesskey") accesskey: String?,
        @Field("sid") sid: String?,
        @Field("cat_id") cat_id: String?
    ): Call<ResponseMyPromotion>?


    @POST("help_faq.php")
    @FormUrlEncoded
    fun getFaqs(
        @Header("Authorization") authHeader: String?,
        @Field("accesskey") accesskey: String?
    ): Call<ResponseHelp>?

    @POST("new_faq.php")
    @FormUrlEncoded
    fun getCustomerSupport(
        @Header("Authorization") authHeader: String?,
        @Field("accesskey") accesskey: String?
    ): Call<ResponseHelp>?

    @POST("get_bank.php")
    @FormUrlEncoded
    fun getBankDetails(
        @Header("Authorization") authHeader: String?,
        @Field("accesskey") accesskey: String?,
        @Field("profile_id") profile_id: String?
    ): Call<ResponseBankDetails>?

 @POST("update_bank.php")
    @FormUrlEncoded
    fun sendBankDetails(
        @Header("Authorization") authHeader: String?,
        @Field("accesskey") accesskey: String?,
        @Field("profile_id") profile_id: String?,
        @Field("acc_name") acc_name: String?,
        @Field("acc_number") acc_number: String?,
        @Field("bank_name") bank_name: String?,
        @Field("ifsc_code") ifsc_code: String?
    ): Call<ResponseUpdateBankDetails>?

    @POST("wallet.php")
    @FormUrlEncoded
    fun getMyWalletDetails(
        @Header("Authorization") authHeader: String?,
        @Field("accesskey") accesskey: String?,
        @Field("profile_id") profile_id: String?
    ): Call<ResponseMyWallet>?


  @POST("withdraw.php")
    @FormUrlEncoded
    fun widthrawMoney(
        @Header("Authorization") authHeader: String?,
        @Field("accesskey") accesskey: String?,
        @Field("profile_id") profile_id: String?
    ): Call<ResponseWidthrawMoney>?

    @POST("dashboard.php")
    @FormUrlEncoded
    fun getDeshBoardDetails(
        @Header("Authorization") authHeader: String?,
        @Field("accesskey") accesskey: String?,
        @Field("spredster_id") spredster_id: String?
    ): Call<ResponseDeshBoardMain>?


    //Screen 13 -> MassOnboarding


    //CATEGORY LIST - MASS ON BOARDING AND INDIVIDUAL ON BOARDING
    @POST("onboard_category.php")
    @FormUrlEncoded
    fun getCategoryMassOnboarding(
        @Header("Authorization") authHeader: String?,
        @Field("accesskey") accesskey: String?
    ): Call<ResponsemassOnboardingCategory>?


    //Map
    @POST("onboard_map_lead.php")
    @FormUrlEncoded
    fun getMapDetails(
        @Header("Authorization") authHeader: String?,
        @Field("accesskey") accesskey: String?,
        @Field("cat_id") cat_id: String?
    ): Call<ResponseMap>?

    //Chart
    @POST("onboard_chart.php")
    @FormUrlEncoded
    fun getChartDetails(
        @Header("Authorization") authHeader: String?,
        @Field("accesskey") accesskey: String?
    ): Call<ResponseChartOnBoardMassPromotion>?



    @POST("onboarded_business.php")
    @FormUrlEncoded
    fun getOnBoardedBusinessesDetails(
        @Header("Authorization") authHeader: String?,
        @Field("accesskey") accesskey: String?,
        @Field("sid") sid: String?
    ): Call<ResponseOnBoardedBusinesses>?

    @POST("notification.php")
    @FormUrlEncoded
    fun getNotificationList(
        @Header("Authorization") authHeader: String?,
        @Field("accesskey") accesskey: String?,
        @Field("sid") sid: String?
    ): Call<ResponseNotification>?


    // Individual OnBoard


    @POST("onboard_list_view.php")
    @FormUrlEncoded
    fun getListView(
        @Header("Authorization") authHeader: String?,
        @Field("accesskey") accesskey: String?,
        @Field("cat_id") cat_id: String?
    ): Call<ResponseInduvidualListView>?


    //New Business - 28, 39


    @POST("new_business.php")
    @FormUrlEncoded
    fun createNewBusiness(
        @Header("Authorization") authHeader: String?,
        @Field("accesskey") accesskey: String?,
        @Field("sid") sid: String?,
        @Field("owner_name") owner_name: String?,
        @Field("business_number") business_number: String?,
        @Field("business_name") business_name: String?,
        @Field("c_id") cid: String?,
        @Field("contact_number") contact_number: String?,
        @Field("email") email: String?
    ): Call<ResponseNewBusiness>?


    //manage notification

    @POST("notification_setting.php")
    @FormUrlEncoded
    fun getNotificationSettings(
        @Header("Authorization") authHeader: String?,
        @Field("accesskey") accesskey: String?,
        @Field("cat_id") cat_id: String?,
        @Field("sid") sid: String?
    ): Call<ResponseNotificationSettings>?

    @POST("manage_setting.php")
    @FormUrlEncoded
    fun managedSettingsNotification(
        @Header("Authorization") authHeader: String?,
        @Field("accesskey") accesskey: String?,
        @Field("setting_id") setting_id: String?,
        @Field("sid") sid: String?,
        @Field("status") status: String?
    ): Call<ResponseManageSettings>?

    @POST("profile_page.php")
    @FormUrlEncoded
    fun getSpreadsterCategory(
        @Header("Authorization") authHeader: String?,
        @Field("accesskey") accesskey: String?,
        @Field("sid") sid: String?
    ): Call<ResponseSpreadsterCategory>?

    @POST("profile_page_offer.php")
    @FormUrlEncoded
    fun getSpreadsterProfile(
        @Header("Authorization") authHeader: String?,
        @Field("accesskey") accesskey: String?,
        @Field("sid") sid: String?,
        @Field("cat_id") cat_id: String?
    ): Call<ResponseSpreadsterProfile>?


    //Invite Business

  @POST("invite_business.php")
    @FormUrlEncoded
    fun sendBusinessInvitation(
        @Header("Authorization") authHeader: String?,
        @Field("accesskey") accesskey: String?,
        @Field("sid") sid: String?,
        @Field("business_id") business_id : String?,
        @Field("msg") msg: String?
    ): Call<ResponseInviteBusiness>?

@POST("link_account.php")
    @FormUrlEncoded
    fun sendLinkInvitation(
        @Header("Authorization") authHeader: String?,
        @Field("accesskey") accesskey: String?,
        @Field("sid") sid: String?,
        @Field("type") type : String?, //(facebbok,instagram,twitter,youtube)
        @Field("link") link: String?
    ): Call<ResponseLinkAccount>?


    @POST("notification_setting.php")
    @FormUrlEncoded
    fun getNotificationUpdate(
        @Header("Authorization") authHeader: String?,
        @Field("accesskey") accesskey: String?,
        @Field("sid") sid: String?,
        @Field("cat_id") cat_id: String?
    ): Call<ReasponseNotificatioUpdate>?


    companion object {
        const val imageUrl = "http://medicaiditpark.com//city_slip/"
        const val url = "http://medicaiditpark.com//city_slip/api-firebase/spredster/"
    }
}