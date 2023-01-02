package com.arp.citysipspreadster.activites

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.arp.citysipspreadster.R
import com.arp.citysipspreadster.databinding.ActivityMyAccountBinding
import com.arp.citysipspreadster.model.ResponseGetProfile
import com.arp.citysipspreadster.utils.*
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyAccountActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMyAccountBinding
    private var bottomButtonClickListner: BottomButtonClickListner? = null
    var pd: ProgressDialog? = null
    private var sessionManager: SessionManager? = null
    var imageUrl = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sessionManager = SessionManager(this)

        getProfile(sessionManager!!.getUserId().toString())

        onBackPressedDispatcher.addCallback(this,onBackInvokedCallBack)

        bottomButtonClickListner = BottomButtonClickListner(this)
        binding.bottomnavigation.bbImgMenu.setColorFilter(resources.getColor(R.color.clr_EA2A31))
        binding.bottomnavigation.bbHome.setOnClickListener(BottomButtonClickListner(this))
        binding.bottomnavigation.bbMyBusiness.setOnClickListener(BottomButtonClickListner(this))
        binding.bottomnavigation.bbOrder.setOnClickListener(BottomButtonClickListner(this))
        binding.bottomnavigation.bbMenu.setOnClickListener(BottomButtonClickListner(this))
        binding.bottomnavigation.bbWallet.setOnClickListener(BottomButtonClickListner(this))

        binding.toolbar.toolbarBack.setOnClickListener {
            val homeActivity = Intent(applicationContext, DeshBoardActivity::class.java)
            startActivity(homeActivity)
            finish()
        }

        binding.toolbar.imgNoti.setOnClickListener {
            val intent = Intent(this,NotificationActivty::class.java)
            startActivity(intent)
        }

        binding.llMyProfile.setOnClickListener {
            val intent = Intent(this,ProfileActivity::class.java)
            startActivity(intent)
        }

        binding.llMyPromotion.setOnClickListener {
            val intent = Intent(this,MyPromotionActivity::class.java)
            intent.putExtra("flag","1")
            startActivity(intent)
        }


        binding.llsocialAccounts.setOnClickListener {
            val intent = Intent(this,SocialAccountsActivity::class.java)
            startActivity(intent)
        }


        binding.llNotification.setOnClickListener {
            val intent = Intent(this,ManageNotificationActivity::class.java)
            startActivity(intent)
        }


        binding.llCustomerSupport.setOnClickListener {
            val intent = Intent(this,CustomerSupportActivity::class.java)
            startActivity(intent)
        }



        binding.llFaqs.setOnClickListener {
            val intent = Intent(this,SpreadsterFaqsActivity::class.java)
            startActivity(intent)
        }


        binding.llPrivacyPolicy.setOnClickListener {
            val intent = Intent(this,PrivacyPolicyActivity::class.java)
            startActivity(intent)
        }

        binding.llTermsAndConditions.setOnClickListener {
            val intent = Intent(this,TermsAndConditions::class.java)
            startActivity(intent)
        }

        binding.llBtnSpreadster.setOnClickListener {
            val intent = Intent(this,SpreadsterProfileActivity::class.java)
            intent.putExtra("image",imageUrl)
            startActivity(intent)
        }

        binding.btnLogout.setOnClickListener {

            sessionManager!!.LogOut()
            sessionManager!!.setLogin(false)
        }

    }

    private fun getProfile(profileId: String) {
        pd = ProgressDialog(this)
        pd!!.setMessage("Please wait...")
        pd!!.setCancelable(false)
        pd!!.show()
        val api: Api = RetrofitClient.client!!.create(Api::class.java)
        val call: Call<ResponseGetProfile?>? = api.getProfile(
            "Bearer " + WS_URL_PARAMS.createJWT(WS_URL_PARAMS.issuer, WS_URL_PARAMS.subject), WS_URL_PARAMS.access_key,profileId)
        call!!.enqueue(object : Callback<ResponseGetProfile?> {
            @SuppressLint("SetTextI18n")
            override fun onResponse(call: Call<ResponseGetProfile?>, response: Response<ResponseGetProfile?>) {

                Log.e("responseGetProfile", Gson().toJson(response.body()))

                if (pd!!.isShowing) {
                    pd!!.dismiss()
                }

                if (!(response.body() == null || !response.isSuccessful)) {

                    if (response.body()!!.error) {


                        Toast.makeText(this@MyAccountActivity, response.body()!!.message, Toast.LENGTH_SHORT).show()

                    } else {

                        imageUrl = response.body()!!.profile

                        if (response.body()!!.name == "") {
                            binding.txtUserName.text = ""
                        } else {
                            binding.txtUserName.text = response.body()!!.name
                        }


                        if (response.body()!!.id.toString() == "") {
                            binding.txtSid.text = ""
                        } else {
                            binding.txtSid.text = "SID -" + response.body()!!.id.toString()
                        }
                        Log.e("MyAccountProfileImage", response.body()!!.profile)
                        sessionManager!!.setProfileImage(response.body()!!.profile)
                        if (response.body()!!.profile == "") {

                            val options: RequestOptions = RequestOptions()
                                .centerCrop()
                                .placeholder(R.drawable.profile_img)
                                .error(R.drawable.profile_img)

                            Glide.with(this@MyAccountActivity).load(response.body()!!.profile).apply(options).into(binding.profileImage)

                        } else {

                            val options: RequestOptions = RequestOptions()
                                .centerCrop()
                                .placeholder(R.drawable.profile_img)
                                .error(R.drawable.profile_img)

                            Glide.with(this@MyAccountActivity).load(response.body()!!.profile).apply(options).into(binding.profileImage)
                        }

                    }

                } else {

                    Toast.makeText(this@MyAccountActivity, resources.getString(R.string.error_admin), Toast.LENGTH_SHORT).show()

                }
            }

            override fun onFailure(call: Call<ResponseGetProfile?>, t: Throwable) {
                t.printStackTrace()
                pd!!.dismiss()
            }
        })
    }

    private  val onBackInvokedCallBack = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            val intent = Intent(this@MyAccountActivity,DeshBoardActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}