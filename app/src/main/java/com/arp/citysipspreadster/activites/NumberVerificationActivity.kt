package com.arp.citysipspreadster.activites

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.arp.citysipspreadster.R
import com.arp.citysipspreadster.databinding.ActivityNumberVerificationBinding
import com.arp.citysipspreadster.model.ResponseSendOtp
import com.arp.citysipspreadster.utils.Api
import com.arp.citysipspreadster.utils.RetrofitClient
import com.arp.citysipspreadster.utils.WS_URL_PARAMS
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NumberVerificationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNumberVerificationBinding
    var pd: ProgressDialog? = null
    var otp = "1234"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNumberVerificationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.e("id", otp)

        binding.edtPhoneNumber.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {

                if (p0.toString().length == 10) {
                    closeKeyboard()

                }
            }
        })

        binding.btnVerify.setOnClickListener {

            if (binding.edtPhoneNumber.text.toString().trim().isEmpty()) {

                binding.edtPhoneNumber.error = "Please enter phone number"

            } else if (binding.edtPhoneNumber.text.toString().length > 10) {

                binding.edtPhoneNumber.error = "Please enter valid phone number"

            } else if (binding.edtPhoneNumber.text.toString().length < 10) {

                binding.edtPhoneNumber.error = "Please enter valid phone number"

            } else {

                sendOtp(binding.edtPhoneNumber.text.toString().trim(),otp)
            }

        }



    }


    private fun sendOtp(mobile:String,otp:String) {
        pd = ProgressDialog(this)
        pd!!.setMessage("Loading...")
        pd!!.setCancelable(false)
        pd!!.show()
        val api: Api = RetrofitClient.client!!.create(Api::class.java)
        val call: Call<ResponseSendOtp?>? = api.sendOtp(
            "Bearer " + WS_URL_PARAMS.createJWT(WS_URL_PARAMS.issuer, WS_URL_PARAMS.subject), WS_URL_PARAMS.access_key,mobile, otp)
        call!!.enqueue(object : Callback<ResponseSendOtp?> {
            override fun onResponse(call: Call<ResponseSendOtp?>, response: Response<ResponseSendOtp?>) {

                Log.e("responseCreateAccount", Gson().toJson(response.body()))

                if (pd!!.isShowing) {
                    pd!!.dismiss()
                }

                if (!(response.body() == null || !response.isSuccessful)) {

                    if (response.body()!!.error) {


                        Toast.makeText(this@NumberVerificationActivity, response.body()!!.message, Toast.LENGTH_SHORT).show()


                    } else {

                        Toast.makeText(this@NumberVerificationActivity, "OTP : $otp", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@NumberVerificationActivity, LoginActivity::class.java)
                        intent.putExtra("otp",otp)
                        intent.putExtra("number",binding.edtPhoneNumber.text.toString().trim())
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                        finish()

                    }

                } else {

                    Toast.makeText(this@NumberVerificationActivity, resources.getString(R.string.error_admin), Toast.LENGTH_SHORT).show()

                }
            }

            override fun onFailure(call: Call<ResponseSendOtp?>, t: Throwable) {
                t.printStackTrace()
                pd!!.dismiss()
            }
        })
    }

    private fun closeKeyboard() {
        val view = this.currentFocus
        if (view != null) {
            val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}