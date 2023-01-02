package com.arp.citysipspreadster.activites

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.arp.citysipspreadster.R
import com.arp.citysipspreadster.adapter.MyPromotionArchiveAdapter
import com.arp.citysipspreadster.adapter.MyPromotionCategoryAdapter
import com.arp.citysipspreadster.adapter.MyPromotionOngoingAdapter
import com.arp.citysipspreadster.adapter.PromotionFargmentAdapter
import com.arp.citysipspreadster.databinding.ActivityMyPromotionBinding
import com.arp.citysipspreadster.model.deshboard.OfferBusiness
import com.arp.citysipspreadster.model.promotion.*
import com.arp.citysipspreadster.utils.*
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class MyPromotionActivity : AppCompatActivity(),
    MyPromotionOngoingAdapter.OnItemClickListner, MyPromotionArchiveAdapter.OnItemClickListner,
    View.OnClickListener, MyPromotionCategoryAdapter.OnItemClickListner {

    private lateinit var binding: ActivityMyPromotionBinding
    var pd: ProgressDialog? = null
    private var sessionManager: SessionManager? = null

    var promationCategoryList = ArrayList<Category>()
    private var promotionCategoryAdapter: MyPromotionCategoryAdapter? = null

    var promationList = ArrayList<MyPromotionOngoing>()
    private var promotionListAdapter: MyPromotionOngoingAdapter? = null

    var promationArchiveList = ArrayList<MyPromotionArchive>()
    private var promotionArchiveListAdapter: MyPromotionArchiveAdapter? = null

    private var bottomButtonClickListner: BottomButtonClickListner? = null


    companion object {
        var catpos = "0"
    }

    var catId: String = ""

    var btnOngoingClicked = "0"
    var isClickedOngoing = true

    var btnArchiveClicked = "0"
    var isClickedArchive = true
    var flag = ""

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyPromotionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sessionManager = SessionManager(this)
        flag = intent.getStringExtra("flag").toString()

        binding.toolbar.toolbarBack.setOnClickListener {

           if (flag == "1") {
               val homeActivity = Intent(applicationContext, MyAccountActivity::class.java)
               startActivity(homeActivity)
               finish()
           } else {

               val homeActivity = Intent(applicationContext, DeshBoardActivity::class.java)
               startActivity(homeActivity)
               finish()

           }
        }

        binding.toolbar.imgNoti.setOnClickListener {
            val intent = Intent(this,NotificationActivty::class.java)
            startActivity(intent)
        }

        onBackPressedDispatcher.addCallback(this,onBackInvokedCallBack)


        bottomButtonClickListner = BottomButtonClickListner(this)
        binding.bottomnavigation.bbImgOrder.setColorFilter(resources.getColor(R.color.clr_EA2A31))
        binding.bottomnavigation.bbHome.setOnClickListener(BottomButtonClickListner(this))
        binding.bottomnavigation.bbWallet.setOnClickListener(BottomButtonClickListner(this))
        binding.bottomnavigation.bbMyBusiness.setOnClickListener(BottomButtonClickListner(this))
        binding.bottomnavigation.bbOrder.setOnClickListener(BottomButtonClickListner(this))
        binding.bottomnavigation.bbMenu.setOnClickListener(BottomButtonClickListner(this))

        /* promotionFargmentAdapter = PromotionFargmentAdapter(supportFragmentManager,lifecycle)
         binding.viewPagerPromotion.adapter = promotionFargmentAdapter

         TabLayoutMediator(binding.tabPromotion,binding.viewPagerPromotion){ tab, position ->

             when(position) {
                 0 -> {

                     tab.text = "OnGoing"

                 }
                 1 -> {

                     tab.text = "Archived"

                 }
             }
         }.attach()*/

        getPromotionCategory(sessionManager!!.getUserId().toString())

        binding.btnOngoing.setOnClickListener(this)
        binding.btnArchive.setOnClickListener(this)

        binding.edtSearchView.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {

               if (isClickedOngoing) {

                   filterCategory(p0.toString(),"1")

               } else {

                   filterCategory(p0.toString(),"2")
               }


            }
        })

    }


    private fun getPromotionCategory(sId : String) {
        val api: Api = RetrofitClient.client!!.create(Api::class.java)
        val call: Call<MypromotionCategoryList>? = api.getMyPromotionCategory(
            "Bearer " + WS_URL_PARAMS.createJWT(WS_URL_PARAMS.issuer, WS_URL_PARAMS.subject),
            WS_URL_PARAMS.access_key,sId)
        call!!.enqueue(object : Callback<MypromotionCategoryList?> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(
                call: Call<MypromotionCategoryList?>,
                response: Response<MypromotionCategoryList?>
            ) {
                Log.e("responseCategory", Gson().toJson(response.body()))
                if (response.body() != null && response.isSuccessful) {
                    if (response.body()!!.error) {

                       /* Toast.makeText(
                            this@MyPromotionActivity,
                            response.body()!!.message,
                            Toast.LENGTH_SHORT
                        ).show()*/
                    } else {

                        promationCategoryList =
                            response.body()!!.categoryList as ArrayList<Category>

                        promotionCategoryAdapter = MyPromotionCategoryAdapter(
                            this@MyPromotionActivity,
                            promationCategoryList,
                            this@MyPromotionActivity
                        )

                        binding.rclrCategory.adapter = promotionCategoryAdapter

                        catpos = "0"
                        val id = promationCategoryList[catpos.toInt()].id
                        catId = id
                        promotionCategoryAdapter!!.notifyDataSetChanged()
                        getPromotionList(sessionManager!!.getUserId()!!, catId, "1")
                    }
                } else {

                    Toast.makeText(
                        this@MyPromotionActivity,
                        resources.getString(R.string.error_admin),
                        Toast.LENGTH_SHORT
                    ).show()

                }
            }

            override fun onFailure(call: Call<MypromotionCategoryList?>, t: Throwable) {
                Log.e("error", t.message!!)
                t.printStackTrace()
            }
        })

    }

    private fun getPromotionList(sId: String, catId: String, bool: String) {

        val api: Api = RetrofitClient.client!!.create(Api::class.java)
        val call: Call<ResponseMyPromotion>? = api.getMyPromotion(
            "Bearer " + WS_URL_PARAMS.createJWT(WS_URL_PARAMS.issuer, WS_URL_PARAMS.subject),
            WS_URL_PARAMS.access_key,
            sId, catId
        )
        call!!.enqueue(object : Callback<ResponseMyPromotion?> {
            override fun onResponse(
                call: Call<ResponseMyPromotion?>,
                response: Response<ResponseMyPromotion?>
            ) {
                Log.e("responseDetailsList", Gson().toJson(response.body()))
                if (response.body() != null && response.isSuccessful) {
                    if (response.body()!!.error) {


                      /*  Toast.makeText(
                            this@MyPromotionActivity,
                            response.body()!!.message,
                            Toast.LENGTH_SHORT
                        ).show()*/


                        binding.imgError.visibility = View.VISIBLE
                        binding.rclrList.visibility = View.GONE

                    } else {


                        binding.imgError.visibility = View.GONE
                        binding.rclrList.visibility = View.VISIBLE


                        if (bool == "1") {

                            promationList =
                                response.body()!!.myPromotionOngoingList as ArrayList<MyPromotionOngoing>
                            promotionListAdapter = MyPromotionOngoingAdapter(
                                this@MyPromotionActivity,
                                promationList,
                                this@MyPromotionActivity
                            )

                            binding.rclrList.adapter = promotionListAdapter
//                            promotionListAdapter!!.notifyDataSetChanged()

                            Log.e("responseclick", "true")

                        } else {

                            promationArchiveList =
                                response.body()!!.myPromotionArchiveList as ArrayList<MyPromotionArchive>
                            promotionArchiveListAdapter = MyPromotionArchiveAdapter(
                                this@MyPromotionActivity,
                                promationArchiveList,
                                this@MyPromotionActivity
                            )

                            binding.rclrList.adapter = promotionArchiveListAdapter
//                            promotionArchiveListAdapter!!.notifyDataSetChanged()
                            Log.e("responseclick", "false")

                        }


                    }
                } else {

                    Toast.makeText(
                        this@MyPromotionActivity,
                        resources.getString(R.string.error_admin),
                        Toast.LENGTH_SHORT
                    ).show()

                }
            }

            override fun onFailure(call: Call<ResponseMyPromotion?>, t: Throwable) {
                Log.e("error", t.message!!)
                t.printStackTrace()
            }
        })
    }


    override fun onItemClicked(postion: Int) {

//        Toast.makeText(this, "my promotion $postion", Toast.LENGTH_SHORT).show()
        catId = promationCategoryList[postion].id
        catpos = postion.toString()
        Log.e("BtnClickedCategory :", catId)
        promotionCategoryAdapter!!.updateList(promationCategoryList)

        getPromotionList(sessionManager!!.getUserId()!!, catId, "1")
    }

    override fun onPromotionListItemClicked(postion: Int) {

        sessionManager!!.setPromotionId(promationList[postion].businessOfferId)
        val intent = Intent(this, SharePromotionActivity::class.java)
        intent.putExtra("businessOfferId", promationList[postion].businessOfferId)
        intent.putExtra("flag", "1")
        startActivity(intent)


    }

    override fun onPromotionArchiveListItemClicked(postion: Int) {

        sessionManager!!.setPromotionId(promationArchiveList[postion].businessOfferId)
        val intent = Intent(this, SharePromotionActivity::class.java)
        intent.putExtra("businessOfferId", promationArchiveList[postion].businessOfferId)
        intent.putExtra("flag", "1")
        startActivity(intent)

    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onClick(p0: View?) {

        when (p0!!.id) {

            R.id.btnOngoing -> {


                isClickedOngoing = true
                btnOngoingClicked = "1"
                binding.btnOngoing.background =
                    resources.getDrawable(R.drawable.btn_sixtydp_corner_radius)
                binding.btnOngoing.setTextColor(getColor(R.color.clr_FFFFFF))
                binding.btnArchive.setBackgroundColor(Color.TRANSPARENT)
                binding.btnArchive.setTextColor(getColor(R.color.clr_EA2A31))



                getPromotionList(sessionManager!!.getUserId()!!, catId, "1")

            }
            R.id.btnArchive -> {


                isClickedOngoing = false
                btnOngoingClicked = "2"
                binding.btnOngoing.setBackgroundColor(Color.TRANSPARENT)
                binding.btnOngoing.setTextColor(getColor(R.color.clr_EA2A31))
                binding.btnArchive.background =
                    resources.getDrawable(R.drawable.btn_sixtydp_corner_radius)
                binding.btnArchive.setTextColor(getColor(R.color.clr_FFFFFF))


                getPromotionList(sessionManager!!.getUserId()!!, catId, "2")
            }
        }
    }

    fun filterCategory(text: String,btnClicked:String) {
        val temp: ArrayList<MyPromotionOngoing> = ArrayList<MyPromotionOngoing>()
        val tempArchived: ArrayList<MyPromotionArchive> = ArrayList<MyPromotionArchive>()

        if (btnClicked == "1") {
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
        } else {

            if (promationArchiveList != null) {
                for (i in promationArchiveList) {
                    if (i.businessName.toUpperCase().contains(text.uppercase(Locale.getDefault()))) {
                        tempArchived.add(i)
                    } else {
                        binding.imgError.visibility = View.VISIBLE
                    }
                }
            }

            if (tempArchived != null){

                binding.imgError.visibility = View.GONE
                promotionArchiveListAdapter!!.updateList(tempArchived)
            }else{
                binding.imgError.visibility = View.VISIBLE
            }

            //update recyclerview

        }
    }

    private  val onBackInvokedCallBack = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            val intent = Intent(this@MyPromotionActivity,DeshBoardActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
