package com.arp.citysipspreadster.activites

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import com.arp.citysipspreadster.R
import com.arp.citysipspreadster.databinding.ActivityLoginBinding
import com.arp.citysipspreadster.databinding.ActivitySignUpBinding
import com.arp.citysipspreadster.model.ResponseVerifyMobile
import com.arp.citysipspreadster.utils.Api
import com.arp.citysipspreadster.utils.RetrofitClient
import com.arp.citysipspreadster.utils.SessionManager
import com.arp.citysipspreadster.utils.WS_URL_PARAMS
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private var otp: String? = null
    private var phoneNumber: String? = null
    var counter = 30

    private var sessionManager: SessionManager? = null
    private var strUsername: String? = null
    var pd: ProgressDialog? = null

    var flag = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)


        sessionManager = SessionManager(this)

        otp = intent.getStringExtra("otp")
        phoneNumber = intent.getStringExtra("number")



        binding.edtPhoneNumber.setText(phoneNumber)
        binding.edtPhoneNumber.isEnabled = false
        binding.edtPhoneNumber.isClickable = false
        binding.edtFour.imeOptions = EditorInfo.IME_ACTION_DONE;

        binding.edtOne.addTextChangedListener(GenericTextWatcher(binding.edtOne, binding.edtTwo))
        binding.edtTwo.addTextChangedListener(GenericTextWatcher(binding.edtTwo, binding.edtThree))
        binding.edtThree.addTextChangedListener(
            GenericTextWatcher(
                binding.edtThree,
                binding.edtFour
            )
        )
        binding.edtFour.addTextChangedListener(GenericTextWatcher(binding.edtFour, null))

        binding.edtOne.setOnKeyListener(GenericKeyEvent(binding.edtOne, null))
        binding.edtTwo.setOnKeyListener(GenericKeyEvent(binding.edtTwo, binding.edtOne))
        binding.edtThree.setOnKeyListener(GenericKeyEvent(binding.edtThree, binding.edtTwo))
        binding.edtFour.setOnKeyListener(GenericKeyEvent(binding.edtFour, binding.edtThree))

        binding.btnLogin.setOnClickListener {
            if (binding.edtOne.text.toString() + binding.edtTwo.text.toString() + binding.edtThree.text.toString() + binding.edtFour.text.toString() == "") {
                Toast.makeText(this, "Please enter OTP", Toast.LENGTH_SHORT).show()

            } else if (binding.edtOne.text.toString() + binding.edtTwo.text.toString() + binding.edtThree.text.toString() + binding.edtFour.text.toString() != otp) {

                Toast.makeText(this, "Please enter valid OTP...", Toast.LENGTH_SHORT).show()

            }  else {


                verifyMobile(phoneNumber.toString())


            }
        }

        binding.txtBtnSignUp.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

    }

    class GenericKeyEvent internal constructor(
        private val currentView: EditText,
        private val previousView: EditText?
    ) : View.OnKeyListener {
        override fun onKey(p0: View?, keyCode: Int, event: KeyEvent?): Boolean {
            if (event!!.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DEL && currentView.id != R.id.edt_one && currentView.text.isEmpty()) {
                //If current is empty then previous EditText's number will also be deleted
                previousView!!.text = null
                previousView.requestFocus()
                return true
            }
            return false
        }


    }

    class GenericTextWatcher internal constructor(
        private val currentView: View,
        private val nextView: View?
    ) :
        TextWatcher {
        override fun afterTextChanged(editable: Editable) { // TODO Auto-generated method stub
            val text = editable.toString()
            when (currentView.id) {
                R.id.edt_one -> if (text.length == 1) nextView!!.requestFocus()
                R.id.edt_two -> if (text.length == 1) nextView!!.requestFocus()
                R.id.edt_three -> if (text.length == 1) nextView!!.requestFocus()
                //You can use EditText4 same as above to hide the keyboard

            }
        }

        override fun beforeTextChanged(
            arg0: CharSequence,
            arg1: Int,
            arg2: Int,
            arg3: Int
        ) {
        }

        override fun onTextChanged(
            arg0: CharSequence,
            arg1: Int,
            arg2: Int,
            arg3: Int
        ) {
        }

    }

    private fun verifyMobile(mobile: String) {
        pd = ProgressDialog(this)
        pd!!.setMessage("Loagin please wait...")
        pd!!.setCancelable(false)
        pd!!.show()
        val api: Api = RetrofitClient.client!!.create(Api::class.java)
        val call: Call<ResponseVerifyMobile?>? = api.verifyMobile(
            "Bearer " + WS_URL_PARAMS.createJWT(WS_URL_PARAMS.issuer, WS_URL_PARAMS.subject),
            WS_URL_PARAMS.access_key,
            mobile
        )
        call!!.enqueue(object : Callback<ResponseVerifyMobile?> {
            override fun onResponse(
                call: Call<ResponseVerifyMobile?>,
                response: Response<ResponseVerifyMobile?>
            ) {

                Log.e("responseCreateAccount", Gson().toJson(response.body()))

                if (pd!!.isShowing) {
                    pd!!.dismiss()
                }

                if (!(response.body() == null || !response.isSuccessful)) {

                    if (response.body()!!.error) {


                        Toast.makeText(
                            this@SignUpActivity,
                            response.body()!!.message,
                            Toast.LENGTH_SHORT
                        ).show()
                        val intent = Intent(this@SignUpActivity, CreateProfileActivity::class.java)
                        intent.putExtra("number", phoneNumber)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                        finish()


                    } else {


                        Toast.makeText(
                            this@SignUpActivity,
                            "Login Successfully...",
                            Toast.LENGTH_SHORT
                        ).show()
                        sessionManager!!.setLogin(true)
                        sessionManager!!.setUserId(response.body()!!.userId)
                        val intent = Intent(this@SignUpActivity, DeshBoardActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                        finish()

                    }

                } else {

                    Toast.makeText(
                        this@SignUpActivity,
                        resources.getString(R.string.error_admin),
                        Toast.LENGTH_SHORT
                    ).show()

                }
            }

            override fun onFailure(call: Call<ResponseVerifyMobile?>, t: Throwable) {
                t.printStackTrace()
                pd!!.dismiss()
            }
        })
    }


    fun closeKeyboard() {
        val view = this.currentFocus
        if (view != null) {
            val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}