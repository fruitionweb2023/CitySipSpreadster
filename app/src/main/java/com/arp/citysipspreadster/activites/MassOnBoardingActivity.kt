package com.arp.citysipspreadster.activites

import android.Manifest
import android.annotation.SuppressLint
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.SeekBar
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.viewpager.widget.ViewPager
import com.arp.citysipspreadster.R
import com.arp.citysipspreadster.adapter.AllDialogOnBoardingAdapter
import com.arp.citysipspreadster.adapter.MassOnBoardingCategoryListAdapter
import com.arp.citysipspreadster.databinding.ActivityMassOnBoardingBinding
import com.arp.citysipspreadster.model.AllDialogData
import com.arp.citysipspreadster.model.TempData
import com.arp.citysipspreadster.model.massOnboarding.*
import com.arp.citysipspreadster.utils.*
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.MPPointF
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.material.tabs.TabLayout
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.acos
import kotlin.math.cos
import kotlin.math.sin


class MassOnBoardingActivity : AppCompatActivity(), View.OnClickListener,
    MassOnBoardingCategoryListAdapter.OnItemClickListner, OnMapReadyCallback,
    GoogleMap.OnMarkerClickListener {

    var zoom: Float = 12f
    private lateinit var mMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var currentLatLong: LatLng
    var lat: Double = 0.0
    var long: Double = 0.0

    private var tempData = ArrayList<TempData>()

    var mList: ArrayList<OnboardBusinessLead>? = null

    private lateinit var binding: ActivityMassOnBoardingBinding
    var pd: ProgressDialog? = null
    private var sessionManager: SessionManager? = null

    var massOnBoardingCategoryList = ArrayList<OnboardCategory>()
    private var massOnBoardingCategoryListAdapter: MassOnBoardingCategoryListAdapter? = null
    private var bottomButtonClickListner: BottomButtonClickListner? = null

    private var circle: Circle? = null

    companion object {
        var catpos = "0"
        private const val LOCATION_REQUEST_CODE = 1
    }

    var catId: String = ""
    var btnClicked = "0"
    var isClickedMapView = true
    private var dialog: Dialog? = null
    private var onBoardingViewPagerAdapter: AllDialogOnBoardingAdapter? = null
    private var tabLayout: TabLayout? = null
    private var onBoardingViewPager: ViewPager? = null
    var position = 0
    var sliderValue = 0.0
    var flag = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMassOnBoardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sessionManager = SessionManager(this)



        if (!sessionManager!!.getMassOnBoardingDialog()) {

            dialog = Dialog(this@MassOnBoardingActivity, R.style.DialogStyle)
            dialog!!.setContentView(R.layout.raw_all_in_one_dialog)
            dialog!!.window!!.setBackgroundDrawableResource(android.R.color.transparent)
            dialog!!.show()

            val onBoardData: MutableList<AllDialogData> = java.util.ArrayList()
            onBoardData.add(AllDialogData("Select ", "Area Range", R.drawable.dialog_one))
            onBoardData.add(AllDialogData("Spread ", "Businesses", R.drawable.dialog_two))
            onBoardData.add(AllDialogData("Onboard & ", "Earn", R.drawable.dialog_three))


            onBoardingViewPager = dialog!!.findViewById<ViewPager>(R.id.screenPager)
            tabLayout = dialog!!.findViewById<TabLayout>(R.id.tabIndicator)

            onBoardingViewPagerAdapter = AllDialogOnBoardingAdapter(context = this, onBoardData)
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

                        sessionManager!!.setMassOnBoardingDialog(true)

                        dialog!!.dismiss()

                    }

                }

            dialog!!.findViewById<Button>(R.id.btnCancel).setOnClickListener {

                dialog!!.dismiss()
            }

            tabLayout!!.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                @SuppressLint("ResourceAsColor", "SetTextI18n")
                override fun onTabSelected(tab: TabLayout.Tab?) {

                    position = tab!!.position
                    if (tab.position == onBoardData.size - 1) {

                        dialog!!.findViewById<Button>(R.id.btnWidthraw).text = "Next"
                        dialog!!.findViewById<Button>(R.id.btnWidthraw)
                            .setBackgroundResource(R.drawable.btn_sixtydp_corner_radius)
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

        onBackPressedDispatcher.addCallback(this, onBackInvokedCallBack)

        binding.toolbar.toolbarBack.setOnClickListener {

            val homeActivity = Intent(applicationContext, DeshBoardActivity::class.java)
            startActivity(homeActivity)
            finish()

        }

        binding.toolbar.imgNoti.setOnClickListener {
            val intent = Intent(this, NotificationActivty::class.java)
            startActivity(intent)
        }

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)


        sessionManager = SessionManager(this)

        getPromotionCategory()


        binding.btnMapView.setOnClickListener(this)
        binding.btnChartView.setOnClickListener(this)




        binding.btnOnBoardingBusiness.setOnClickListener {

            val intent = Intent(this, InviteBusinessActivity::class.java)
            startActivity(intent)
        }


        binding.seekBarKm.progress = 0
        binding.seekBarKm.incrementProgressBy(1)
        binding.seekBarKm.max = 20

        binding.seekBarKm.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {

                if (p1 % 1 == 0) {
                    binding.txtKm.text = "$p1 Km"
                    circle!!.radius = p1.toDouble() * 2000
                    sliderValue = p1.toDouble() * 2000

                    if (32000 < sliderValue) {
                        zoom = 8.5f
                    } else if (16000 < sliderValue) {
                        zoom = 9f
                    } else if (8000 < sliderValue) {
                        zoom = 10f
                    } else if (4000 < sliderValue) {
                        zoom = 11f
                    } else {
                        zoom = 12f
                    }


                    tempData.clear()
                    mMap.clear()

                    myCurrentLocation(lat, long)

                    for (i in 0 until mList!!.size) {
                        val obl: OnboardBusinessLead = mList!![i]

                        val dis: Double =
                            distance(lat, long, obl.latitude.toDouble(), obl.longitude.toDouble())

                        Log.e("dis", "$dis - ${p1.toDouble()}")

                        if (sliderValue >= dis * 2000) {

                            tempData.add(
                                TempData(
                                    obl.latitude.toDouble(),
                                    obl.longitude.toDouble(),
                                    obl.name
                                )
                            )

                        }

                    }

                    markerSet()


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
                            this@MassOnBoardingActivity,
                            response.body()!!.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {

                        massOnBoardingCategoryList =
                            response.body()!!.onboardCategoryList as ArrayList<OnboardCategory>

                        massOnBoardingCategoryListAdapter = MassOnBoardingCategoryListAdapter(
                            this@MassOnBoardingActivity,
                            massOnBoardingCategoryList,
                            this@MassOnBoardingActivity
                        )

                        response.body()!!.onboardCategoryList[0]
                        binding.rclrCategory.adapter = massOnBoardingCategoryListAdapter

                        MyPromotionActivity.catpos = "0"
                        val id = massOnBoardingCategoryList[catpos.toInt()].id
                        catId = id
                        getMapDetails(id)
                        massOnBoardingCategoryListAdapter!!.notifyDataSetChanged()


                    }
                } else {

                    Toast.makeText(
                        this@MassOnBoardingActivity,
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
                            this@MassOnBoardingActivity,
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
                        /* val l: Legend = binding.pieChart.legend
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
                        this@MassOnBoardingActivity,
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


    private fun getMapDetails(catId: String) {
        val api: Api = RetrofitClient.client!!.create(Api::class.java)
        val call: Call<ResponseMap>? = api.getMapDetails(
            "Bearer " + WS_URL_PARAMS.createJWT(WS_URL_PARAMS.issuer, WS_URL_PARAMS.subject),
            WS_URL_PARAMS.access_key, catId
        )
        call!!.enqueue(object : Callback<ResponseMap?> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(
                call: Call<ResponseMap?>,
                response: Response<ResponseMap?>
            ) {
                Log.e("responseMap", Gson().toJson(response.body()))
                if (response.body() != null && response.isSuccessful) {
                    if (response.body()!!.error) {

                        Toast.makeText(
                            this@MassOnBoardingActivity,
                            response.body()!!.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {

                        mList =
                            response.body()!!.onboardBusinessLead as ArrayList<OnboardBusinessLead>

                        if (mList!!.isEmpty()) {
                            // mMap.clear()
                            myCurrentLocation(lat, long)
                            Toast.makeText(
                                this@MassOnBoardingActivity,
                                "No data found...",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {

                            tempData.clear()
                            for (i in 0 until mList!!.size) {
                                val obl: OnboardBusinessLead = mList!![i]

                                val dis: Double = distance(
                                    lat,
                                    long,
                                    obl.latitude.toDouble(),
                                    obl.longitude.toDouble()
                                )

                                if (sliderValue >= dis * 2000) {

                                    tempData.add(
                                        TempData(
                                            obl.latitude.toDouble(),
                                            obl.longitude.toDouble(),
                                            obl.name
                                        )
                                    )

                                }

                            }

                            markerSet()
                        }

                    }
                } else {

                    Toast.makeText(
                        this@MassOnBoardingActivity,
                        resources.getString(R.string.error_admin),
                        Toast.LENGTH_SHORT
                    ).show()

                }
            }

            override fun onFailure(call: Call<ResponseMap?>, t: Throwable) {
                Log.e("error", t.message!!)
                t.printStackTrace()
            }
        })

    }


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onClick(p0: View?) {
        when (p0!!.id) {

            R.id.btnMapView -> {


                isClickedMapView = true
                btnClicked = "1"
                binding.apply {
                    btnMapView.background =
                        resources.getDrawable(R.drawable.btn_sixtydp_corner_radius)
                    btnMapView.setTextColor(getColor(R.color.clr_FFFFFF))
                    btnChartView.setBackgroundColor(Color.TRANSPARENT)
                    btnChartView.setTextColor(getColor(R.color.clr_EA2A31))
                    pieChart.visibility = View.GONE
                    llMap.visibility = View.VISIBLE
                    llSeekBar.visibility = View.VISIBLE
                }


            }

            R.id.btnChartView -> {

                isClickedMapView = false
                btnClicked = "2"

                binding.apply {

                    btnMapView.setBackgroundColor(Color.TRANSPARENT)
                    btnMapView.setTextColor(getColor(R.color.clr_EA2A31))
                    btnChartView.background =
                        resources.getDrawable(R.drawable.btn_sixtydp_corner_radius)
                    btnChartView.setTextColor(getColor(R.color.clr_FFFFFF))
                    pieChart.visibility = View.VISIBLE
                    llMap.visibility = View.GONE
                    llSeekBar.visibility = View.GONE
                }

                getChart()

            }
        }
    }

    override fun onItemClicked(postion: Int) {
        Toast.makeText(this, "my promotion $postion", Toast.LENGTH_SHORT).show()
        catId = massOnBoardingCategoryList[postion].id
        catpos = postion.toString()
        Log.e("BtnClickedCategory :", catId)
        massOnBoardingCategoryListAdapter!!.updateList(massOnBoardingCategoryList)
        getMapDetails(massOnBoardingCategoryList[postion].id)

        mMap.clear()

        myCurrentLocation(lat, long)

    }

    private val onBackInvokedCallBack = object : OnBackPressedCallback(true) {

        override fun handleOnBackPressed() {
            val intent = Intent(this@MassOnBoardingActivity, DeshBoardActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onMapReady(p0: GoogleMap) {

        mMap = p0

        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isScrollGesturesEnabled = true
        mMap.uiSettings.isZoomGesturesEnabled = true
        mMap.uiSettings.setAllGesturesEnabled(true)
        mMap.setOnMarkerClickListener(this)

        setUpMenu()
    }

    @SuppressLint("MissingPermission")
    private fun setUpMenu() {

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            ActivityCompat.requestPermissions(
                this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_REQUEST_CODE
            )

            return
        }

        mMap.isMyLocationEnabled = true
        fusedLocationClient.lastLocation.addOnSuccessListener(this) {
            myCurrentLocation(it.latitude.toDouble(), it.longitude)
        }

    }

    private fun myCurrentLocation(latitude: Double, longitude: Double) {

        // lastLocation = it
        currentLatLong = LatLng(latitude, longitude)
        lat = latitude
        long = longitude
        placeMarkerOnMap(currentLatLong)
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLong, zoom))
        circle = mMap.addCircle(
            CircleOptions()
                .center(LatLng(latitude, longitude))
                .fillColor(resources.getColor(R.color.colorLightYellow))
                .radius(sliderValue)
        )
    }

    private fun placeMarkerOnMap(currentLocation: LatLng) {

        val markerOptations = MarkerOptions().position(currentLocation)
        markerOptations.title("$currentLocation")
        markerOptations.icon(BitmapDescriptorFactory.fromResource(R.drawable.current_location))
        mMap.addMarker(markerOptations)

    }


    private fun placeMarkerForResponseLocationsOnMap(currentLocation: LatLng, title: String) {

        val markerOptations = MarkerOptions().position(currentLocation)
        markerOptations.title(title)
        mMap.addMarker(markerOptations)

    }

    override fun onMarkerClick(p0: Marker) = false

    private fun markerSet() {
        for (m in 0 until tempData.size) {
            val temp: TempData = tempData[m]
//            if (temp.latitude != "" && temp.longitude != "") {

            val currentLatLong = LatLng(temp.lat.toDouble(), temp.long.toDouble())
            placeMarkerForResponseLocationsOnMap(currentLatLong, temp.title)
            // mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLong,12f))

//            }
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
