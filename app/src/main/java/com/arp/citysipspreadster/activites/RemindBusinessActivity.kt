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
import com.arp.citysipspreadster.adapter.MyWalletHistroyListAdapter
import com.arp.citysipspreadster.adapter.MyWalletWidthrawListAdapter
import com.arp.citysipspreadster.databinding.ActivityInviteBusinessBinding
import com.arp.citysipspreadster.databinding.ActivityRemindBusinessBinding
import com.arp.citysipspreadster.model.ResponseInviteBusiness
import com.arp.citysipspreadster.model.accounts.Histroy
import com.arp.citysipspreadster.model.accounts.ResponseMyWallet
import com.arp.citysipspreadster.model.accounts.WithdrawRequest
import com.arp.citysipspreadster.utils.Api
import com.arp.citysipspreadster.utils.RetrofitClient
import com.arp.citysipspreadster.utils.SessionManager
import com.arp.citysipspreadster.utils.WS_URL_PARAMS
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RemindBusinessActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRemindBusinessBinding
    var pd: ProgressDialog? = null
    private var sessionManager: SessionManager? = null
    private var dialog: Dialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRemindBusinessBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sessionManager = SessionManager(this)
        val businessId = intent.getStringExtra("businessId")

        binding.toolbar.toolbarBack.setOnClickListener {

                val homeActivity = Intent(applicationContext, OnBoardBusinessActivity::class.java)
                startActivity(homeActivity)
                finish()
        }
        binding.toolbar.imgNoti.setOnClickListener {
            val intent = Intent(this,NotificationActivty::class.java)
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

                        Toast.makeText(this@RemindBusinessActivity, response.body()!!.message, Toast.LENGTH_SHORT).show()

                    } else {

                        dialog = Dialog(this@RemindBusinessActivity, R.style.DialogStyle)
                        dialog!!.setContentView(R.layout.raw_reminder_send_dialog)
                        dialog!!.window!!.setBackgroundDrawableResource(android.R.color.transparent)
                        dialog!!.show()

                        dialog!!.findViewById<View>(R.id.btnDone)
                            .setOnClickListener {

                                val intent = Intent(this@RemindBusinessActivity, OnBoardBusinessActivity::class.java)
                                startActivity(intent)
                                dialog!!.dismiss()

                            }

                    }

                } else {

                    Toast.makeText(this@RemindBusinessActivity, resources.getString(R.string.error_admin), Toast.LENGTH_SHORT).show()

                }
            }

            override fun onFailure(call: Call<ResponseInviteBusiness?>, t: Throwable) {
                t.printStackTrace()
                pd!!.dismiss()
            }
        })
    }
}