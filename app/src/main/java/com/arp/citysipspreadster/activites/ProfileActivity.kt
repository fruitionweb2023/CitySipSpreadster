package com.arp.citysipspreadster.activites

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.arp.citysipspreadster.R
import com.arp.citysipspreadster.databinding.ActivityProfileBinding
import com.arp.citysipspreadster.model.ResponseGetProfile
import com.arp.citysipspreadster.utils.*
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding
    var pd: ProgressDialog? = null
    private var sessionManager: SessionManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sessionManager = SessionManager(this)

        onBackPressedDispatcher.addCallback(this,onBackInvokedCallBack)


        binding.toolbar.toolbarBack.setOnClickListener {
            val homeActivity = Intent(applicationContext, MyAccountActivity::class.java)
            startActivity(homeActivity)
            finish()
        }

        binding.toolbar.imgNoti.setOnClickListener {
            val intent = Intent(this,NotificationActivty::class.java)
            startActivity(intent)
        }

        sessionManager!!.getUserId()?.let { getProfile(it) }
        Log.e("UserId", sessionManager!!.getUserId().toString())

        binding.llEditProfileBtn.setOnClickListener {

            val intent = Intent(this,EditProfileActivity::class.java)
            startActivity(intent)

        }

    }

    private fun getProfile(profileId: String) {
        pd = ProgressDialog(this)
        pd!!.setMessage("Profile is loading...")
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


                        Toast.makeText(this@ProfileActivity, response.body()!!.message, Toast.LENGTH_SHORT).show()

                    } else {


                        if (response.body()!!.name == "") {
                            binding.txtUserName.text = ""
                        } else {
                            binding.txtUserName.text = response.body()!!.name
                        }

                        if (response.body()!!.mobile == "") {
                            binding.txtNumber.text = ""
                        } else {
                            binding.txtNumber.text = response.body()!!.mobile
                        }

                        if (response.body()!!.email == "") {
                            binding.txtEmail.text = ""
                        } else {
                            binding.txtEmail.text = response.body()!!.email
                        }

                        if (response.body()!!.city == "") {
                            binding.txtCity.text = ""
                        } else {
                            binding.txtCity.text = response.body()!!.city
                        }

                        if (response.body()!!.state == "") {
                            binding.txtState.text = ""
                        } else {
                            binding.txtState.text = response.body()!!.state
                        }

                        if (response.body()!!.country == "") {
                            binding.txtCountry.text = ""
                        } else {
                            binding.txtCountry.text = response.body()!!.country
                        }

                        if (response.body()!!.id.toString() == "") {
                            binding.txtSid.text = ""
                        } else {
                            binding.txtSid.text = "SID -" + response.body()!!.id.toString()
                        }

                        if (response.body()!!.detail == "") {
                            binding.txtAboutMe.text = ""
                        } else {
                            binding.txtAboutMe.text = response.body()!!.detail
                        }
                        Log.e("ProfileImage", response.body()!!.profile)

                        if (response.body()!!.profile == "") {

                            val options: RequestOptions = RequestOptions()
                                .centerCrop()
                                .placeholder(R.drawable.profile_img)
                                .error(R.drawable.profile_img)

                            Glide.with(this@ProfileActivity).load(response.body()!!.profile).apply(options).into(binding.profileImage)

                        } else {

                            val options: RequestOptions = RequestOptions()
                                .centerCrop()
                                .placeholder(R.drawable.profile_img)
                                .error(R.drawable.profile_img)

                            Glide.with(this@ProfileActivity).load(response.body()!!.profile).apply(options).into(binding.profileImage)
                        }

                    }

                } else {

                    Toast.makeText(this@ProfileActivity, resources.getString(R.string.error_admin), Toast.LENGTH_SHORT).show()

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
            val intent = Intent(this@ProfileActivity,MyAccountActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}