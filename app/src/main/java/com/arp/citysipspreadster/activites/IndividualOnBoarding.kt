package com.arp.citysipspreadster.activites

import android.annotation.SuppressLint
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.SeekBar
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.arp.citysipspreadster.R
import com.arp.citysipspreadster.adapter.*
import com.arp.citysipspreadster.databinding.ActivityIndividualOnBoardingBinding
import com.arp.citysipspreadster.databinding.ActivityMassOnBoardingBinding
import com.arp.citysipspreadster.model.AllDialogData
import com.arp.citysipspreadster.model.TempData
import com.arp.citysipspreadster.model.deshboard.OfferBusiness
import com.arp.citysipspreadster.model.individualOnBoard.OnboardBusinessLead
import com.arp.citysipspreadster.model.individualOnBoard.ResponseInduvidualListView
import com.arp.citysipspreadster.model.massOnboarding.OnboardCategory
import com.arp.citysipspreadster.model.massOnboarding.ResponseChartOnBoardMassPromotion
import com.arp.citysipspreadster.model.massOnboarding.ResponsemassOnboardingCategory
import com.arp.citysipspreadster.model.promotion.Category
import com.arp.citysipspreadster.model.promotion.MypromotionCategoryList
import com.arp.citysipspreadster.utils.*
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.MPPointF
import com.google.android.material.tabs.TabLayout
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.acos
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin


class IndividualOnBoarding : AppCompatActivity(), View.OnClickListener,
    IndividualOnBoardListViewAdapter.OnItemClickListner,
    IndividualOnBoardingCategoryListAdapter.OnItemClickListner {

    private lateinit var binding: ActivityIndividualOnBoardingBinding
    var pd: ProgressDialog? = null
    private var sessionManager: SessionManager? = null
    private var bottomButtonClickListner: BottomButtonClickListner? = null


    private var individualOnBoardingCategoryListAdapter: IndividualOnBoardingCategoryListAdapter? = null

    var listViewItem = ArrayList<OnboardBusinessLead>()
    var mList =  ArrayList<OnboardBusinessLead>()
    //private var tempData = ArrayList<TempData>()
    var sliderValue = 0.0
    var distance = ArrayList<Double>()

    private var individualOnBoardListViewAdapter: IndividualOnBoardListViewAdapter? = null


    companion object {
        var individualOnBoardingCategoryList = ArrayList<OnboardCategory>()
        var catpos = "0"
    }

    var catId: String = ""
    var roundoff = 0.0

    var btnClicked = "0"
    var isClicked = true

    private var dialog: Dialog? = null
    private var onBoardingViewPagerAdapter : AllDialogOnBoardingAdapter? = null
    private var tabLayout: TabLayout? = null
    private var onBoardingViewPager : ViewPager? = null
    var position = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIndividualOnBoardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sessionManager = SessionManager(this)
        binding.llSeekBar.visibility = View.GONE

        if (!sessionManager!!.getIndividualOnBoardingDialog()) {

        dialog = Dialog(this@IndividualOnBoarding, R.style.DialogStyle)
        dialog!!.setContentView(R.layout.raw_all_in_one_dialog)
        dialog!!.window!!.setBackgroundDrawableResource(android.R.color.transparent)

        val onBoardData : MutableList<AllDialogData> = java.util.ArrayList()
        onBoardData.add(AllDialogData("Reach to ","Location",R.drawable.dialog_one))
        onBoardData.add(AllDialogData("Select ","Businesses",R.drawable.dialog_two))
        onBoardData.add(AllDialogData("Onboard & ","Earn",R.drawable.dialog_three))


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
                    sessionManager!!.setIndividualOnBoardingDialog(true)
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

        bottomButtonClickListner = BottomButtonClickListner(this)
        binding.bottomnavigation.bbImgHome.setColorFilter(resources.getColor(R.color.clr_EA2A31))
        binding.bottomnavigation.bbHome.setOnClickListener(BottomButtonClickListner(this))
        binding.bottomnavigation.bbMyBusiness.setOnClickListener(BottomButtonClickListner(this))
        binding.bottomnavigation.bbOrder.setOnClickListener(BottomButtonClickListner(this))
        binding.bottomnavigation.bbMenu.setOnClickListener(BottomButtonClickListner(this))
        binding.bottomnavigation.bbWallet.setOnClickListener(BottomButtonClickListner(this))

        onBackPressedDispatcher.addCallback(this,onBackInvokedCallBack)

        binding.toolbar.toolbarBack.setOnClickListener {

            val homeActivity = Intent(applicationContext, DeshBoardActivity::class.java)
            startActivity(homeActivity)
            finish()

        }

        binding.toolbar.imgNoti.setOnClickListener {
            val intent = Intent(this,NotificationActivty::class.java)
            startActivity(intent)
        }

        binding.pieChart.visibility = View.VISIBLE
        binding.rclrListView.visibility = View.GONE
        binding.imgError.visibility = View.GONE


        getPromotionCategory()

        getChart()


      binding.btnChartView.setOnClickListener(this)
      binding.btnListView.setOnClickListener(this)

        binding.btnAddNewBusiness.setOnClickListener {

            val intent = Intent(this,NewBusinessActivity::class.java)
            startActivity(intent)
        }

        binding.edtSearchView.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
               if (!isClicked) {
                   filterCategory(p0.toString())
               }
                // filter(p0.toString())
                /*if (p0.isNullOrEmpty()){
                   // getPromotionList(catId)
                }else {
                   // getPromotionSearchList(p0.toString())

                }*/

            }
        })

        individualOnBoardListViewAdapter = IndividualOnBoardListViewAdapter(
            this@IndividualOnBoarding,
            listViewItem,
            this@IndividualOnBoarding
        )

        binding.rclrListView.adapter = individualOnBoardListViewAdapter


        binding.seekBarKm.progress = 0
        binding.seekBarKm.incrementProgressBy(1)
        binding.seekBarKm.max = 20

        binding.seekBarKm.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {

                if (p1 % 1 == 0) {
                    binding.txtKm.text = "$p1 Km"
                    sliderValue = p1.toDouble() * 2000

                    mList.clear()
                    individualOnBoardListViewAdapter!!.updateList(mList)

                    for (i in 0 until listViewItem.size) {
                        val obl: OnboardBusinessLead = listViewItem[i]
                        val dis: Double = distance(sessionManager!!.getLat()!!.toDouble(), sessionManager!!.getLng()!!.toDouble(), obl.latitude.toDouble(), obl.longitude.toDouble())
                         roundoff = (dis * 100.0).roundToInt() / 100.0
                        Log.e("dis", "$roundoff - ${p1.toDouble()}")

                        if (sliderValue >= roundoff * 2000) {

                            mList.add(obl)

                            individualOnBoardListViewAdapter!!.updateList(mList)

                        } else {


                        }

                    }

                }
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {

            }

            override fun onStopTrackingTouch(p0: SeekBar?) {

            }
        })



    }

    private fun getPromotionCategory() {
        val api: Api = RetrofitClient.client!!.create(Api::class.java)
        val call: Call<ResponsemassOnboardingCategory>? = api.getCategoryMassOnboarding(
            "Bearer " + WS_URL_PARAMS.createJWT(WS_URL_PARAMS.issuer, WS_URL_PARAMS.subject),
            WS_URL_PARAMS.access_key
        )
        call!!.enqueue(object : Callback<ResponsemassOnboardingCategory?> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(
                call: Call<ResponsemassOnboardingCategory?>,
                response: Response<ResponsemassOnboardingCategory?>
            ) {
                Log.e("responseCategory", Gson().toJson(response.body()))
                if (response.body() != null && response.isSuccessful) {
                    if (response.body()!!.error) {

                        Toast.makeText(
                            this@IndividualOnBoarding,
                            response.body()!!.message,
                            Toast.LENGTH_SHORT
                        ).show()

                    } else {

                        individualOnBoardingCategoryList =
                            response.body()!!.onboardCategoryList as ArrayList<OnboardCategory>

                        individualOnBoardingCategoryListAdapter = IndividualOnBoardingCategoryListAdapter(
                            this@IndividualOnBoarding,
                            individualOnBoardingCategoryList,
                            this@IndividualOnBoarding
                        )

                        binding.rclrCategory.adapter = individualOnBoardingCategoryListAdapter

                        catpos = "0"
                        val id = individualOnBoardingCategoryList[catpos.toInt()].id
                        catId = id
                        getListView(catId)
                        individualOnBoardingCategoryListAdapter!!.notifyDataSetChanged()

                    }
                } else {

                    Toast.makeText(
                        this@IndividualOnBoarding,
                        resources.getString(R.string.error_admin),
                        Toast.LENGTH_SHORT
                    ).show()

                }
            }

            override fun onFailure(call: Call<ResponsemassOnboardingCategory?>, t: Throwable) {

                Log.e("error", t.message!!)
                t.printStackTrace()

            }
        })

    }

    private fun getChart() {
        val api: Api = RetrofitClient.client!!.create(Api::class.java)
        val call: Call<ResponseChartOnBoardMassPromotion>? = api.getChartDetails(
            "Bearer " + WS_URL_PARAMS.createJWT(WS_URL_PARAMS.issuer, WS_URL_PARAMS.subject),
            WS_URL_PARAMS.access_key
        )
        call!!.enqueue(object : Callback<ResponseChartOnBoardMassPromotion?> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(
                call: Call<ResponseChartOnBoardMassPromotion?>,
                response: Response<ResponseChartOnBoardMassPromotion?>
            ) {
                Log.e("responseChart", Gson().toJson(response.body()))
                if (response.body() != null && response.isSuccessful) {
                    if (response.body()!!.error) {

                        Toast.makeText(
                            this@IndividualOnBoarding,
                            response.body()!!.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {


                        val chartList = response.body()!!.onboardCategoryChart

                        binding.pieChart.setUsePercentValues(true)
                        binding.pieChart.description.isEnabled = false
                        binding.pieChart.setExtraOffsets(5f, 10f, 5f, 5f)
                        binding.pieChart.dragDecelerationFrictionCoef = 0.95f
                        binding.pieChart.isDrawHoleEnabled = true
                        binding.pieChart.setHoleColor(Color.WHITE)
                        binding.pieChart.centerText = response.body()!!.totalCount
                        binding.pieChart.setCenterTextSize(30f)
                        binding.pieChart.setTransparentCircleColor(Color.WHITE)
                        binding.pieChart.setTransparentCircleAlpha(110)
                        binding.pieChart.holeRadius = 58f
                        binding.pieChart.transparentCircleRadius = 61f
                        binding.pieChart.setDrawCenterText(true)
                        binding.pieChart.rotationAngle = 0f

                        // enable rotation of the chart by touch
                        binding.pieChart.isRotationEnabled = true
                        binding.pieChart.isHighlightPerTapEnabled = true

                        binding.pieChart.dragDecelerationFrictionCoef = 0.15f
                        binding.pieChart.transparentCircleRadius = 91f
                        binding.pieChart.animateY(1400, Easing.EaseInOutQuad)
                        binding.pieChart.legend.isEnabled = false

                        /*val l: Legend = binding.pieChart.legend
                        l.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
                        l.horizontalAlignment = Legend.LegendHorizontalAlignment.LEFT
                        l.orientation = Legend.LegendOrientation.HORIZONTAL
                        l.setDrawInside(false)
                        l.xEntrySpace = 4f
                        l.yEntrySpace = 0f
                        l.yOffset = 0f*/

                        val values = ArrayList<PieEntry>()

                        for (i in chartList) {
                            values.add(PieEntry(i.persantage.toFloat(), i.name))
                        }


                        val dataSet: PieDataSet = PieDataSet(values, "")
                        dataSet.sliceSpace = 2f
                        dataSet.selectionShift = 5f
                        dataSet.setDrawIcons(false)
                        dataSet.iconsOffset = MPPointF(0f, 40f)


                        // add a lot of colors
                        val colors: ArrayList<Int> = ArrayList<Int>()

                        for (j in chartList) {

                            if (j.bgColor == "") {
                                colors.add(Color.parseColor("#F06369"))
                            } else {
                                colors.add(Color.parseColor(j.bgColor))
                            }
                        }


                        dataSet.colors = colors

                        val data: PieData = PieData(dataSet)
                        data.setValueTextSize(8f)
                        data.setValueTextColor(Color.WHITE)

                        binding.pieChart.setEntryLabelTextSize(8f);
                        binding.pieChart.data = data


                    }
                } else {

                    Toast.makeText(
                        this@IndividualOnBoarding,
                        resources.getString(R.string.error_admin),
                        Toast.LENGTH_SHORT
                    ).show()

                }
            }

            override fun onFailure(call: Call<ResponseChartOnBoardMassPromotion?>, t: Throwable) {
                Log.e("error", t.message!!)
                t.printStackTrace()
            }
        })

    }


    private fun getListView(catId : String) {
        val api: Api = RetrofitClient.client!!.create(Api::class.java)
        val call: Call<ResponseInduvidualListView>? = api.getListView(
            "Bearer " + WS_URL_PARAMS.createJWT(WS_URL_PARAMS.issuer, WS_URL_PARAMS.subject),
            WS_URL_PARAMS.access_key,catId)
        call!!.enqueue(object : Callback<ResponseInduvidualListView?> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(
                call: Call<ResponseInduvidualListView?>,
                response: Response<ResponseInduvidualListView?>
            ) {
                Log.e("responseListView", Gson().toJson(response.body()))
                if (response.body() != null && response.isSuccessful) {
                    if (response.body()!!.error) {

                        Toast.makeText(
                            this@IndividualOnBoarding,
                            response.body()!!.message,
                            Toast.LENGTH_SHORT
                        ).show()

                        binding.imgError.visibility = View.VISIBLE
                        binding.rclrListView.visibility = View.GONE

                    } else {

                       if (btnClicked == "2") {

                           binding.imgError.visibility = View.GONE
                           binding.rclrListView.visibility = View.VISIBLE

                           listViewItem = response.body()!!.onboardBusinessLead as ArrayList<OnboardBusinessLead>



                           mList.clear()

                           for (i in 0 until listViewItem.size) {
                               val obl: OnboardBusinessLead = listViewItem[i]
                               val dis: Double = distance(sessionManager!!.getLat()!!.toDouble(), sessionManager!!.getLng()!!.toDouble(), obl.latitude.toDouble(), obl.longitude.toDouble())
                               roundoff = (dis * 100.0).roundToInt() / 100.0
                              // Log.e("dis", "$dis - ${p1.toDouble()}")

                               if (sliderValue >= roundoff * 2000) {

                                   mList.add(obl)

                                   individualOnBoardListViewAdapter!!.updateList(mList)

                               }

                           }

                       } else {
                           binding.rclrListView.visibility = View.GONE
                       }

                    }
                } else {

                    Toast.makeText(
                        this@IndividualOnBoarding,
                        resources.getString(R.string.error_admin),
                        Toast.LENGTH_SHORT
                    ).show()

                }
            }

            override fun onFailure(call: Call<ResponseInduvidualListView?>, t: Throwable) {

                Log.e("error", t.message!!)
                t.printStackTrace()

            }
        })

    }

  @RequiresApi(Build.VERSION_CODES.M)
  override fun onClick(p0: View?) {
      when (p0!!.id) {

          R.id.btnChartView -> {


              isClicked = true
              btnClicked = "1"
              binding.btnChartView.background =
                  resources.getDrawable(R.drawable.btn_sixtydp_corner_radius)
              binding.btnChartView.setTextColor(getColor(R.color.clr_FFFFFF))
              binding.btnListView.setBackgroundColor(Color.TRANSPARENT)
              binding.btnListView.setTextColor(getColor(R.color.clr_EA2A31))
              binding.pieChart.visibility = View.VISIBLE
              binding.rclrListView.visibility = View.GONE
              binding.imgError.visibility = View.GONE
              binding.llSeekBar.visibility = View.GONE

              getChart()
          }

          R.id.btnListView -> {

              isClicked = false
              btnClicked = "2"
              binding.btnChartView.setBackgroundColor(Color.TRANSPARENT)
              binding.btnChartView.setTextColor(getColor(R.color.clr_EA2A31))
              binding.btnListView.background =
                  resources.getDrawable(R.drawable.btn_sixtydp_corner_radius)
              binding.btnListView.setTextColor(getColor(R.color.clr_FFFFFF))
              binding.pieChart.visibility = View.GONE
              binding.rclrListView.visibility = View.VISIBLE
              binding.llSeekBar.visibility = View.VISIBLE
              getListView(catId)

          }
      }
  }

    override fun onListViewItemClicked(postion: Int) {

        val intent = Intent(this,IndividualOnBoardListItemDetails::class.java)
        intent.putExtra("businessName",listViewItem[postion].name)
        intent.putExtra("amount",listViewItem[postion].amount)
        intent.putExtra("address",listViewItem[postion].address)
        intent.putExtra("distance",listViewItem[postion].distance.toString())
        intent.putExtra("latitude",listViewItem[postion].latitude.toDouble())
        intent.putExtra("longitude",listViewItem[postion].longitude.toDouble())
        startActivity(intent)

    }

    override fun onindividualCategoryItemClicked(postion: Int) {

        catId = individualOnBoardingCategoryList[postion].id
        catpos = postion.toString()
       if (btnClicked == "2") {
           getListView(individualOnBoardingCategoryList[postion].id)
       }
        individualOnBoardingCategoryListAdapter!!.updateList(individualOnBoardingCategoryList)

    }

    fun filterCategory(text: String) {
        val temp: ArrayList<OnboardBusinessLead> = ArrayList<OnboardBusinessLead>()
        if (listViewItem != null) {
            for (d in listViewItem) {
                if (d.name.toUpperCase().contains(text.uppercase(Locale.getDefault()))) {
                    temp.add(d)
                } else {
                    binding.imgError.visibility = View.VISIBLE
                }
            }
        }

        if (temp != null){

            binding.imgError.visibility = View.GONE
            individualOnBoardListViewAdapter!!.updateList(temp)
        }else{
            binding.imgError.visibility = View.VISIBLE
        }
        //update recyclerview
    }

    private  val onBackInvokedCallBack = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            val intent = Intent(this@IndividualOnBoarding,DeshBoardActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun distance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val theta = lon1 - lon2
        var dist = (sin(deg2rad(lat1))
                * sin(deg2rad(lat2))
                + (cos(deg2rad(lat1))
                * cos(deg2rad(lat2))
                * cos(deg2rad(theta))))
        dist = acos(dist)
        dist = rad2deg(dist)
        dist *= 60
        return dist
    }

    private fun deg2rad(deg: Double): Double {
        return deg * Math.PI / 180.0
    }

    private fun rad2deg(rad: Double): Double {
        return rad * 180.0 / Math.PI
    }

}
