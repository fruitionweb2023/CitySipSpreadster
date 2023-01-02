package com.arp.citysipspreadster.activites

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.Window
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.ViewPager
import com.arp.citysipspreadster.R
import com.arp.citysipspreadster.adapter.CustomOnBoardingViewPagerAdapter
import com.arp.citysipspreadster.adapter.onBoardingViewPagerAdapter
import com.arp.citysipspreadster.databinding.ActivityMainBinding
import com.arp.citysipspreadster.model.OnBoardingData
import com.arp.citysipspreadster.utils.SessionManager
import com.google.android.material.tabs.TabLayout
import com.mahdiasd.filepicker.FileModel
import com.mahdiasd.filepicker.FilePicker
import com.mahdiasd.filepicker.FilePickerListener
import com.mahdiasd.filepicker.PickerMode
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var onBoardingViewPagerAdapter: CustomOnBoardingViewPagerAdapter? = null
    private var tabLayout: TabLayout? = null
    private var onBoardingViewPager: ViewPager? = null
    var position = 0
    private var sessionManager: SessionManager? = null
    private var dialog: Dialog? = null


    var img2 = ""
    var name = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        var file: File? = null
        var body1: MultipartBody.Part? = null

        if (img2 != "") {

            file = File(img2)
            val requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file)
            val s = name + "_" + file.name
            val logo = s.replace(" ".toRegex(), "_")

            body1 = MultipartBody.Part.createFormData("image", logo, requestFile)

        }

        binding.btnImage.setOnClickListener {
            FilePicker(this, supportFragmentManager)
                .setMode(PickerMode.Video, PickerMode.Image)
                    //PickerMode.Audio,PickerMode.FILE
                .setDefaultMode(PickerMode.Image)
                .setMaxSelection(1)
                .setMaxEachFileSize(1 * 1000) // mean -> 1 mb
                .setMaxTotalFileSize(15 * 1000) // mean -> 15 mb
                .setCustomText("video", "audio")
                    //, "storage", "image", "openStorage"
                .setShowFileWhenClick(true)
                .setCardBackgroundColor(ContextCompat.getColor(this, R.color.white))
                .setDeActiveColor(ContextCompat.getColor(this, R.color.clr_BFBFBF))
                .setActiveColor(ContextCompat.getColor(this, R.color.clr_EA2A31))
                .setIcons(
                    videoIcon = ContextCompat.getDrawable(this, com.mahdiasd.filepicker.R.drawable.ic_video),
                    audioIcon = ContextCompat.getDrawable(this, com.mahdiasd.filepicker.R.drawable.ic_audio),
                   // imageIcon = ContextCompat.getDrawable(this, com.mahdiasd.filepicker.R.drawable.ic_image),
                  //  fileManagerIcon = ContextCompat.getDrawable(this, com.mahdiasd.filepicker.R.drawable.ic_file),
                )
                .setListener(object : FilePickerListener {
                    override fun selectedFiles(list: List<FileModel>?) {

                        if (list!!.isNotEmpty()) {

                            Log.e("File Path :", list[0].file.path)
                            img2 = list[0].file.path.toString()
                            name = list[0].file.name
                        }
                    }
                })
                .show()
        }

    }
}



/*

        dialog = Dialog(this@MainActivity, R.style.DialogStyle)
        dialog!!.setContentView(R.layout.raw_onboard_dialog)
        dialog!!.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        dialog!!.show()
        dialog!!.window!!.setLayout(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT)
        dialog!!.window!!.setGravity(Gravity.CENTER)

        tabLayout = findViewById(R.id.tabIndicator)


        val onBoardData : MutableList<OnBoardingData> = ArrayList()
        onBoardData.add(OnBoardingData("Welcome to ","Spreadster","Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor.",R.drawable.onboarding_screen_image))
        onBoardData.add(OnBoardingData("Be your ","Own Boss","Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor.",R.drawable.onboarding_screen_image))
        onBoardData.add(OnBoardingData("Spread More ","Earn More","Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor.",R.drawable.onboarding_screen_image))

        setOnBoardingViewPagerAdapter(onBoardData)

        position = onBoardingViewPager!!.currentItem

        dialog!!.findViewById<View>(R.id.screenPager)
        dialog!!.findViewById<View>(R.id.txtBtnNext).setOnClickListener {

            if (position < onBoardData.size) {
                position++
                onBoardingViewPager!!.currentItem = position

            }

            if (position == onBoardData.size) {
                // sessionManager!!.setOnBoardScreen(true)
              dialog!!.dismiss()

            }
        }

        dialog!!.findViewById<TabLayout>(R.id.tabIndicator)!!.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            @SuppressLint("ResourceAsColor", "SetTextI18n")
            override fun onTabSelected(tab: TabLayout.Tab?) {

                position = tab!!.position
              */
/*  if(tab.position == onBoardData.size - 1) {

                    dialog!!.findViewById<TextView>(R.id.txtBtnNext).text = "Get Started"
                    dialog!!.findViewById<TextView>(R.id.txtBtnNext).setBackgroundResource(R.drawable.btn_background)
                    dialog!!.findViewById<TextView>(R.id.txtBtnNext).setTextColor(Color.WHITE)

                } else{
                    dialog!!.findViewById<TextView>(R.id.txtBtnNext).text = "Next"
                    dialog!!.findViewById<TextView>(R.id.txtBtnNext).setBackgroundColor(Color.TRANSPARENT)
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        dialog!!.findViewById<TextView>(R.id.txtBtnNext).setTextColor(getColor(R.color.clr_EA2A31))
                    }

                }*//*

            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {



            }
        })
    }

    private fun setOnBoardingViewPagerAdapter(onBoardingData: List<OnBoardingData>) {
        onBoardingViewPager = dialog!!.findViewById<ViewPager>(R.id.screenPager)
        onBoardingViewPagerAdapter = CustomOnBoardingViewPagerAdapter(context = this,onBoardingData)
        onBoardingViewPager!!.adapter = onBoardingViewPagerAdapter
        dialog!!.findViewById<TabLayout>(R.id.tabIndicator).setupWithViewPager(onBoardingViewPager)
    }

    }
*/
