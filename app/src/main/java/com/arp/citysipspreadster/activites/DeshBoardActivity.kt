package com.arp.citysipspreadster.activites

import android.Manifest
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.getSystemService
import androidx.core.view.isVisible
import com.arp.citysipspreadster.R
import com.arp.citysipspreadster.databinding.ActivityDeshBoardBinding
import com.arp.citysipspreadster.model.accounts.ResponseWidthrawMoney
import com.arp.citysipspreadster.model.deshboard.main.ResponseDeshBoardMain
import com.arp.citysipspreadster.utils.*
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.gson.Gson
import com.highsoft.highcharts.common.HIColor
import com.highsoft.highcharts.common.hichartsclasses.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class DeshBoardActivity : BaseActivity() {


    private var fusedLocationProvider: FusedLocationProviderClient? = null
    private var locationRequest: LocationRequest = LocationRequest.create().apply {
        interval = 30
        fastestInterval = 10
        priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
        maxWaitTime = 60

    }

    private var locationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val locationList = locationResult.locations
            if (locationList.isNotEmpty()) {
                //The last location in the list is the newest
                val location = locationList.last()
               /* Log.e("Lng",location.longitude.toString() )
                Toast.makeText(
                    this@DeshBoardActivity,
                    "Got Location: $location",
                    Toast.LENGTH_LONG
                )
                    .show()*/
            }
        }
    }

    private var   PERMISSION_REQUEST_CODE = 200;
    private lateinit var binding: ActivityDeshBoardBinding
    private var bottomButtonClickListner: BottomButtonClickListner? = null
    private var doubleBackToExitPressedOnce : Boolean = false
   // var pd: ProgressDialog? = null
    private var dialog: Dialog? = null

    private var sessionManager: SessionManager? = null

    var earning : List<Int> = ArrayList<Int>()
    var reach : List<Int> = ArrayList<Int>()
    var date : List<String> = ArrayList<String>()

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDeshBoardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fusedLocationProvider = LocationServices.getFusedLocationProviderClient(this)


        if (!checkPermission()) {

            requestPermission();

        } else {

            Toast.makeText(this, "Permission already granted.", Toast.LENGTH_LONG).show();
            fusedLocationProvider!!.lastLocation.addOnCompleteListener(this) {

                val location : Location = it.result

                if (location != null) {
                    Log.e("Lat", location.latitude.toString())
                   // Toast.makeText(this, "Lat :"+location.latitude.toString(), Toast.LENGTH_SHORT).show()
                   // Toast.makeText(this, "Lng :"+location.longitude.toString(), Toast.LENGTH_SHORT).show()
                    Log.e("Lng", location.longitude.toString())
                    sessionManager!!.setLat(location.latitude.toString())
                    sessionManager!!.setLng(location.longitude.toString())
                }
            }

          /*  fusedLocationProvider!!.lastLocation.addOnSuccessListener(this) {
                Log.e("Lat", it.latitude.toString())
                Toast.makeText(this, "Lat :"+it.latitude.toString(), Toast.LENGTH_SHORT).show()
                Toast.makeText(this, "Lng :"+it.longitude.toString(), Toast.LENGTH_SHORT).show()
                Log.e("Lng", it.longitude.toString())
            }*/

        }

        toolbarPatch(this,"")


        bottomButtonClickListner = BottomButtonClickListner(this)
        binding.bottomnavigation.bbImgHome.setColorFilter(resources.getColor(R.color.clr_EA2A31))
        binding.bottomnavigation.bbHome.setOnClickListener(BottomButtonClickListner(this))
        binding.bottomnavigation.bbMyBusiness.setOnClickListener(BottomButtonClickListner(this))
        binding.bottomnavigation.bbOrder.setOnClickListener(BottomButtonClickListner(this))
        binding.bottomnavigation.bbWallet.setOnClickListener(BottomButtonClickListner(this))
        binding.bottomnavigation.bbMenu.setOnClickListener(BottomButtonClickListner(this))

        sessionManager = SessionManager(this)

        binding.toolbar.toolbarBack.isVisible = false

        binding.toolbar.imgNoti.setOnClickListener {
            val intent = Intent(this,NotificationActivty::class.java)
            startActivity(intent)
        }


        getDeshBoardDetails(sessionManager!!.getUserId().toString())

        //binding.lineChart.theme = "sand-signika";

        val options = HIOptions()

        val title = HITitle()
        title.text = ""
        options.title = title

        val legend = HILegend()
        legend.layout = "vertical"
        legend.align = "left"
        legend.verticalAlign = "top"
        legend.x = 240
        legend.y = 50
        legend.floating = true
        legend.borderWidth = 1
        legend.backgroundColor = HIColor.initWithHexValue("FFFFFF")
        options.legend = legend
        options.legend.enabled = false

        val xaxis = HIXAxis()
        val categories = ArrayList<String>()
        /*  for (i in date) {

              categories.add(i)
          }*/
        categories.add("13 Oct")
        categories.add("14 Oct")
        categories.add("15 Oct")
        categories.add("16 Oct")
        categories.add("17 Oct")
        categories.add("18 Oct")
        categories.add("19 Oct")
        xaxis.categories = categories

        options.xAxis = object : ArrayList<HIXAxis?>() {
            init {
                add(xaxis)
            }
        }

        val yaxis = HIYAxis()
        yaxis.title = HITitle()
        yaxis.title.text = ""
        options.yAxis = object : ArrayList<HIYAxis?>() {
            init {
                add(yaxis)
            }
        }

        val tooltip = HITooltip()
        tooltip.shared = true
        tooltip.valueSuffix = "units"
        options.tooltip = tooltip

        val credits = HICredits()
        credits.enabled = false
        options.credits = credits

        val plotOptions = HIPlotOptions()
        plotOptions.areaspline = HIAreaspline()
        plotOptions.areaspline.fillOpacity = 0.5
        options.plotOptions = plotOptions

        val areaspline1 = HIAreaspline()
        areaspline1.name = "Reach"
        val areaspline1Data = ArrayList<Number>()

        /* for (j in reach) {

             areaspline1Data.add(j)
         }*/
        areaspline1Data.add(5185)
        areaspline1Data.add(4573)
        areaspline1Data.add(6712)
        areaspline1Data.add(8354)
        areaspline1Data.add(8054)
        areaspline1Data.add(1668)
        areaspline1Data.add(8994)
        areaspline1.data = areaspline1Data

        val areaspline2 = HIAreaspline()
        areaspline2.name = "Earning"
        val areaspline2Data = ArrayList<Number>()
        /* for (k in earning) {

             areaspline2Data.add(k)
         }*/
        areaspline2Data.add(2005)
        areaspline2Data.add(7096)
        areaspline2Data.add(5580)
        areaspline2Data.add(9623)
        areaspline2Data.add(6833)
        areaspline2Data.add(4576)
        areaspline2Data.add(5047)
        areaspline2.data = areaspline2Data

        options.series = ArrayList(listOf(areaspline1, areaspline2))
        val exporting = HIExporting()
        exporting.enabled = false
        options.exporting = exporting

        binding.lineChart.options = options
        binding.lineChart.reload();

        binding.llBtnIndividualOnboard.setOnClickListener {

            val intent = Intent(this,IndividualOnBoarding::class.java)
            startActivity(intent)

        }

        binding.llBtnMassOnboard.setOnClickListener {

            checkGpsPermission()

        }

        binding.llWallet.setOnClickListener {

            val intent = Intent(this,MyWalletActivity::class.java)
            startActivity(intent)
        }

        binding.llBtnNewPromotion.setOnClickListener {

            val intent = Intent(this,NewPromotionActivity::class.java)
            startActivity(intent)

        }

        binding.llBtnMyPromotion.setOnClickListener {

            val intent = Intent(this,MyPromotionActivity::class.java)
            startActivity(intent)
        }

        binding.btnWithdraw.setOnClickListener {

            dialog = Dialog(this@DeshBoardActivity, R.style.DialogStyle)
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

        binding.llOnBoardedBusiness.setOnClickListener {

            val intent = Intent(this,OnBoardBusinessActivity::class.java)
            intent.putExtra("flag","4")
            startActivity(intent)
        }

        binding.llReminderRequire.setOnClickListener {

            val intent = Intent(this,OnBoardBusinessActivity::class.java)
            intent.putExtra("flag","2")
            startActivity(intent)
        }

        binding.llInvitedBusiness.setOnClickListener {

            val intent = Intent(this,OnBoardBusinessActivity::class.java)
            intent.putExtra("flag","3")
            startActivity(intent)
        }

        binding.llBusinessOnBoard.setOnClickListener {

            val intent = Intent(this,OnBoardBusinessActivity::class.java)
            intent.putExtra("flag","1")
            startActivity(intent)
        }
    }

    private fun checkGpsPermission() {
        locationRequest = LocationRequest.create()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 5000
        locationRequest.fastestInterval = 3000

        val builder : LocationSettingsRequest.Builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)
            .setAlwaysShow(true)

        val locationSettingsResponseTask : Task<LocationSettingsResponse> = LocationServices.getSettingsClient(applicationContext)
            .checkLocationSettings(builder.build())

        locationSettingsResponseTask.addOnCompleteListener{
            try {
                val response : LocationSettingsResponse = it.getResult(ApiException::class.java)
                val intent = Intent(this, MassOnBoardingActivity::class.java)
                startActivity(intent)

            }
            catch (e : ApiException) {
               if (e.statusCode == LocationSettingsStatusCodes.RESOLUTION_REQUIRED) {
                   val resolvableApiException : ResolvableApiException = e as ResolvableApiException

                   try {
                       resolvableApiException.startResolutionForResult(this,101)

                   } catch (sendIntentException : IntentSender.SendIntentException) {

                       sendIntentException.printStackTrace()
                   }
               }

                if (e.statusCode == LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE) {
                    Toast.makeText(this, "Setting not available", Toast.LENGTH_SHORT).show()
                }
            }

        }
    }


    private fun getDeshBoardDetails(spreadsterId: String) {
        pd = ProgressDialog(this)
        pd!!.setMessage("loading...")
        pd!!.setCancelable(false)
        pd!!.show()
        val api: Api = RetrofitClient.client!!.create(Api::class.java)
        val call: Call<ResponseDeshBoardMain>? = api.getDeshBoardDetails(
            "Bearer " + WS_URL_PARAMS.createJWT(WS_URL_PARAMS.issuer, WS_URL_PARAMS.subject), WS_URL_PARAMS.access_key,spreadsterId)
        call!!.enqueue(object : Callback<ResponseDeshBoardMain?> {
            @SuppressLint("SetTextI18n")
            override fun onResponse(call: Call<ResponseDeshBoardMain?>, response: Response<ResponseDeshBoardMain?>) {

                Log.e("responseMain", Gson().toJson(response.body()))

                if (pd!!.isShowing) {
                    pd!!.dismiss()
                }

                if (!(response.body() == null || !response.isSuccessful)) {

                    if (!response.body()!!.error) {


                        Toast.makeText(this@DeshBoardActivity, response.body()!!.message, Toast.LENGTH_SHORT).show()

                    } else {


                        val data = response.body()!!.data
                        val btnData = data.myDashboard
                        val  graph = data.earningVsReach
                         earning = data.earningVsReach.earning
                         reach = data.earningVsReach.reach
                         date = data.earningVsReach.day

                        Log.e("data",btnData.activePromotion )

                        if (btnData.invitedBusiness == "") {
                            binding.txtInvitedBusiness.text = ""
                        } else {
                            binding.txtInvitedBusiness.text = btnData.invitedBusiness
                        }

                        if (btnData.onboardedBusiness == "") {
                            binding.txtOnBoardBusiness.text = ""
                        } else {
                            binding.txtOnBoardBusiness.text = btnData.onboardedBusiness
                        }

                        if (btnData.respondedBusiness == "") {
                            binding.txtRespondedBusiness.text = ""
                        } else {
                            binding.txtRespondedBusiness.text = btnData.respondedBusiness
                        }

                        if (btnData.reminderBusiness == "") {
                            binding.txtReminderRequire.text = ""
                        } else {
                            binding.txtReminderRequire.text = btnData.reminderBusiness
                        }

                        if (btnData.activePromotion == "") {
                            binding.txtActivePromotion.text = ""
                        } else {
                            binding.txtReminderRequire.text = btnData.activePromotion
                        }

                        if (btnData.inactivePromotion == "") {
                            binding.txtInActivePromotion.text = ""
                        } else {
                            binding.txtInActivePromotion.text = btnData.inactivePromotion
                        }

                        if (btnData.newPromotion == "") {
                            binding.txtNewPromotion.text = ""
                        } else {
                            binding.txtNewPromotion.text = btnData.newPromotion
                        }

                        if (btnData.archivePromotion == "") {
                            binding.txtArchivePromotion.text = ""
                        } else {
                            binding.txtArchivePromotion.text = btnData.archivePromotion
                        }

                        if (data.myWallet.toString() == "") {
                            binding.txtAmount.text = ""
                        } else {
                            binding.txtAmount.text = "₹" +data.myWallet.toString()
                        }

                        if (data.totalEarning == "") {
                            binding.txtTotalEarning.text = ""
                        } else {
                            binding.txtTotalEarning.text = "₹" +data.totalEarning
                        }

                        if (data.businessOnBoard == "") {
                            binding.txtBusinessOnBoard.text = ""
                        } else {
                            binding.txtBusinessOnBoard.text = data.businessOnBoard
                        }

                    }

                } else {

                    Toast.makeText(this@DeshBoardActivity, resources.getString(R.string.error_admin), Toast.LENGTH_SHORT).show()

                }
            }

            override fun onFailure(call: Call<ResponseDeshBoardMain?>, t: Throwable) {
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

                        dialog!!.dismiss()
                        Toast.makeText(this@DeshBoardActivity, response.body()!!.message, Toast.LENGTH_SHORT).show()

                    } else {

                        getDeshBoardDetails(sessionManager!!.getUserId().toString())
                        dialog!!.dismiss()

                    }

                } else {

                    Toast.makeText(this@DeshBoardActivity, resources.getString(R.string.error_admin), Toast.LENGTH_SHORT).show()

                }
            }

            override fun onFailure(call: Call<ResponseWidthrawMoney?>, t: Throwable) {
                t.printStackTrace()
                if (pd!!.isShowing) {
                    pd!!.dismiss()
                }
            }
        })
    }

    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            moveTaskToBack(true)
            return
        }

        this.doubleBackToExitPressedOnce = true
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show()

        Handler().postDelayed({ doubleBackToExitPressedOnce = false }, 2000)
    }


    @SuppressLint("MissingPermission")
    override fun onResume() {
        super.onResume()
       // getDeshBoardDetails(sessionManager!!.getUserId().toString())


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED
        ) {

            fusedLocationProvider?.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )
        }
    }

    override fun onPause() {
        super.onPause()

       // getDeshBoardDetails(sessionManager!!.getUserId().toString())
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            == PackageManager.PERMISSION_GRANTED
        ) {

            fusedLocationProvider?.removeLocationUpdates(locationCallback)
        }
    }

    private fun checkLocationPermission() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            ) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                AlertDialog.Builder(this)
                    .setTitle("Location Permission Needed")
                    .setMessage("This app needs the Location permission, please accept to use location functionality")
                    .setPositiveButton(
                        "OK"
                    ) { _, _ ->
                        //Prompt the user once explanation has been shown
                        requestLocationPermission()
                    }
                    .create()
                    .show()
            } else {
                // No explanation needed, we can request the permission.
                requestLocationPermission()
            }
        } else {

            checkBackgroundLocation()
        }
    }

    private fun checkBackgroundLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestBackgroundLocationPermission()
        }
    }

    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
            ),
            MY_PERMISSIONS_REQUEST_LOCATION
        )
    }

    private fun requestBackgroundLocationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION
                ),
                MY_PERMISSIONS_REQUEST_BACKGROUND_LOCATION
            )
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                MY_PERMISSIONS_REQUEST_LOCATION
            )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 101) {
            if (resultCode == RESULT_OK) {

                binding.llProgressDialog.visibility = View.VISIBLE

                binding.llProgressDialog.setBackgroundColor(Color.parseColor("#57000000"))

                Handler().postDelayed({
                    binding.llProgressDialog.visibility = View.GONE
                    val intent = Intent(this@DeshBoardActivity, MassOnBoardingActivity::class.java)
                    startActivity(intent)
                }, 4000)

               // Toast.makeText(this, "Now GPS is enable...", Toast.LENGTH_SHORT).show()


            }
            if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Please enable GPS...", Toast.LENGTH_SHORT).show()
                checkGpsPermission()
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSION_REQUEST_CODE -> if (grantResults.isNotEmpty()) {
                val locationAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED
                if (locationAccepted) {
                    Toast.makeText(this, "Permission Granted, Now you can access location data.", Toast.LENGTH_LONG).show()
                }

                else {
                    Toast.makeText(
                        this,
                        "Permission Denied, You cannot access location data.",
                        Toast.LENGTH_LONG
                    ).show()
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale(ACCESS_FINE_LOCATION)) {
                            showMessageOKCancel(
                                "You need to allow access the permissions"
                            ) { dialog, which ->
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    requestPermissions(
                                        arrayOf(ACCESS_FINE_LOCATION),
                                        PERMISSION_REQUEST_CODE
                                    )
                                }
                            }
                            return
                        }
                    }
                }
            }
        }
    }



    private fun checkPermission(): Boolean {
        val result = ContextCompat.checkSelfPermission(applicationContext, ACCESS_FINE_LOCATION)
        return result == PackageManager.PERMISSION_GRANTED && result == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(ACCESS_FINE_LOCATION),
            PERMISSION_REQUEST_CODE
        )
    }
    private fun showMessageOKCancel(message: String, okListener: DialogInterface.OnClickListener) {
        AlertDialog.Builder(this@DeshBoardActivity)
            .setMessage(message)
            .setPositiveButton("OK", okListener)
            .setNegativeButton("Cancel", null)
            .create()
            .show()
    }

    companion object {
        private const val MY_PERMISSIONS_REQUEST_LOCATION = 99
        private const val MY_PERMISSIONS_REQUEST_BACKGROUND_LOCATION = 66

    }
}