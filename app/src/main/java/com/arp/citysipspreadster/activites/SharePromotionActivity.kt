package com.arp.citysipspreadster.activites

import android.annotation.SuppressLint
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.CompoundButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.arp.citysipspreadster.R
import com.arp.citysipspreadster.databinding.ActivitySharePromotionBinding
import com.arp.citysipspreadster.model.promotion.ResponseAddToFavPromotion
import com.arp.citysipspreadster.model.promotion.ResponseDeletePromotion
import com.arp.citysipspreadster.model.promotion.ResponseSharePromotionDetails
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


class SharePromotionActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySharePromotionBinding
    var pd: ProgressDialog? = null
    private var sessionManager: SessionManager? = null
    private var dialog: Dialog? = null
    var flag = false
    var flagNav = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySharePromotionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sessionManager = SessionManager(this)

        flagNav = intent.getStringExtra("flagNav").toString()

        binding.toolbar.toolbarBack.setOnClickListener {
           if (flagNav == "1") {

               val intent = Intent(this@SharePromotionActivity, NewPromotionActivity::class.java)
               startActivity(intent)
               finish()
           } else {

               val intent = Intent(this@SharePromotionActivity, MyPromotionActivity::class.java)
               startActivity(intent)
               finish()

           }
        }

        val uri : Uri? = intent.data
        if (uri != null) {

            val parameters = uri.pathSegments
            val param = parameters[parameters.size - 1]

            Toast.makeText(this, param, Toast.LENGTH_SHORT).show()
        }

        val promotionId = intent.getStringExtra("businessOfferId")

        getSharePromotionDetails(sessionManager!!.getPromotionId().toString(),sessionManager!!.getUserId().toString())

        binding.imgShare.setOnClickListener {

            val sendIntent = Intent()
            sendIntent.action = Intent.ACTION_SEND
            sendIntent.putExtra(Intent.EXTRA_TEXT, "https://medicaiditpark.com/city_slip/offer.php?id=${sessionManager!!.getPromotionId()}")
            sendIntent.type = "text/plain"

            val shareIntent = Intent.createChooser(sendIntent, null)
            startActivity(shareIntent)

        }

        binding.imgDelete.setOnClickListener {

            dialog = Dialog(this@SharePromotionActivity, R.style.DialogStyle)
            dialog!!.setContentView(R.layout.raw_delete_promotion)
            dialog!!.window!!.setBackgroundDrawableResource(android.R.color.transparent)
            dialog!!.show()

            dialog!!.findViewById<View>(R.id.btnDelete)
                .setOnClickListener {

                    deletePromotion(sessionManager!!.getPromotionId().toString())

                }

            dialog!!.findViewById<View>(R.id.btnCancel).setOnClickListener {

                dialog!!.dismiss()
            }
        }

        binding.imgAddToFav.setOnCheckedChangeListener { _, b ->
            if (b) {
                flag = false
                binding.imgAddToFav.isChecked = true
                addToFavourite(
                    sessionManager!!.getUserId().toString(),
                    sessionManager!!.getPromotionId().toString(),
                    "1"
                )
            } else {
                flag = true
                binding.imgAddToFav.isChecked = false
                addToFavourite(
                    sessionManager!!.getUserId().toString(),
                    sessionManager!!.getPromotionId().toString(),
                    "0"
                )
            }
        }

    }

    private fun getSharePromotionDetails(promotionId: String,sId:String) {
        pd = ProgressDialog(this)
        pd!!.setMessage("Promotion is loading...")
        pd!!.setCancelable(false)
        pd!!.show()
        val api: Api = RetrofitClient.client!!.create(Api::class.java)
        val call: Call<ResponseSharePromotionDetails>? = api.getSharePromotionDetails(
            "Bearer " + WS_URL_PARAMS.createJWT(WS_URL_PARAMS.issuer, WS_URL_PARAMS.subject), WS_URL_PARAMS.access_key,promotionId,sId)
        call!!.enqueue(object : Callback<ResponseSharePromotionDetails?> {
            @SuppressLint("SetTextI18n")
            override fun onResponse(call: Call<ResponseSharePromotionDetails?>, response: Response<ResponseSharePromotionDetails?>) {

                Log.e("responseDetails", Gson().toJson(response.body()))

                if (pd!!.isShowing) {
                    pd!!.dismiss()
                }

                if (!(response.body() == null || !response.isSuccessful)) {

                    if (response.body()!!.error) {


                        Toast.makeText(this@SharePromotionActivity, response.body()!!.message, Toast.LENGTH_SHORT).show()

                    } else {


                        if (response.body()!!.imageVideo == "") {

                            val options: RequestOptions = RequestOptions()
                                .centerCrop()
                                .placeholder(R.drawable.bg_image)
                                .error(R.drawable.bg_image)

                            Glide.with(this@SharePromotionActivity).load(Api.imageUrl+response.body()!!.imageVideo).apply(options).into(binding.imgPromotion)


                        } else {

                            val options: RequestOptions = RequestOptions()
                                .centerCrop()
                                .placeholder(R.drawable.bg_image)
                                .error(R.drawable.bg_image)

                            Glide.with(this@SharePromotionActivity).load(Api.imageUrl+response.body()!!.imageVideo).apply(options).into(binding.imgPromotion)


                        }

                        if (response.body()!!.title == "") {
                            binding.txtBusinessName.text = ""
                        } else {
                            binding.txtBusinessName.text =  response.body()!!.title
                        }

                        if (response.body()!!.dateTime == "") {
                            binding.txtTimeAndDate.text = ""
                        } else {
                            binding.txtTimeAndDate.text = response.body()!!.dateTime
                        }

                        if (response.body()!!.description == "") {
                            binding.txtDescription.text = ""
                        } else {
                            binding.txtDescription.text = response.body()!!.description
                        }

                        if (response.body()!!.earning.toString() == "") {
                            binding.txtEarning.text = ""

                        } else {
                            binding.txtEarning.text = "₹ "+response.body()!!.earning.toString()
                        }

                        if (response.body()!!.engagement.toString() == "") {
                            binding.txtEngagement.text = ""

                        } else {
                            binding.txtEngagement.text = response.body()!!.engagement.toString()+ "%"

                        }

                        if (response.body()!!.conversationRatio.toString() == "") {
                            binding.txtConversionRatio.text = ""

                        } else {
                            binding.txtConversionRatio.text = response.body()!!.conversationRatio.toString()

                        }

                        if (response.body()!!.promoCode.toString() == "") {
                            binding.txtCouponCode.text = ""

                        } else {
                            binding.txtCouponCode.text = response.body()!!.promoCode.toString()

                        }

                        if (response.body()!!.status == "") {
                            binding.txtStatus.text = ""

                        } else {

                            if (response.body()!!.status == "1") {
                                binding.txtStatus.text = "Active"
                            } else {
                                binding.txtStatus.text = "Deactive"
                            }

                        }

                        binding.imgAddToFav.isChecked = response.body()!!.favourite_status == "1"



                    }

                } else {

                    Toast.makeText(this@SharePromotionActivity, resources.getString(R.string.error_admin), Toast.LENGTH_SHORT).show()

                }
            }

            override fun onFailure(call: Call<ResponseSharePromotionDetails?>, t: Throwable) {
                t.printStackTrace()
                pd!!.dismiss()
            }
        })
    }


    private fun deletePromotion(id: String) {
        pd = ProgressDialog(this)
        pd!!.setMessage("Profile is loading...")
        pd!!.setCancelable(false)
        pd!!.show()
        val api: Api = RetrofitClient.client!!.create(Api::class.java)
        val call: Call<ResponseDeletePromotion>? = api.deletePromotion(
            "Bearer " + WS_URL_PARAMS.createJWT(WS_URL_PARAMS.issuer, WS_URL_PARAMS.subject), WS_URL_PARAMS.access_key,id)
        call!!.enqueue(object : Callback<ResponseDeletePromotion?> {
            override fun onResponse(call: Call<ResponseDeletePromotion?>, response: Response<ResponseDeletePromotion?>) {

                Log.e("responseDelete", Gson().toJson(response.body()))

                if (pd!!.isShowing) {
                    pd!!.dismiss()
                }

                if (!(response.body() == null || !response.isSuccessful)) {

                    if (response.body()!!.error) {


                        Toast.makeText(this@SharePromotionActivity, response.body()!!.message, Toast.LENGTH_SHORT).show()

                    } else {

                        dialog!!.dismiss()
                        val intent = Intent(this@SharePromotionActivity,NewPromotionActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                        finish()
                    }

                } else {

                    Toast.makeText(this@SharePromotionActivity, resources.getString(R.string.error_admin), Toast.LENGTH_SHORT).show()

                }
            }

            override fun onFailure(call: Call<ResponseDeletePromotion?>, t: Throwable) {
                t.printStackTrace()
                pd!!.dismiss()
            }
        })
    }


    private fun addToFavourite(sid: String,promotionId:String,status:String) {
        val api: Api = RetrofitClient.client!!.create(Api::class.java)
        val call: Call<ResponseAddToFavPromotion>? = api.addToFavourite(
            "Bearer " + WS_URL_PARAMS.createJWT(WS_URL_PARAMS.issuer, WS_URL_PARAMS.subject), WS_URL_PARAMS.access_key,sid,promotionId,status)
        call!!.enqueue(object : Callback<ResponseAddToFavPromotion?> {
            override fun onResponse(call: Call<ResponseAddToFavPromotion?>, response: Response<ResponseAddToFavPromotion?>) {

                Log.e("responseFavourite", Gson().toJson(response.body()))

                if (!(response.body() == null || !response.isSuccessful)) {

                    if (response.body()!!.error) {


                        Toast.makeText(this@SharePromotionActivity, response.body()!!.message, Toast.LENGTH_SHORT).show()

                    } else {

                        Toast.makeText(this@SharePromotionActivity, "Added Successfully...", Toast.LENGTH_SHORT).show()
                    }

                } else {

                    Toast.makeText(this@SharePromotionActivity, resources.getString(R.string.error_admin), Toast.LENGTH_SHORT).show()

                }
            }

            override fun onFailure(call: Call<ResponseAddToFavPromotion?>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }

}