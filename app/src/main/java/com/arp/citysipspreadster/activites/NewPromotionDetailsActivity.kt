package com.arp.citysipspreadster.activites

import android.annotation.SuppressLint
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.arp.citysipspreadster.R
import com.arp.citysipspreadster.databinding.ActivityNewPromotionDetailsBinding
import com.arp.citysipspreadster.model.deshboard.ResponsePromotionDetails
import com.arp.citysipspreadster.model.promotion.ResponseAddLead
import com.arp.citysipspreadster.utils.Api
import com.arp.citysipspreadster.utils.RetrofitClient
import com.arp.citysipspreadster.utils.SessionManager
import com.arp.citysipspreadster.utils.WS_URL_PARAMS
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NewPromotionDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNewPromotionDetailsBinding
    var pd: ProgressDialog? = null
    private var businessOfferId: String? = null
    private var address: String? = null
    private var percentage: String? = null
    private var offerTitle: String? = null
    private var businessName: String? = null
    private var sessionManager: SessionManager? = null
    private var dialog: Dialog? = null
    var flag = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewPromotionDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sessionManager = SessionManager(this)
        flag = intent.getStringExtra("flag").toString()

        binding.toolbar.toolbarBack.setOnClickListener {
            val intent = Intent(this@NewPromotionDetailsActivity, NewPromotionActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.toolbar.imgNoti.setOnClickListener {
            val intent = Intent(this,NotificationActivty::class.java)
            startActivity(intent)
        }

        //businessOfferId = intent.getStringExtra("businessOfferId")

        getPromotionDetails(sessionManager!!.getPromotionId().toString())

        binding.btnPromoteNow.setOnClickListener {

            val intent = Intent(this, AddPromotionActivity::class.java)
            // intent.putExtra("offerId",businessOfferId)
            intent.putExtra("businessName", businessName)
            intent.putExtra("address", address)
            intent.putExtra("percentage", percentage)
            intent.putExtra("offerTitle", offerTitle)
            startActivity(intent)
        }
        binding.btnAddToMyLead.setOnClickListener {

            dialog = Dialog(this@NewPromotionDetailsActivity, R.style.DialogStyle)
            dialog!!.setContentView(R.layout.raw_add_lead_dialog)
            dialog!!.window!!.setBackgroundDrawableResource(android.R.color.transparent)
            dialog!!.show()

            dialog!!.findViewById<View>(R.id.btnDone)
                .setOnClickListener {

                    addToLead(
                        sessionManager!!.getUserId().toString(),
                        sessionManager!!.getPromotionId().toString()
                    )

                }
        }

    }

    private fun getPromotionDetails(businessOfferId: String) {
        pd = ProgressDialog(this)
        pd!!.setMessage("Profile is loading...")
        pd!!.setCancelable(false)
        pd!!.show()
        val api: Api = RetrofitClient.client!!.create(Api::class.java)
        val call: Call<ResponsePromotionDetails>? = api.getPromotionCategoryDetails(
            "Bearer " + WS_URL_PARAMS.createJWT(WS_URL_PARAMS.issuer, WS_URL_PARAMS.subject),
            WS_URL_PARAMS.access_key,
            businessOfferId
        )
        call!!.enqueue(object : Callback<ResponsePromotionDetails?> {
            @SuppressLint("SetTextI18n")
            override fun onResponse(
                call: Call<ResponsePromotionDetails?>,
                response: Response<ResponsePromotionDetails?>
            ) {

                Log.e("responseDetails", Gson().toJson(response.body()))

                if (pd!!.isShowing) {
                    pd!!.dismiss()
                }

                if (!(response.body() == null || !response.isSuccessful)) {

                    if (response.body()!!.error) {

                        binding.rlMain.visibility = View.GONE
                        binding.blMain.visibility = View.GONE
                        binding.llError.visibility = View.VISIBLE
                        Toast.makeText(
                            this@NewPromotionDetailsActivity,
                            response.body()!!.message,
                            Toast.LENGTH_SHORT
                        ).show()

                    } else {

                        binding.rlMain.visibility = View.VISIBLE
                        binding.blMain.visibility = View.VISIBLE
                        binding.llError.visibility = View.GONE

                        if (response.body()!!.image == "") {

                            val options: RequestOptions = RequestOptions()
                                .centerCrop()
                                .placeholder(R.drawable.bg_image)
                                .error(R.drawable.bg_image)

                            Glide.with(this@NewPromotionDetailsActivity)
                                .load(response.body()!!.image).apply(options)
                                .into(binding.imgPromotion)


                        } else {

                            val options: RequestOptions = RequestOptions()
                                .centerCrop()
                                .placeholder(R.drawable.bg_image)
                                .error(R.drawable.bg_image)

                            Glide.with(this@NewPromotionDetailsActivity)
                                .load(Api.imageUrl + response.body()!!.image).apply(options)
                                .into(binding.imgPromotion)


                        }

                        if (response.body()!!.businessName == "") {
                            binding.txtBusinessName.text = ""
                            businessName = ""
                        } else {
                            binding.txtBusinessName.text = response.body()!!.businessName
                            businessName = response.body()!!.businessName
                        }

                        if (response.body()!!.emailId == "") {
                            binding.txtEmail.text = ""
                        } else {
                            binding.txtEmail.text = response.body()!!.emailId
                        }

                        if (response.body()!!.phoneNo == "") {
                            binding.txtNumber.text = ""
                        } else {
                            binding.txtNumber.text = response.body()!!.phoneNo
                        }

                        if (response.body()!!.description == "") {
                            binding.txtDescription.text = ""

                        } else {
                            binding.txtDescription.text = response.body()!!.description
                        }

                        if (response.body()!!.percentage == "") {
                            binding.txtPercentage.text = ""
                            percentage = response.body()!!.businessName
                        } else {
                            binding.txtPercentage.text = response.body()!!.percentage + "%"
                            percentage = response.body()!!.percentage + "%"
                        }

                        if (response.body()!!.title == "") {
                            binding.txtOfferTitle.text = ""
                            offerTitle = ""
                        } else {
                            binding.txtOfferTitle.text = response.body()!!.title
                            offerTitle = response.body()!!.title
                        }


                        if (response.body()!!.rating == "") {
                            binding.txtRating.text = ""
                        } else {
                            binding.txtRating.text = response.body()!!.rating
                        }

                        if (response.body()!!.address == "") {
                            binding.txtAddress.text = ""
                            address = ""
                        } else {
                            binding.txtAddress.text = response.body()!!.address
                            address = response.body()!!.address
                        }

                    }

                } else {

                    Toast.makeText(
                        this@NewPromotionDetailsActivity,
                        resources.getString(R.string.error_admin),
                        Toast.LENGTH_SHORT
                    ).show()

                }
            }

            override fun onFailure(call: Call<ResponsePromotionDetails?>, t: Throwable) {
                t.printStackTrace()
                pd!!.dismiss()
            }
        })
    }


    private fun addToLead(sid: String, offerId: String) {
        pd = ProgressDialog(this)
        pd!!.setMessage("Profile is loading...")
        pd!!.setCancelable(false)
        pd!!.show()
        val api: Api = RetrofitClient.client!!.create(Api::class.java)
        val call: Call<ResponseAddLead>? = api.addToLead(
            "Bearer " + WS_URL_PARAMS.createJWT(WS_URL_PARAMS.issuer, WS_URL_PARAMS.subject),
            WS_URL_PARAMS.access_key,
            sid,
            offerId
        )
        call!!.enqueue(object : Callback<ResponseAddLead?> {
            override fun onResponse(
                call: Call<ResponseAddLead?>,
                response: Response<ResponseAddLead?>
            ) {

                Log.e("responseDetails", Gson().toJson(response.body()))

                if (pd!!.isShowing) {
                    pd!!.dismiss()
                }

                if (!(response.body() == null || !response.isSuccessful)) {

                    if (response.body()!!.error) {
                        dialog!!.dismiss()

                        Toast.makeText(
                            this@NewPromotionDetailsActivity,
                            response.body()!!.message,
                            Toast.LENGTH_SHORT
                        ).show()

                    } else {

                        dialog!!.dismiss()
                    }

                } else {

                    Toast.makeText(
                        this@NewPromotionDetailsActivity,
                        resources.getString(R.string.error_admin),
                        Toast.LENGTH_SHORT
                    ).show()

                }
            }

            override fun onFailure(call: Call<ResponseAddLead?>, t: Throwable) {
                t.printStackTrace()
                pd!!.dismiss()
            }
        })
    }
}