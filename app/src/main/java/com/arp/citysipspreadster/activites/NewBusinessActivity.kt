package com.arp.citysipspreadster.activites

import android.app.Dialog
import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.arp.citysipspreadster.R
import com.arp.citysipspreadster.adapter.CategoryListSearchAdapter
import com.arp.citysipspreadster.databinding.ActivityIndividualOnBoardListItemRegisterBinding
import com.arp.citysipspreadster.databinding.ActivityNewBusinessBinding
import com.arp.citysipspreadster.model.ResponseNewBusiness
import com.arp.citysipspreadster.utils.*
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NewBusinessActivity : AppCompatActivity(),
    CategoryListSearchAdapter.RecyclerViewItemClickListener {

    private lateinit var binding: ActivityNewBusinessBinding
    var pd: ProgressDialog? = null
    private var sessionManager: SessionManager? = null
    private var dialog: Dialog? = null

    var titleListAdapter: CategoryListSearchAdapter? = null
    var customDialog: CustomListViewDialogWithSearch? = null
    var category = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewBusinessBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sessionManager = SessionManager(this)

        binding.toolbar.toolbarBack.setOnClickListener {

            val homeActivity = Intent(applicationContext, IndividualOnBoarding::class.java)
            startActivity(homeActivity)
            finish()
        }

        binding.toolbar.imgNoti.setOnClickListener {
            val intent = Intent(this,NotificationActivty::class.java)
            startActivity(intent)
        }

        binding.txtSpreadsterId.text = sessionManager!!.getUserId()

        binding.txtSelectCategory.setOnClickListener {
            titleListAdapter =
                CategoryListSearchAdapter(
                    IndividualOnBoarding.individualOnBoardingCategoryList,
                    this@NewBusinessActivity
                )
            customDialog = CustomListViewDialogWithSearch(
                this@NewBusinessActivity,
                titleListAdapter,
                IndividualOnBoarding.individualOnBoardingCategoryList
            )
           // customDialog = CustomListViewDialogWithSearch(this@NewBusinessActivity, R.style.DialogStyle)
            customDialog!!.window!!.setBackgroundDrawableResource(android.R.color.transparent)

            customDialog!!.show()
            customDialog!!.setCanceledOnTouchOutside(false)

        }


        binding.btnSendInvitation.setOnClickListener {

            if (binding.edtName.text.toString().isEmpty()) {

                binding.edtName.error = "Please enter your name"

            } else if (binding.edtContectNumber.text.toString().isEmpty()) {

                binding.edtContectNumber.error = "Please enter your contect number"

            } else if (binding.edtEmail.text.toString().isEmpty()) {

                binding.edtEmail.error = "Please enter your name"

            } else if (binding.edtBusinessName.text.toString().isEmpty()) {

                binding.edtBusinessName.error = "Please enter your businessName"

            } else if (binding.txtSelectCategory.text.toString().isEmpty()) {

                binding.txtSelectCategory.error = "Please select category"

            } else if (binding.edtBusinessNumber.text.toString().isEmpty()) {

                binding.edtBusinessNumber.error = "Please enter business number"

            } else {

                registerBusiness(sessionManager!!.getUserId().toString(),binding.edtName.text.toString(),
                    binding.edtBusinessNumber.text.toString(),
                    binding.edtBusinessName.text.toString(),
                    category,
                    binding.edtContectNumber.text.toString(),
                    binding.edtEmail.text.toString())
            }
        }
    }

    private fun registerBusiness(
        sId: String,
        ownerName: String,
        businessNumber: String,
        businessName: String,
        cId: String,
        contectNumber: String,
        email: String
    ) {
        pd = ProgressDialog(this)
        pd!!.setMessage("Loading...")
        pd!!.setCancelable(false)
        pd!!.show()
        val api: Api = RetrofitClient.client!!.create(Api::class.java)
        val call: Call<ResponseNewBusiness>? = api.createNewBusiness(
            "Bearer " + WS_URL_PARAMS.createJWT(WS_URL_PARAMS.issuer, WS_URL_PARAMS.subject),
            WS_URL_PARAMS.access_key,
            sId,
            ownerName,
            businessNumber,
            businessName,
            cId,
            contectNumber,
            email
        )
        call!!.enqueue(object : Callback<ResponseNewBusiness?> {
            override fun onResponse(
                call: Call<ResponseNewBusiness?>,
                response: Response<ResponseNewBusiness?>
            ) {

                Log.e("responseCreateBusiness", Gson().toJson(response.body()))

                if (pd!!.isShowing) {
                    pd!!.dismiss()
                }

                if (!(response.body() == null || !response.isSuccessful)) {

                    if (response.body()!!.error) {


                        Toast.makeText(
                            this@NewBusinessActivity,
                            response.body()!!.message,
                            Toast.LENGTH_SHORT
                        ).show()

                    } else {

                        dialog = Dialog(
                            this@NewBusinessActivity,
                            R.style.DialogStyle
                        )
                        dialog!!.setContentView(R.layout.raw_verify_business_dialog)
                        dialog!!.window!!.setBackgroundDrawableResource(android.R.color.transparent)
                        dialog!!.show()

                        dialog!!.findViewById<View>(R.id.btnDone)
                            .setOnClickListener {

                                Toast.makeText(
                                    this@NewBusinessActivity,
                                    "Your Business created successfull...",
                                    Toast.LENGTH_SHORT
                                ).show()
                                val intent = Intent(
                                    this@NewBusinessActivity,
                                    IndividualOnBoarding::class.java
                                )
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                                startActivity(intent)
                                finish()
                                dialog!!.dismiss()

                            }
                    }

                } else {

                    Toast.makeText(
                        this@NewBusinessActivity,
                        resources.getString(R.string.error_admin),
                        Toast.LENGTH_SHORT
                    ).show()

                }
            }

            override fun onFailure(call: Call<ResponseNewBusiness?>, t: Throwable) {
                t.printStackTrace()
                pd!!.dismiss()
            }
        })
    }


    override fun clickOnPartyListItem(data: String?, id: String?) {

        binding.txtSelectCategory.text = data
        category = id.toString()

        if (customDialog!!.isShowing) {
            customDialog!!.dismiss()
        }

    }
}