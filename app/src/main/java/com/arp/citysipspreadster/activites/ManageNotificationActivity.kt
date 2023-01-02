package com.arp.citysipspreadster.activites

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.arp.citysipspreadster.R
import com.arp.citysipspreadster.adapter.HelpFaqAdapter
import com.arp.citysipspreadster.adapter.managedNotificationAdapter
import com.arp.citysipspreadster.databinding.ActivityManageNotificationBinding
import com.arp.citysipspreadster.databinding.ActivityTermsAndConditionsBinding
import com.arp.citysipspreadster.model.accounts.Help
import com.arp.citysipspreadster.model.accounts.ResponseHelp
import com.arp.citysipspreadster.model.notification.NotificationSetting
import com.arp.citysipspreadster.model.notification.ResponseNotificationSettings
import com.arp.citysipspreadster.utils.Api
import com.arp.citysipspreadster.utils.RetrofitClient
import com.arp.citysipspreadster.utils.SessionManager
import com.arp.citysipspreadster.utils.WS_URL_PARAMS
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ManageNotificationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityManageNotificationBinding
    var pd: ProgressDialog? = null
    private var sessionManager: SessionManager? = null

    var notificationSettings = ArrayList<NotificationSetting>()
    private var managedNotificationAdapter : managedNotificationAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityManageNotificationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sessionManager = SessionManager(this)

        getNotification("1",sessionManager!!.getUserId().toString())
        binding.toolbar.toolbarBack.setOnClickListener {
            val homeActivity = Intent(applicationContext, MyAccountActivity::class.java)
            startActivity(homeActivity)
            finish()
        }

        binding.toolbar.imgNoti.setOnClickListener {
            val intent = Intent(this,NotificationActivty::class.java)
            startActivity(intent)
        }
    }

    private fun getNotification(catId : String,sId : String) {
        val api: Api = RetrofitClient.client!!.create(Api::class.java)
        val call: Call<ResponseNotificationSettings>? = api.getNotificationSettings(
            "Bearer " + WS_URL_PARAMS.createJWT(WS_URL_PARAMS.issuer, WS_URL_PARAMS.subject),
            WS_URL_PARAMS.access_key,catId,sId)
        call!!.enqueue(object : Callback<ResponseNotificationSettings?> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(
                call: Call<ResponseNotificationSettings?>,
                response: Response<ResponseNotificationSettings?>
            ) {
                Log.e("responseNotification", Gson().toJson(response.body()))
                if (response.body() != null && response.isSuccessful) {
                    if (response.body()!!.error) {

                        Toast.makeText(
                            this@ManageNotificationActivity,
                            response.body()!!.message,
                            Toast.LENGTH_SHORT
                        ).show()

                    } else {

                        notificationSettings = response.body()!!.notificationSetting as ArrayList<NotificationSetting>

                        managedNotificationAdapter = managedNotificationAdapter(
                            this@ManageNotificationActivity,
                            notificationSettings
                        )

                        binding.rclrNotification.adapter = managedNotificationAdapter


                    }
                } else {

                    Toast.makeText(
                        this@ManageNotificationActivity,
                        resources.getString(R.string.error_admin),
                        Toast.LENGTH_SHORT
                    ).show()

                }
            }

            override fun onFailure(call: Call<ResponseNotificationSettings?>, t: Throwable) {
                Log.e("error", t.message!!)
                t.printStackTrace()
            }
        })
    }
}