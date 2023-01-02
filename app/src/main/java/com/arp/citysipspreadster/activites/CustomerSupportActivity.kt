package com.arp.citysipspreadster.activites

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.arp.citysipspreadster.R
import com.arp.citysipspreadster.adapter.CustomerSupportAdapter
import com.arp.citysipspreadster.adapter.HelpFaqAdapter
import com.arp.citysipspreadster.databinding.ActivityCustomerSupportBinding
import com.arp.citysipspreadster.model.accounts.Help
import com.arp.citysipspreadster.model.accounts.ResponseHelp
import com.arp.citysipspreadster.utils.Api
import com.arp.citysipspreadster.utils.RetrofitClient
import com.arp.citysipspreadster.utils.SessionManager
import com.arp.citysipspreadster.utils.WS_URL_PARAMS
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CustomerSupportActivity : AppCompatActivity(),
    CustomerSupportAdapter.OnItemClickListner {

    private lateinit var binding: ActivityCustomerSupportBinding
    var pd: ProgressDialog? = null
    private var sessionManager: SessionManager? = null

    var helpList = ArrayList<Help>()
    private var customerSupportAdapter: CustomerSupportAdapter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCustomerSupportBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sessionManager = SessionManager(this)

        binding.toolbar.toolbarBack.setOnClickListener {
            val homeActivity = Intent(applicationContext, MyAccountActivity::class.java)
            startActivity(homeActivity)
            finish()
        }

        binding.toolbar.imgNoti.setOnClickListener {
            val intent = Intent(this,NotificationActivty::class.java)
            startActivity(intent)
        }

        getCustomerQuestions()
    }

    private fun getCustomerQuestions() {
        val api: Api = RetrofitClient.client!!.create(Api::class.java)
        val call: Call<ResponseHelp>? = api.getCustomerSupport(
            "Bearer " + WS_URL_PARAMS.createJWT(WS_URL_PARAMS.issuer, WS_URL_PARAMS.subject),
            WS_URL_PARAMS.access_key
        )
        call!!.enqueue(object : Callback<ResponseHelp?> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(
                call: Call<ResponseHelp?>,
                response: Response<ResponseHelp?>
            ) {
                Log.e("responseCustomerSupport", Gson().toJson(response.body()))
                if (response.body() != null && response.isSuccessful) {
                    if (response.body()!!.error) {

                        Toast.makeText(
                            this@CustomerSupportActivity,
                            response.body()!!.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {

                        helpList =
                            response.body()!!.helpList as ArrayList<Help>

                        customerSupportAdapter = CustomerSupportAdapter(
                            this@CustomerSupportActivity,
                            helpList,
                            this@CustomerSupportActivity
                        )

                        binding.rclrCustomerSupport.adapter = customerSupportAdapter


                    }
                } else {

                    Toast.makeText(
                        this@CustomerSupportActivity,
                        resources.getString(R.string.error_admin),
                        Toast.LENGTH_SHORT
                    ).show()

                }
            }

            override fun onFailure(call: Call<ResponseHelp?>, t: Throwable) {
                Log.e("error", t.message!!)
                t.printStackTrace()
            }
        })
    }

    override fun onItemClicked(postion: Int) {

        val intent = Intent(this,ChatActivity::class.java)
        startActivity(intent)
    }
}