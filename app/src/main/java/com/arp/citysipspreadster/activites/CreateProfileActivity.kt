package com.arp.citysipspreadster.activites

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.arp.citysipspreadster.R
import com.arp.citysipspreadster.databinding.ActivityCreateProfileBinding
import com.arp.citysipspreadster.model.ResponseCreateAccount
import com.arp.citysipspreadster.utils.Api
import com.arp.citysipspreadster.utils.RetrofitClient
import com.arp.citysipspreadster.utils.SessionManager
import com.arp.citysipspreadster.utils.WS_URL_PARAMS
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CreateProfileActivity : AppCompatActivity() {


    private lateinit var binding: ActivityCreateProfileBinding
    var pd: ProgressDialog? = null
    private var phoneNumber: String? = null
    private var sessionManager: SessionManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sessionManager = SessionManager(this)

        phoneNumber = intent.getStringExtra("number")

        binding.btnStart.setOnClickListener {

            if (binding.edtName.text.toString().isEmpty()) {

                binding.edtName.error = "Please enter your name"

            } else if (binding.edtEmail.text.toString().isEmpty()) {

                binding.edtName.error = "Please enter your email"

            }else if (binding.edtAbout.text.toString().isEmpty()) {

                binding.edtName.error = "Please describe about you"

            }else {

                createAccount(binding.edtName.text.toString().trim(),phoneNumber.toString(),binding.edtAbout.text.toString().trim(),binding.edtEmail.text.toString().trim())

            }
        }
    }

    private fun createAccount(name: String,mobile:String,about:String,email:String) {
        pd = ProgressDialog(this)
        pd!!.setMessage("Loading...")
        pd!!.setCancelable(false)
        pd!!.show()
        val api: Api = RetrofitClient.client!!.create(Api::class.java)
        val call: Call<ResponseCreateAccount?>? = api.createAccount(
            "Bearer " + WS_URL_PARAMS.createJWT(WS_URL_PARAMS.issuer, WS_URL_PARAMS.subject), WS_URL_PARAMS.access_key,name,mobile,about,email)
        call!!.enqueue(object : Callback<ResponseCreateAccount?> {
            override fun onResponse(call: Call<ResponseCreateAccount?>,response: Response<ResponseCreateAccount?>) {

                Log.e("responseCreateAccount", Gson().toJson(response.body()))

                if (pd!!.isShowing) {
                    pd!!.dismiss()
                }

                if (!(response.body() == null || !response.isSuccessful)) {

                    if (response.body()!!.error) {


                        Toast.makeText(this@CreateProfileActivity, response.body()!!.message, Toast.LENGTH_SHORT).show()

                    } else {

                        sessionManager!!.setUserId(response.body()!!.userId)
                        sessionManager!!.setLogin(true)
                        Toast.makeText(this@CreateProfileActivity, "Your account created successfull...", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@CreateProfileActivity, DeshBoardActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                        finish()

                    }

                } else {

                    Toast.makeText(this@CreateProfileActivity, resources.getString(R.string.error_admin), Toast.LENGTH_SHORT).show()

                }
            }

            override fun onFailure(call: Call<ResponseCreateAccount?>, t: Throwable) {
                t.printStackTrace()
                pd!!.dismiss()
            }
        })
    }

}