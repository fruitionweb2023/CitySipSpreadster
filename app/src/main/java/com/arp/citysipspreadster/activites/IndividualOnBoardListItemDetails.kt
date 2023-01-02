package com.arp.citysipspreadster.activites

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.arp.citysipspreadster.databinding.ActivityIndividualOnBoardListItemDetailsBinding
import com.arp.citysipspreadster.utils.SessionManager
import kotlin.math.acos
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin


class IndividualOnBoardListItemDetails : AppCompatActivity() {

    private lateinit var binding: ActivityIndividualOnBoardListItemDetailsBinding
    var pd: ProgressDialog? = null
    private var sessionManager: SessionManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIndividualOnBoardListItemDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sessionManager = SessionManager(this)

        binding.toolbar.toolbarBack.setOnClickListener {
            val intent = Intent(this,IndividualOnBoarding::class.java)
            startActivity(intent)
        }

        binding.toolbar.imgNoti.setOnClickListener {
            val intent = Intent(this,NotificationActivty::class.java)
            startActivity(intent)
        }

        val newLat = intent.getDoubleExtra("latitude",0.0)
        val newLng = intent.getDoubleExtra("longitude",0.0)
        Log.e("Lat", sessionManager!!.getLat().toString())
        Log.e("Lat New", newLat.toString())
        Log.e("Lng", sessionManager!!.getLng().toString())
        Log.e("Lng New", newLng.toString())

        val dis: Double = distance(sessionManager!!.getLat()!!.toDouble(), sessionManager!!.getLng()!!.toDouble(), newLat, newLng)

        val roundoff = (dis * 100.0).roundToInt() / 100.0
        Log.e("dis", "$roundoff Km")

        binding.txtBusinessName.text = intent.getStringExtra("businessName")
        binding.txtPercentage.text = intent.getStringExtra("amount") + "%"
        binding.txtAddress.text = intent.getStringExtra("address")
       // binding.txtDistance.text = intent.getStringExtra("distance") + "Km"
        binding.txtDistance.text = roundoff.toString() + " Km"


        binding.txtGetDirection.setOnClickListener {

            val geoUri =
                "http://maps.google.com/maps?q=loc:" + intent.getDoubleExtra("latitude",0.0) + "," +  intent.getDoubleExtra("longitude",0.0) + " (" + intent.getStringExtra("businessName") + ")"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(geoUri))
            startActivity(intent)
        }

        binding.btnGetOnBoard.setOnClickListener {

            val intent = Intent(this,IndividualOnBoardListItemRegisterActivity::class.java)
            intent.putExtra("businessName",binding.txtBusinessName.text.toString())
            startActivity(intent)

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