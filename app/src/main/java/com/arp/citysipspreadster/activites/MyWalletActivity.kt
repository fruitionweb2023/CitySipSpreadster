package com.arp.citysipspreadster.activites

import android.app.Dialog
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.arp.citysipspreadster.R
import com.arp.citysipspreadster.adapter.MyWalletHistroyListAdapter
import com.arp.citysipspreadster.adapter.MyWalletWidthrawListAdapter
import com.arp.citysipspreadster.databinding.ActivityMyWalletBinding
import com.arp.citysipspreadster.model.accounts.Histroy
import com.arp.citysipspreadster.model.accounts.ResponseMyWallet
import com.arp.citysipspreadster.model.accounts.ResponseWidthrawMoney
import com.arp.citysipspreadster.model.accounts.WithdrawRequest
import com.arp.citysipspreadster.utils.*
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyWalletActivity : AppCompatActivity(), View.OnClickListener,
    MyWalletHistroyListAdapter.OnItemClickListner, MyWalletWidthrawListAdapter.OnItemClickListner {

    private lateinit var binding: ActivityMyWalletBinding
    var pd: ProgressDialog? = null
    private var sessionManager: SessionManager? = null
    private var doubleBackToExitPressedOnce : Boolean = false
    private var bottomButtonClickListner: BottomButtonClickListner? = null

    var histroyList = ArrayList<Histroy>()
    private var myWalletHistroyListAdapter: MyWalletHistroyListAdapter? = null

    var requestesList = ArrayList<WithdrawRequest>()
    private var myWalletWidthrawListAdapter: MyWalletWidthrawListAdapter? = null

    var btnClicked = "0"
    var isClicked = true

    private var dialog: Dialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyWalletBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sessionManager = SessionManager(this)

        onBackPressedDispatcher.addCallback(this,onBackInvokedCallBack)


        binding.toolbar.toolbarBack.setOnClickListener {

                val intent = Intent(this@MyWalletActivity, DeshBoardActivity::class.java)
                startActivity(intent)
                finish()

        }

        binding.toolbar.imgNoti.setOnClickListener {
            val intent = Intent(this,NotificationActivty::class.java)
            startActivity(intent)
        }

        bottomButtonClickListner = BottomButtonClickListner(this)
        binding.bottomnavigation.bbWalletImg.setColorFilter(resources.getColor(R.color.clr_EA2A31))
        binding.bottomnavigation.bbHome.setOnClickListener(BottomButtonClickListner(this))
        binding.bottomnavigation.bbWallet.setOnClickListener(BottomButtonClickListner(this))
        binding.bottomnavigation.bbMyBusiness.setOnClickListener(BottomButtonClickListner(this))
        binding.bottomnavigation.bbOrder.setOnClickListener(BottomButtonClickListner(this))
        binding.bottomnavigation.bbMenu.setOnClickListener(BottomButtonClickListner(this))

        getWalletDetails(sessionManager!!.getUserId().toString(), "1")

        binding.btnHistory.setOnClickListener(this)
        binding.btnRrequest.setOnClickListener(this)

        binding.btnLinkedAccount.setOnClickListener {

            val intent = Intent(this,LinkedAccountActivity::class.java)
            startActivity(intent)
        }

        binding.btnWithdraw.setOnClickListener {

            dialog = Dialog(this@MyWalletActivity, R.style.DialogStyle)
            dialog!!.setContentView(R.layout.raw_widthraw_money_dialog)
            dialog!!.window!!.setBackgroundDrawableResource(android.R.color.transparent)
            dialog!!.show()

            dialog!!.findViewById<View>(R.id.btnWidthraw)
                .setOnClickListener {

                    widthrawMoney(sessionManager!!.getUserId().toString())

                }

            dialog!!.findViewById<View>(R.id.btnCancel).setOnClickListener {

                dialog!!.dismiss()
            }
        }

    }

    private fun getWalletDetails(profileId: String,bool : String) {
        pd = ProgressDialog(this)
        pd!!.setMessage("Profile is loading...")
        pd!!.setCancelable(false)
        pd!!.show()
        val api: Api = RetrofitClient.client!!.create(Api::class.java)
        val call: Call<ResponseMyWallet>? = api.getMyWalletDetails(
            "Bearer " + WS_URL_PARAMS.createJWT(WS_URL_PARAMS.issuer, WS_URL_PARAMS.subject), WS_URL_PARAMS.access_key,profileId)
        call!!.enqueue(object : Callback<ResponseMyWallet?> {
            override fun onResponse(call: Call<ResponseMyWallet?>, response: Response<ResponseMyWallet?>) {

                Log.e("responseWallet", Gson().toJson(response.body()))

                if (pd!!.isShowing) {
                    pd!!.dismiss()
                }

                if (!(response.body() == null || !response.isSuccessful)) {

                    if (response.body()!!.error) {

                        binding.imgError.visibility = View.VISIBLE
                        binding.rclrWallet.visibility = View.GONE

                        Toast.makeText(this@MyWalletActivity, response.body()!!.message, Toast.LENGTH_SHORT).show()

                    } else {


                        if (response.body()!!.wallet == "") {
                            binding.txtAmount.text = ""
                        } else {
                            binding.txtAmount.text = "₹ " +response.body()!!.wallet
                        }


                        binding.imgError.visibility = View.GONE
                        binding.rclrWallet.visibility = View.VISIBLE


                        if (bool == "1") {

                            histroyList =
                                response.body()!!.histroy as ArrayList<Histroy>

                            if (histroyList.isEmpty()) {
                                binding.imgError.visibility = View.VISIBLE
                                binding.rclrWallet.visibility = View.GONE
                            } else {
                                binding.imgError.visibility = View.GONE
                                binding.rclrWallet.visibility = View.VISIBLE
                            }
                            myWalletHistroyListAdapter = MyWalletHistroyListAdapter(
                                this@MyWalletActivity,
                                histroyList,
                                this@MyWalletActivity
                            )

                            binding.rclrWallet.adapter = myWalletHistroyListAdapter

                            Log.e("responseclick", "true")

                        } else {

                            requestesList =
                                response.body()!!.withdrawRequest as ArrayList<WithdrawRequest>

                            if (requestesList.isEmpty()) {
                                binding.imgError.visibility = View.VISIBLE
                                binding.rclrWallet.visibility = View.GONE
                            } else {
                                binding.imgError.visibility = View.GONE
                                binding.rclrWallet.visibility = View.VISIBLE
                            }
                            myWalletWidthrawListAdapter = MyWalletWidthrawListAdapter(
                                this@MyWalletActivity,
                                requestesList,
                                this@MyWalletActivity
                            )

                            binding.rclrWallet.adapter = myWalletWidthrawListAdapter
                            Log.e("responseclick", "false")

                        }

                    }

                } else {

                    Toast.makeText(this@MyWalletActivity, resources.getString(R.string.error_admin), Toast.LENGTH_SHORT).show()

                }
            }

            override fun onFailure(call: Call<ResponseMyWallet?>, t: Throwable) {
                t.printStackTrace()
                pd!!.dismiss()
            }
        })
    }

    private fun widthrawMoney(profileId: String) {
        pd = ProgressDialog(this)
        pd!!.setMessage("Profile is loading...")
        pd!!.setCancelable(false)
        pd!!.show()
        val api: Api = RetrofitClient.client!!.create(Api::class.java)
        val call: Call<ResponseWidthrawMoney>? = api.widthrawMoney(
            "Bearer " + WS_URL_PARAMS.createJWT(WS_URL_PARAMS.issuer, WS_URL_PARAMS.subject), WS_URL_PARAMS.access_key,profileId)
        call!!.enqueue(object : Callback<ResponseWidthrawMoney?> {
            override fun onResponse(call: Call<ResponseWidthrawMoney?>, response: Response<ResponseWidthrawMoney?>) {

                Log.e("responseWidthraw", Gson().toJson(response.body()))

                if (pd!!.isShowing) {
                    pd!!.dismiss()
                }

                if (!(response.body() == null || !response.isSuccessful)) {

                    if (response.body()!!.error) {


                        Toast.makeText(this@MyWalletActivity, response.body()!!.message, Toast.LENGTH_SHORT).show()

                    } else {

                        dialog!!.dismiss()
                       /* val intent = Intent(this@MyWalletActivity,MyWalletActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                        finish()*/
                    }

                } else {

                    Toast.makeText(this@MyWalletActivity, resources.getString(R.string.error_admin), Toast.LENGTH_SHORT).show()

                }
            }

            override fun onFailure(call: Call<ResponseWidthrawMoney?>, t: Throwable) {
                t.printStackTrace()
                pd!!.dismiss()
            }
        })
    }




    @RequiresApi(Build.VERSION_CODES.M)
    override fun onClick(p0: View?) {


        when (p0!!.id) {

            R.id.btnHistory -> {


                isClicked = true
                btnClicked = "1"
                binding.btnHistory.background =
                    resources.getDrawable(R.drawable.btn_sixtydp_corner_radius)
                binding.btnHistory.setTextColor(getColor(R.color.clr_FFFFFF))
                binding.btnRrequest.setBackgroundColor(Color.TRANSPARENT)
                binding.btnRrequest.setTextColor(getColor(R.color.clr_EA2A31))

                getWalletDetails(sessionManager!!.getUserId().toString(), "1")

            }
            R.id.btnRrequest -> {


                isClicked = false
                btnClicked = "2"
                binding.btnHistory.setBackgroundColor(Color.TRANSPARENT)
                binding.btnHistory.setTextColor(getColor(R.color.clr_EA2A31))
                binding.btnRrequest.background =
                    resources.getDrawable(R.drawable.btn_sixtydp_corner_radius)
                binding.btnRrequest.setTextColor(getColor(R.color.clr_FFFFFF))


                getWalletDetails(sessionManager!!.getUserId().toString(), "2")

            }
        }

    }

    override fun onHistroyListItemClicked(postion: Int) {

    }

    override fun onWidthrawItemClicked(postion: Int) {

    }

    private  val onBackInvokedCallBack = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            val intent = Intent(this@MyWalletActivity,DeshBoardActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

}