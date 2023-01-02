package com.arp.citysipspreadster.activites

import android.annotation.SuppressLint
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.arp.citysipspreadster.R
import com.arp.citysipspreadster.adapter.AllDialogOnBoardingAdapter
import com.arp.citysipspreadster.adapter.PromotionCategoryAdapter
import com.arp.citysipspreadster.adapter.PromotionListAdapter
import com.arp.citysipspreadster.adapter.PromotionSearchListAdapter
import com.arp.citysipspreadster.databinding.ActivityNewPromotionBinding
import com.arp.citysipspreadster.model.AllDialogData
import com.arp.citysipspreadster.model.deshboard.*
import com.arp.citysipspreadster.utils.Api
import com.arp.citysipspreadster.utils.RetrofitClient
import com.arp.citysipspreadster.utils.SessionManager
import com.arp.citysipspreadster.utils.WS_URL_PARAMS
import com.google.android.material.tabs.TabLayout
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class NewPromotionActivity : AppCompatActivity(), PromotionCategoryAdapter.OnItemClickListner,
    PromotionListAdapter.OnItemClickListner, PromotionSearchListAdapter.OnItemClickListner {

    private lateinit var binding: ActivityNewPromotionBinding
    var pd: ProgressDialog? = null
    private var sessionManager: SessionManager? = null
    var promationCategoryList = ArrayList<Category>()
    private var promotionCategoryAdapter: PromotionCategoryAdapter? = null

    var promationList = ArrayList<OfferBusiness>()
    private var promotionListAdapter: PromotionListAdapter? = null

    var promationSearchList = ArrayList<OfferBusinessSearch>()
    private var promotionSearchListAdapter: PromotionSearchListAdapter? = null

    companion object {
        var catposNew = "0"
    }

    var catId = ""

    private var dialog: Dialog? = null
    private var onBoardingViewPagerAdapter : AllDialogOnBoardingAdapter? = null
    private var tabLayout: TabLayout? = null
    private var onBoardingViewPager : ViewPager? = null
    var position = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewPromotionBinding.inflate(layoutInflater)
        setContentView(binding.root)


        sessionManager = SessionManager(this)

        if (!sessionManager!!.getNewPromotionDialog()) {


        dialog = Dialog(this@NewPromotionActivity, R.style.DialogStyle)
        dialog!!.setContentView(R.layout.raw_all_in_one_dialog)
        dialog!!.window!!.setBackgroundDrawableResource(android.R.color.transparent)

        val onBoardData : MutableList<AllDialogData> = ArrayList()
        onBoardData.add(AllDialogData("Choose ","Offer",R.drawable.dialog_one))
        onBoardData.add(AllDialogData("Create your ","Link",R.drawable.dialog_two))
        onBoardData.add(AllDialogData("Spread & ","Earn",R.drawable.dialog_three))


        onBoardingViewPager = dialog!!.findViewById<ViewPager>(R.id.screenPager)
        tabLayout =  dialog!!.findViewById<TabLayout>(R.id.tabIndicator)

        onBoardingViewPagerAdapter = AllDialogOnBoardingAdapter(context = this,onBoardData)
        onBoardingViewPager?.adapter = onBoardingViewPagerAdapter
        tabLayout?.setupWithViewPager(onBoardingViewPager)


        position = onBoardingViewPager!!.currentItem


        dialog!!.findViewById<Button>(R.id.btnWidthraw)
            .setOnClickListener {

                if (position < onBoardData.size) {
                    position++
                    onBoardingViewPager!!.currentItem = position

                }

                if (position == onBoardData.size) {
                    // sessionManager!!.setOnBoardScreen(true)
                   /* val intent = Intent(this,NewPromotionActivity::class.java)
                    startActivity(intent)*/
                    sessionManager!!.setNewPromotionDialog(true)
                    dialog!!.dismiss()

                }

            }

        dialog!!.findViewById<Button>(R.id.btnCancel).setOnClickListener {

            dialog!!.dismiss()
        }

        tabLayout!!.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            @SuppressLint("ResourceAsColor", "SetTextI18n")
            override fun onTabSelected(tab: TabLayout.Tab?) {

                position = tab!!.position
                if(tab.position == onBoardData.size - 1) {

                    dialog!!.findViewById<Button>(R.id.btnWidthraw).text = "Next"
                    dialog!!.findViewById<Button>(R.id.btnWidthraw).setBackgroundResource(R.drawable.btn_sixtydp_corner_radius)
                    dialog!!.findViewById<Button>(R.id.btnWidthraw).setTextColor(Color.WHITE)

                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }
        })

        }

        onBackPressedDispatcher.addCallback(this,onBackInvokedCallBack)


       binding.toolbar.toolbarBack.setOnClickListener {
           val intent = Intent(this@NewPromotionActivity, DeshBoardActivity::class.java)
           startActivity(intent)
           finish()
       }

        binding.toolbar.imgNoti.setOnClickListener {
            val intent = Intent(this,NotificationActivty::class.java)
            startActivity(intent)
        }

        getPromotionCategory()


        binding.edtSearchView.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                filterCategory(p0.toString())
               // filter(p0.toString())
                /*if (p0.isNullOrEmpty()){
                   // getPromotionList(catId)
                }else {
                   // getPromotionSearchList(p0.toString())

                }*/

            }
        })


    }

    private fun getPromotionCategory() {
        val api: Api = RetrofitClient.client!!.create(Api::class.java)
        val call: Call<ResponsePromotionCategory>? = api.getPromotionCategory(
            "Bearer " + WS_URL_PARAMS.createJWT(WS_URL_PARAMS.issuer, WS_URL_PARAMS.subject),
            WS_URL_PARAMS.access_key
        )
        call!!.enqueue(object : Callback<ResponsePromotionCategory?> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(
                call: Call<ResponsePromotionCategory?>,
                response: Response<ResponsePromotionCategory?>
            ) {
                Log.e("responseFavRestaurent", Gson().toJson(response.body()))
                if (response.body() != null && response.isSuccessful) {
                    if (response.body()!!.error) {

                        Toast.makeText(
                            this@NewPromotionActivity,
                            response.body()!!.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {

                        promationCategoryList =
                            response.body()!!.categoryList as ArrayList<Category>

                        promotionCategoryAdapter = PromotionCategoryAdapter(
                            this@NewPromotionActivity,
                            promationCategoryList,
                            this@NewPromotionActivity
                        )

                        binding.rclrCategory.adapter = promotionCategoryAdapter

                        catposNew = "0"
                        val id = promationCategoryList[catposNew.toInt()].id
                        promotionCategoryAdapter!!.notifyDataSetChanged()
                        catId = id

                        getPromotionList(id)


                    }
                } else {

                    Toast.makeText(
                        this@NewPromotionActivity,
                        resources.getString(R.string.error_admin),
                        Toast.LENGTH_SHORT
                    ).show()

                }
            }

            override fun onFailure(call: Call<ResponsePromotionCategory?>, t: Throwable) {
                Log.e("error", t.message!!)
                t.printStackTrace()
            }
        })
    }

    private fun getPromotionList(catId: String) {
        val api: Api = RetrofitClient.client!!.create(Api::class.java)
        val call: Call<ResponsePromotionList>? = api.getPromotionCategoryList(
            "Bearer " + WS_URL_PARAMS.createJWT(WS_URL_PARAMS.issuer, WS_URL_PARAMS.subject),
            WS_URL_PARAMS.access_key,
            catId
        )
        call!!.enqueue(object : Callback<ResponsePromotionList?> {
            override fun onResponse(
                call: Call<ResponsePromotionList?>,
                response: Response<ResponsePromotionList?>
            ) {
                Log.e("responseDetailsList", Gson().toJson(response.body()))
                if (response.body() != null && response.isSuccessful) {
                    if (response.body()!!.error) {

                        binding.imgError.visibility = View.VISIBLE
                        binding.rclrCategoryList.visibility = View.GONE

                        Toast.makeText(
                            this@NewPromotionActivity,
                            response.body()!!.message,
                            Toast.LENGTH_SHORT
                        ).show()

                    } else {

                        promationList = response.body()!!.offerBusinessList as ArrayList<OfferBusiness>

                        binding.imgError.visibility = View.GONE
                        binding.rclrCategoryList.visibility = View.VISIBLE

                        promotionListAdapter = PromotionListAdapter(
                            this@NewPromotionActivity,
                            promationList,
                            this@NewPromotionActivity
                        )

                        binding.rclrCategoryList.adapter = promotionListAdapter


                    }
                } else {

                    Toast.makeText(
                        this@NewPromotionActivity,
                        resources.getString(R.string.error_admin),
                        Toast.LENGTH_SHORT
                    ).show()

                }
            }

            override fun onFailure(call: Call<ResponsePromotionList?>, t: Throwable) {
                Log.e("error", t.message!!)
                t.printStackTrace()
            }
        })
    }


    private fun getPromotionSearchList(keyword: String) {
        val api: Api = RetrofitClient.client!!.create(Api::class.java)
        val call: Call<ResponsePromotionSearch>? = api.getPromotionSearch(
            "Bearer " + WS_URL_PARAMS.createJWT(WS_URL_PARAMS.issuer, WS_URL_PARAMS.subject),
            WS_URL_PARAMS.access_key,
            keyword
        )
        call!!.enqueue(object : Callback<ResponsePromotionSearch?> {
            override fun onResponse(
                call: Call<ResponsePromotionSearch?>,
                response: Response<ResponsePromotionSearch?>
            ) {
                Log.e("responseFavRestaurent", Gson().toJson(response.body()))
                if (response.body() != null && response.isSuccessful) {
                    if (response.body()!!.error) {

                        binding.imgError.visibility = View.VISIBLE
                        binding.rclrCategoryList.visibility = View.GONE

                        Toast.makeText(
                            this@NewPromotionActivity,
                            response.body()!!.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {

                        promationSearchList = response.body()!!.offerBusinessSearch as ArrayList<OfferBusinessSearch>

                        binding.imgError.visibility = View.GONE
                        binding.rclrCategoryList.visibility = View.VISIBLE

                            promotionSearchListAdapter = PromotionSearchListAdapter(
                                this@NewPromotionActivity,
                                promationSearchList,
                                this@NewPromotionActivity)
                            binding.rclrCategoryList.adapter = promotionSearchListAdapter
                        promotionSearchListAdapter!!.updateSearchList(promationSearchList)



                    }
                } else {

                    Toast.makeText(
                        this@NewPromotionActivity,
                        resources.getString(R.string.error_admin),
                        Toast.LENGTH_SHORT
                    ).show()

                }
            }

            override fun onFailure(call: Call<ResponsePromotionSearch?>, t: Throwable) {
                Log.e("error", t.message!!)
                t.printStackTrace()
            }
        })
    }

    @SuppressLint("LongLogTag")
    override fun onItemClicked(postion: Int) {

        Log.e("Category Item Cliecked Id :", promationCategoryList[postion].id)
        catposNew = postion.toString()
        catId = promationCategoryList[postion].id
        getPromotionList(promationCategoryList[postion].id)
        promotionCategoryAdapter!!.updateList(promationCategoryList)

    }

    @SuppressLint("LongLogTag")
    override fun onPromotionListItemClicked(postion: Int) {
        Log.e("PromotionList Item Cliecked Id :", promationList[postion].businessOfferId )
        sessionManager!!.setPromotionId(promationList[postion].businessOfferId)
        val intent = Intent(this, NewPromotionDetailsActivity::class.java)
        intent.putExtra("businessOfferId", promationList[postion].businessOfferId)
        startActivity(intent)
    }

    override fun onPromotionSearchListItemClicked(postion: Int) {

        sessionManager!!.setPromotionId(promationList[postion].businessOfferId)
        val intent = Intent(this, NewPromotionDetailsActivity::class.java)
        intent.putExtra("businessOfferId", promationSearchList[postion].businessOfferId)
        startActivity(intent)

    }

    fun filterCategory(text: String) {
        val temp: ArrayList<OfferBusiness> = ArrayList<OfferBusiness>()
        if (promationList != null) {
            for (d in promationList) {
                if (d.businessName.toUpperCase().contains(text.uppercase(Locale.getDefault()))) {
                    temp.add(d)
                } else {
                    binding.imgError.visibility = View.VISIBLE
                }
            }
        }

        if (temp != null){

            binding.imgError.visibility = View.GONE
            promotionListAdapter!!.updateList(temp)
        }else{
            binding.imgError.visibility = View.VISIBLE
        }
        //update recyclerview
    }

    private  val onBackInvokedCallBack = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            val intent = Intent(this@NewPromotionActivity,DeshBoardActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}