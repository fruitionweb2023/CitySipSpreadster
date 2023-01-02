package com.arp.citysipspreadster.activites

import android.app.Dialog
import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.arp.citysipspreadster.R
import com.arp.citysipspreadster.databinding.ActivityFaqAnswerBinding
import com.arp.citysipspreadster.databinding.ActivityInviteBusinessBinding
import com.arp.citysipspreadster.model.ResponseInviteBusiness
import com.arp.citysipspreadster.utils.*
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class InviteBusinessActivity : AppCompatActivity() {

    private lateinit var binding: ActivityInviteBusinessBinding
    var pd: ProgressDialog? = null
    private var sessionManager: SessionManager? = null
    private var dialog: Dialog? = null
    private var bottomButtonClickListner: BottomButtonClickListner? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInviteBusinessBinding.inflate(layoutInflater)
        setContentView(binding.root)

        bottomButtonClickListner = BottomButtonClickListner(this)
        binding.bottomnavigation.bbImgHome.setColorFilter(resources.getColor(R.color.clr_EA2A31))
        binding.bottomnavigation.bbHome.setOnClickListener(BottomButtonClickListner(this))
        binding.bottomnavigation.bbMyBusiness.setOnClickListener(BottomButtonClickListner(this))
        binding.bottomnavigation.bbOrder.setOnClickListener(BottomButtonClickListner(this))
        binding.bottomnavigation.bbMenu.setOnClickListener(BottomButtonClickListner(this))
        binding.bottomnavigation.bbWallet.setOnClickListener(BottomButtonClickListner(this))

        sessionManager = SessionManager(this)

        val businessId = intent.getStringExtra("businessId")

        binding.toolbar.toolbarBack.setOnClickListener {
            if (intent.getStringExtra("flag") == "1") {
                val homeActivity = Intent(applicationContext, OnBoardBusinessActivity::class.java)
                startActivity(homeActivity)
                finish()
            } else {
                val homeActivity = Intent(applicationContext, MassOnBoardingActivity::class.java)
                startActivity(homeActivity)
                finish()
            }
        }

        binding.toolbar.imgNoti.setOnClickListener {
            val intent = Intent(this, NotificationActivty::class.java)
            startActivity(intent)
        }


        binding.btnSendInvitation.setOnClickListener {

            if (binding.edtAbout.text.toString() == "") {
                binding.edtAbout.error = "enter custom message..."
            } else {
                sendInvitation(sessionManager!!.getUserId().toString(),businessId.toString(),binding.edtAbout.text.toString())
            }

        }
    }

    private fun sendInvitation(sId: String,businessId : String,message:String) {
        pd = ProgressDialog(this)
        pd!!.setMessage("loading...")
        pd!!.setCancelable(false)
        pd!!.show()
        val api: Api = RetrofitClient.client!!.create(Api::class.java)
        val call: Call<ResponseInviteBusiness>? = api.sendBusinessInvitation(
            "Bearer " + WS_URL_PARAMS.createJWT(WS_URL_PARAMS.issuer, WS_URL_PARAMS.subject), WS_URL_PARAMS.access_key,sId,businessId,message)
        call!!.enqueue(object : Callback<ResponseInviteBusiness?> {
            override fun onResponse(call: Call<ResponseInviteBusiness?>, response: Response<ResponseInviteBusiness?>) {

                Log.e("responseInviteBusiness", Gson().toJson(response.body()))

                if (pd!!.isShowing) {
                    pd!!.dismiss()
                }

                if (!(response.body() == null || !response.isSuccessful)) {

                    if (response.body()!!.error) {

                        Toast.makeText(this@InviteBusinessActivity, response.body()!!.message, Toast.LENGTH_SHORT).show()

                    } else {

                        dialog = Dialog(this@InviteBusinessActivity, R.style.DialogStyle)
                        dialog!!.setContentView(R.layout.raw_invite_business_dialog)
                        dialog!!.window!!.setBackgroundDrawableResource(android.R.color.transparent)
                        dialog!!.show()

                        dialog!!.findViewById<View>(R.id.btnDone)
                            .setOnClickListener {

                                if (intent.getStringExtra("flag") == "1") {

                                    val intent = Intent(this@InviteBusinessActivity, OnBoardBusinessActivity::class.java)
                                    startActivity(intent)
                                    dialog!!.dismiss()

                                } else {

                                    val intent = Intent(this@InviteBusinessActivity, MassOnBoardingActivity::class.java)
                                    startActivity(intent)
                                    dialog!!.dismiss()

                                }

                            }
                    }

                } else {

                    Toast.makeText(this@InviteBusinessActivity, resources.getString(R.string.error_admin), Toast.LENGTH_SHORT).show()

                }
            }

            override fun onFailure(call: Call<ResponseInviteBusiness?>, t: Throwable) {
                t.printStackTrace()
                pd!!.dismiss()
            }
        })
    }
}