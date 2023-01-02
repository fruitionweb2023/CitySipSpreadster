package com.arp.citysipspreadster.activites

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.arp.citysipspreadster.R
import com.arp.citysipspreadster.adapter.NotificationListAdapter
import com.arp.citysipspreadster.databinding.ActivityNotificationActivtyBinding
import com.arp.citysipspreadster.model.notification.Notification
import com.arp.citysipspreadster.model.notification.ResponseNotification
import com.arp.citysipspreadster.utils.Api
import com.arp.citysipspreadster.utils.RetrofitClient
import com.arp.citysipspreadster.utils.SessionManager
import com.arp.citysipspreadster.utils.WS_URL_PARAMS
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NotificationActivty : AppCompatActivity() {

    private lateinit var binding: ActivityNotificationActivtyBinding
    var pd: ProgressDialog? = null
    private var sessionManager: SessionManager? = null

    var notificationList = ArrayList<Notification>()
    private var notificationListAdapter: NotificationListAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotificationActivtyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sessionManager = SessionManager(this)

        onBackPressedDispatcher.addCallback(this,onBackInvokedCallBack)

        binding.toolbar.imgNoti.visibility = View.GONE
        binding.toolbar.cartToolbarAddedItemCount.visibility = View.GONE

        binding.toolbar.toolbarBack.setOnClickListener {

            val intent = Intent(this,DeshBoardActivity::class.java)
            startActivity(intent)
            finish()
        }

        getNotification(sessionManager!!.getUserId().toString())



    }


    private fun getNotification(sId:String) {
        val api: Api = RetrofitClient.client!!.create(Api::class.java)
        val call: Call<ResponseNotification>? = api.getNotificationList(
            "Bearer " + WS_URL_PARAMS.createJWT(WS_URL_PARAMS.issuer, WS_URL_PARAMS.subject),
            WS_URL_PARAMS.access_key,sId)
        call!!.enqueue(object : Callback<ResponseNotification?> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(
                call: Call<ResponseNotification?>,
                response: Response<ResponseNotification?>
            ) {
                Log.e("responseNotification", Gson().toJson(response.body()))
                if (response.body() != null && response.isSuccessful) {
                    if (response.body()!!.error) {

                        Toast.makeText(
                            this@NotificationActivty,
                            response.body()!!.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {

                        notificationList =
                            response.body()!!.notificationList as ArrayList<Notification>

                        notificationListAdapter = NotificationListAdapter(
                            this@NotificationActivty,
                            notificationList)

                        binding.rclrNotification.adapter = notificationListAdapter

                    }
                } else {

                    Toast.makeText(
                        this@NotificationActivty,
                        resources.getString(R.string.error_admin),
                        Toast.LENGTH_SHORT
                    ).show()

                }
            }

            override fun onFailure(call: Call<ResponseNotification?>, t: Throwable) {
                Log.e("error", t.message!!)
                t.printStackTrace()
            }
        })

    }

    private  val onBackInvokedCallBack = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            val intent = Intent(this@NotificationActivty,DeshBoardActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

}