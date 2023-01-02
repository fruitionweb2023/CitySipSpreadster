package com.arp.citysipspreadster.activites

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.arp.citysipspreadster.R
import com.arp.citysipspreadster.adapter.HelpFaqAdapter
import com.arp.citysipspreadster.databinding.ActivitySpreadsterFaqsBinding
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

class SpreadsterFaqsActivity : AppCompatActivity(), HelpFaqAdapter.OnItemClickListner {

    private lateinit var binding: ActivitySpreadsterFaqsBinding
    var pd: ProgressDialog? = null
    private var sessionManager: SessionManager? = null

    var helpList = ArrayList<Help>()
    private var helpFaqAdapter: HelpFaqAdapter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySpreadsterFaqsBinding.inflate(layoutInflater)
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

        getFaqs()


    }


    private fun getFaqs() {
        val api: Api = RetrofitClient.client!!.create(Api::class.java)
        val call: Call<ResponseHelp>? = api.getFaqs(
            "Bearer " + WS_URL_PARAMS.createJWT(WS_URL_PARAMS.issuer, WS_URL_PARAMS.subject),
            WS_URL_PARAMS.access_key
        )
        call!!.enqueue(object : Callback<ResponseHelp?> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(
                call: Call<ResponseHelp?>,
                response: Response<ResponseHelp?>
            ) {
                Log.e("responseFaqs", Gson().toJson(response.body()))
                if (response.body() != null && response.isSuccessful) {
                    if (response.body()!!.error) {

                        Toast.makeText(
                            this@SpreadsterFaqsActivity,
                            response.body()!!.message,
                            Toast.LENGTH_SHORT
                        ).show()

                    } else {

                        helpList =
                            response.body()!!.helpList as ArrayList<Help>

                        helpFaqAdapter = HelpFaqAdapter(
                            this@SpreadsterFaqsActivity,
                            helpList,
                            this@SpreadsterFaqsActivity
                        )

                        binding.rclrFaqs.adapter = helpFaqAdapter


                    }
                } else {

                    Toast.makeText(
                        this@SpreadsterFaqsActivity,
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

        val intent = Intent(this,FaqAnswerActivity::class.java)
        intent.putExtra("title",helpList[postion].title)
        intent.putExtra("description",helpList[postion].description)
        startActivity(intent)
    }
}