package com.arp.citysipspreadster.activites

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.widget.Toast
import com.arp.citysipspreadster.R
import com.arp.citysipspreadster.databinding.ActivityAddPromotionBinding
import com.arp.citysipspreadster.databinding.ActivityLinkedAccountBinding
import com.arp.citysipspreadster.model.ResponseGetProfile
import com.arp.citysipspreadster.model.ResponseUpdateProfile
import com.arp.citysipspreadster.model.accounts.ResponseBankDetails
import com.arp.citysipspreadster.model.accounts.ResponseUpdateBankDetails
import com.arp.citysipspreadster.utils.Api
import com.arp.citysipspreadster.utils.RetrofitClient
import com.arp.citysipspreadster.utils.SessionManager
import com.arp.citysipspreadster.utils.WS_URL_PARAMS
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LinkedAccountActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLinkedAccountBinding
    var pd: ProgressDialog? = null
    private var sessionManager: SessionManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLinkedAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sessionManager = SessionManager(this)

        binding.toolbar.toolbarBack.setOnClickListener {

            val intent = Intent(this@LinkedAccountActivity, MyWalletActivity::class.java)
            startActivity(intent)
            finish()

        }

        binding.toolbar.imgNoti.setOnClickListener {
            val intent = Intent(this,NotificationActivty::class.java)
            startActivity(intent)
        }

        getBankdetails(sessionManager!!.getUserId().toString())


        binding.btnRegister.setOnClickListener {

            if (binding.edtName.text.toString().trim() == "") {

                binding.edtName.error = "Enter your AccountName"

            } else if (binding.edtAccountNumber.text.toString().trim() == "") {

                binding.edtAccountNumber.error = "Enter your AccountNumber"

            } else if (binding.edtBankName.text.toString().trim() == "") {

                binding.edtBankName.error = "Enter your BankName"

            } else if (binding.edtIsfcCode.text.toString().trim() == "") {

                binding.edtIsfcCode.error = "Enter IFSC Code"

            } else {

                updateBankDetails(sessionManager!!.getUserId()!!,
                    binding.edtName.text.toString(),
                    binding.edtAccountNumber.text.toString(),
                    binding.edtBankName.text.toString(),
                    binding.edtIsfcCode.text.toString())
            }
        }
    }

    private fun getBankdetails(profileId: String) {
        pd = ProgressDialog(this)
        pd!!.setMessage("Profile is loading...")
        pd!!.setCancelable(false)
        pd!!.show()
        val api: Api = RetrofitClient.client!!.create(Api::class.java)
        val call: Call<ResponseBankDetails>? = api.getBankDetails(
            "Bearer " + WS_URL_PARAMS.createJWT(WS_URL_PARAMS.issuer, WS_URL_PARAMS.subject), WS_URL_PARAMS.access_key,profileId)
        call!!.enqueue(object : Callback<ResponseBankDetails?> {
            override fun onResponse(call: Call<ResponseBankDetails?>, response: Response<ResponseBankDetails?>) {

                Log.e("responseBankDetails", Gson().toJson(response.body()))

                if (pd!!.isShowing) {
                    pd!!.dismiss()
                }

                if (!(response.body() == null || !response.isSuccessful)) {

                    if (response.body()!!.error) {


                        Toast.makeText(this@LinkedAccountActivity, response.body()!!.message, Toast.LENGTH_SHORT).show()

                    } else {



                        if (response.body()!!.accName.isNullOrBlank()) {
                            binding.edtName.text = Editable.Factory.getInstance().newEditable("")
                        } else {
                            binding.edtName.text = Editable.Factory.getInstance().newEditable(response.body()!!.accName)
                        }

                        if (response.body()!!.accNumber.isNullOrBlank()) {
                            binding.edtAccountNumber.text = Editable.Factory.getInstance().newEditable("")
                        } else {
                            binding.edtAccountNumber.text =  Editable.Factory.getInstance().newEditable(response.body()!!.accNumber)
                        }

                        if (response.body()!!.bankName.isNullOrBlank()) {
                            binding.edtBankName.text = Editable.Factory.getInstance().newEditable("")
                        } else {
                            binding.edtBankName.text = Editable.Factory.getInstance().newEditable(response.body()!!.bankName)
                        }



                        if (response.body()!!.ifscCode.isNullOrBlank()) {
                            binding.edtIsfcCode.text = Editable.Factory.getInstance().newEditable("")
                        } else {
                            binding.edtIsfcCode.text = Editable.Factory.getInstance().newEditable(response.body()!!.ifscCode)
                        }
                    }

                } else {

                    Toast.makeText(this@LinkedAccountActivity, resources.getString(R.string.error_admin), Toast.LENGTH_SHORT).show()

                }
            }

            override fun onFailure(call: Call<ResponseBankDetails?>, t: Throwable) {
                t.printStackTrace()
                pd!!.dismiss()
            }
        })
    }

    private fun updateBankDetails(profileId : String,name: String,accountNumber:String,bankName:String,ifscCode:String) {
        pd = ProgressDialog(this)
        pd!!.setMessage("Loading...")
        pd!!.setCancelable(false)
        pd!!.show()
        val api: Api = RetrofitClient.client!!.create(Api::class.java)
        val call: Call<ResponseUpdateBankDetails>? = api.sendBankDetails(
            "Bearer " + WS_URL_PARAMS.createJWT(WS_URL_PARAMS.issuer, WS_URL_PARAMS.subject), WS_URL_PARAMS.access_key,profileId,name,accountNumber,bankName,ifscCode)
        call!!.enqueue(object : Callback<ResponseUpdateBankDetails?> {
            @SuppressLint("LongLogTag")
            override fun onResponse(call: Call<ResponseUpdateBankDetails?>, response: Response<ResponseUpdateBankDetails?>) {

                Log.e("responeUpdateBankDetails", Gson().toJson(response.body()))

                if (pd!!.isShowing) {
                    pd!!.dismiss()
                }

                if (!(response.body() == null || !response.isSuccessful)) {

                    if (response.body()!!.error) {


                        Toast.makeText(this@LinkedAccountActivity, response.body()!!.message, Toast.LENGTH_SHORT).show()

                    } else {

                        Toast.makeText(this@LinkedAccountActivity, "Your bank details update successfull...", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@LinkedAccountActivity, MyWalletActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                        finish()

                    }

                } else {

                    Toast.makeText(this@LinkedAccountActivity, resources.getString(R.string.error_admin), Toast.LENGTH_SHORT).show()

                }
            }

            override fun onFailure(call: Call<ResponseUpdateBankDetails?>, t: Throwable) {
                t.printStackTrace()
                pd!!.dismiss()
            }
        })
    }


}