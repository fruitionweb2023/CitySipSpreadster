package com.arp.citysipspreadster.activites

import android.annotation.SuppressLint
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.arp.citysipspreadster.R
import com.arp.citysipspreadster.adapter.SpreadsterProfileCategoryAdapter
import com.arp.citysipspreadster.databinding.ActivityMyPromotionBinding
import com.arp.citysipspreadster.databinding.ActivitySocialAccountsBinding
import com.arp.citysipspreadster.model.ResponseInviteBusiness
import com.arp.citysipspreadster.model.ResponseLinkAccount
import com.arp.citysipspreadster.model.accounts.ProfileCategory
import com.arp.citysipspreadster.model.accounts.ResponseSpreadsterCategory
import com.arp.citysipspreadster.utils.Api
import com.arp.citysipspreadster.utils.RetrofitClient
import com.arp.citysipspreadster.utils.SessionManager
import com.arp.citysipspreadster.utils.WS_URL_PARAMS
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SocialAccountsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySocialAccountsBinding
    var pd: ProgressDialog? = null
    private var sessionManager: SessionManager? = null
    private var dialog: Dialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySocialAccountsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sessionManager = SessionManager(this)

        getPromotionCategory(sessionManager!!.getUserId().toString())

        binding.toolbar.toolbarBack.setOnClickListener {
            val homeActivity = Intent(applicationContext, MyAccountActivity::class.java)
            startActivity(homeActivity)
            finish()
        }

        binding.toolbar.imgNoti.setOnClickListener {
            val intent = Intent(this,NotificationActivty::class.java)
            startActivity(intent)
        }

        binding.llFacebook.setOnClickListener {

            dialog = Dialog(this@SocialAccountsActivity, R.style.DialogStyle)
            dialog!!.setContentView(R.layout.raw_social_account_link_dialog)
            dialog!!.window!!.setBackgroundDrawableResource(android.R.color.transparent)
            dialog!!.show()
            dialog!!.findViewById<TextView>(R.id.txtAccountName).text = "Facebook"

            dialog!!.findViewById<View>(R.id.btnLink)
                .setOnClickListener {

                    val edtLink = dialog!!.findViewById<EditText>(R.id.edtAccountLink)

                   if (edtLink.text.toString() == "") {
                       edtLink.error = "Please enter your account link"
                   } else {
                       sendInvitation(sessionManager!!.getUserId().toString(),"facebook",edtLink.text.toString())
                   }
                    binding.txtFacebook.text = "Linked"
                    binding.txtFacebook.setTextColor(Color.parseColor("#EA2A31"))
                    dialog!!.dismiss()

                }

            dialog!!.findViewById<View>(R.id.btnCancel)
                .setOnClickListener {
                    if(binding.txtFacebook.text == "Linked") {
                        binding.txtFacebook.text = "Linked"
                        binding.txtFacebook.setTextColor(Color.parseColor("#EA2A31"))
                    } else {
                        binding.txtFacebook.text = "Not Linked"
                        binding.txtFacebook.setTextColor(Color.parseColor("#999999"))
                    }

                    dialog!!.dismiss()

                }


        }

        binding.llTwitter.setOnClickListener {

            dialog = Dialog(this@SocialAccountsActivity, R.style.DialogStyle)
            dialog!!.setContentView(R.layout.raw_social_account_link_dialog)
            dialog!!.window!!.setBackgroundDrawableResource(android.R.color.transparent)
            dialog!!.show()
            dialog!!.findViewById<TextView>(R.id.txtAccountName).text = "Twitter"

            dialog!!.findViewById<View>(R.id.btnLink)
                .setOnClickListener {

                    val edtLink = dialog!!.findViewById<EditText>(R.id.edtAccountLink)

                    if (edtLink.text.toString() == "") {
                        edtLink.error = "Please enter your account link"
                    } else {
                        sendInvitation(sessionManager!!.getUserId().toString(),"twitter",edtLink.text.toString())
                    }
                    binding.txtTwitter.text = "Linked"
                    binding.txtTwitter.setTextColor(Color.parseColor("#EA2A31"))
                    dialog!!.dismiss()

                }

            dialog!!.findViewById<View>(R.id.btnCancel)
                .setOnClickListener {
                    if(binding.txtFacebook.text == "Linked") {
                        binding.txtFacebook.text = "Linked"
                        binding.txtFacebook.setTextColor(Color.parseColor("#EA2A31"))
                    } else {
                        binding.txtFacebook.text = "Not Linked"
                        binding.txtFacebook.setTextColor(Color.parseColor("#999999"))
                    }

                    dialog!!.dismiss()

                }

        }

        binding.llInsta.setOnClickListener {

            dialog = Dialog(this@SocialAccountsActivity, R.style.DialogStyle)
            dialog!!.setContentView(R.layout.raw_social_account_link_dialog)
            dialog!!.window!!.setBackgroundDrawableResource(android.R.color.transparent)
            dialog!!.show()
            dialog!!.findViewById<TextView>(R.id.txtAccountName).text = "Instagram"

            dialog!!.findViewById<View>(R.id.btnLink)
                .setOnClickListener {

                    val edtLink = dialog!!.findViewById<EditText>(R.id.edtAccountLink)

                    if (edtLink.text.toString() == "") {
                        edtLink.error = "Please enter your account link"
                    } else {
                        sendInvitation(sessionManager!!.getUserId().toString(),"instagram",edtLink.text.toString())
                    }
                    binding.txtInsta.text = "Linked"
                    binding.txtInsta.setTextColor(Color.parseColor("#EA2A31"))
                    dialog!!.dismiss()

                }

            dialog!!.findViewById<View>(R.id.btnCancel)
                .setOnClickListener {

                    if(binding.txtFacebook.text == "Linked") {
                        binding.txtFacebook.text = "Linked"
                        binding.txtFacebook.setTextColor(Color.parseColor("#EA2A31"))
                    } else {
                        binding.txtFacebook.text = "Not Linked"
                        binding.txtFacebook.setTextColor(Color.parseColor("#999999"))
                    }
                    dialog!!.dismiss()

                }

        }

        binding.llYoutube.setOnClickListener {

            dialog = Dialog(this@SocialAccountsActivity, R.style.DialogStyle)
            dialog!!.setContentView(R.layout.raw_social_account_link_dialog)
            dialog!!.window!!.setBackgroundDrawableResource(android.R.color.transparent)
            dialog!!.show()
            dialog!!.findViewById<TextView>(R.id.txtAccountName).text = "YouTube"

            dialog!!.findViewById<View>(R.id.btnLink)
                .setOnClickListener {

                    val edtLink = dialog!!.findViewById<EditText>(R.id.edtAccountLink)

                    if (edtLink.text.toString() == "") {
                        edtLink.error = "Please enter your account link"
                    } else {
                        sendInvitation(sessionManager!!.getUserId().toString(),"youtube",edtLink.text.toString())
                    }
                    binding.txtYoutube.text = "Linked"
                    binding.txtYoutube.setTextColor(Color.parseColor("#EA2A31"))
                    dialog!!.dismiss()

                }

            dialog!!.findViewById<View>(R.id.btnCancel)
                .setOnClickListener {

                    if(binding.txtFacebook.text == "Linked") {
                        binding.txtFacebook.text = "Linked"
                        binding.txtFacebook.setTextColor(Color.parseColor("#EA2A31"))
                    } else {
                        binding.txtFacebook.text = "Not Linked"
                        binding.txtFacebook.setTextColor(Color.parseColor("#999999"))
                    }
                    dialog!!.dismiss()

                }
        }

    }

    private fun sendInvitation(sId: String,type : String,link:String) {
        pd = ProgressDialog(this)
        pd!!.setMessage("loading...")
        pd!!.setCancelable(false)
        pd!!.show()
        val api: Api = RetrofitClient.client!!.create(Api::class.java)
        val call: Call<ResponseLinkAccount>? = api.sendLinkInvitation(
            "Bearer " + WS_URL_PARAMS.createJWT(WS_URL_PARAMS.issuer, WS_URL_PARAMS.subject), WS_URL_PARAMS.access_key,sId,type,link)
        call!!.enqueue(object : Callback<ResponseLinkAccount?> {
            override fun onResponse(call: Call<ResponseLinkAccount?>, response: Response<ResponseLinkAccount?>) {

                Log.e("responseInviteBusiness", Gson().toJson(response.body()))

                if (pd!!.isShowing) {
                    pd!!.dismiss()
                }

                if (!(response.body() == null || !response.isSuccessful)) {

                    if (response.body()!!.error) {

                        Toast.makeText(this@SocialAccountsActivity, response.body()!!.message, Toast.LENGTH_SHORT).show()

                    } else {

                    }

                } else {

                    Toast.makeText(this@SocialAccountsActivity, resources.getString(R.string.error_admin), Toast.LENGTH_SHORT).show()

                }
            }

            override fun onFailure(call: Call<ResponseLinkAccount?>, t: Throwable) {
                t.printStackTrace()
                pd!!.dismiss()
            }
        })
    }

    private fun getPromotionCategory(sId:String) {
        val api: Api = RetrofitClient.client!!.create(Api::class.java)
        val call: Call<ResponseSpreadsterCategory>? = api.getSpreadsterCategory(
            "Bearer " + WS_URL_PARAMS.createJWT(WS_URL_PARAMS.issuer, WS_URL_PARAMS.subject),
            WS_URL_PARAMS.access_key,sId)
        call!!.enqueue(object : Callback<ResponseSpreadsterCategory?> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(
                call: Call<ResponseSpreadsterCategory?>,
                response: Response<ResponseSpreadsterCategory?>
            ) {
                Log.e("responseCategory", Gson().toJson(response.body()))
                if (response.body() != null && response.isSuccessful) {
                    if (response.body()!!.error) {

                        Toast.makeText(
                            this@SocialAccountsActivity,
                            response.body()!!.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {


                        if( response.body()!!.twitter != "") {
                            binding.txtInsta.text = "Linked"
                            binding.txtInsta.setTextColor(Color.parseColor("#EA2A31"))

                        } else {
                            binding.txtInsta.text = "Not Linked"
                            binding.txtInsta.setTextColor(Color.parseColor("#999999"))

                        }

                        if(response.body()!!.facebook != "") {
                            binding.txtFacebook.text = "Linked"
                            binding.txtFacebook.setTextColor(Color.parseColor("#EA2A31"))

                        } else {
                            binding.txtFacebook.text = "Not Linked"
                            binding.txtFacebook.setTextColor(Color.parseColor("#999999"))

                        }

                        if( response.body()!!.youtube != "") {
                            binding.txtYoutube.text = "Linked"
                            binding.txtYoutube.setTextColor(Color.parseColor("#EA2A31"))

                        } else {
                            binding.txtYoutube.text = "Not Linked"
                            binding.txtYoutube.setTextColor(Color.parseColor("#999999"))

                        }

                        if( response.body()!!.instagram != "") {
                            binding.txtInsta.text = "Linked"
                            binding.txtInsta.setTextColor(Color.parseColor("#EA2A31"))

                        } else {
                            binding.txtInsta.text = "Not Linked"
                            binding.txtInsta.setTextColor(Color.parseColor("#999999"))

                        }

                    }
                } else {

                    Toast.makeText(
                        this@SocialAccountsActivity,
                        resources.getString(R.string.error_admin),
                        Toast.LENGTH_SHORT
                    ).show()

                }
            }

            override fun onFailure(call: Call<ResponseSpreadsterCategory?>, t: Throwable) {
                Log.e("error", t.message!!)
                t.printStackTrace()
            }
        })
    }
}