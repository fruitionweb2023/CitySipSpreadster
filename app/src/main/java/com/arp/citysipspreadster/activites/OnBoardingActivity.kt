package com.arp.citysipspreadster.activites

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.arp.citysipspreadster.R
import com.arp.citysipspreadster.databinding.ActivityOnBoardingBinding
import com.arp.citysipspreadster.model.OnBoardingData
import com.arp.citysipspreadster.adapter.onBoardingViewPagerAdapter
import com.arp.citysipspreadster.utils.SessionManager
import com.google.android.material.tabs.TabLayout

class OnBoardingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOnBoardingBinding
    private var onBoardingViewPagerAdapter : onBoardingViewPagerAdapter? = null
    private var tabLayout: TabLayout? = null
    private var onBoardingViewPager : ViewPager? = null
    var position = 0
    private var sessionManager : SessionManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityOnBoardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        tabLayout = findViewById(R.id.tabIndicator)

        sessionManager = SessionManager(this)

//        if (!sessionManager!!.checkOnBoardScreen()) {
//
//            val intent = Intent(this,NumberVerificationActivity::class.java)
//            startActivity(intent)
//            finish()
//
//        }

        val onBoardData : MutableList<OnBoardingData> = ArrayList()
        onBoardData.add(OnBoardingData("Welcome to ","Spreadster","Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor.",R.drawable.onboarding_screen_image))
        onBoardData.add(OnBoardingData("Be your ","Own Boss","Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor.",R.drawable.onboarding_screen_image))
        onBoardData.add(OnBoardingData("Spread More ","Earn More","Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor.",R.drawable.onboarding_screen_image))

        setOnBoardingViewPagerAdapter(onBoardData)

        position = onBoardingViewPager!!.currentItem

        binding.txtBtnNext.setOnClickListener {

            if (position < onBoardData.size) {
                position++
                onBoardingViewPager!!.currentItem = position

            }

            if (position == onBoardData.size) {
               // sessionManager!!.setOnBoardScreen(true)
                val intent = Intent(this,NumberVerificationActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                finish()

            }
        }

        tabLayout!!.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            @SuppressLint("ResourceAsColor", "SetTextI18n")
            override fun onTabSelected(tab: TabLayout.Tab?) {

                position = tab!!.position
                if(tab.position == onBoardData.size - 1) {

                    binding.txtBtnNext.text = "Get Started"
                    binding.txtBtnNext.setBackgroundResource(R.drawable.btn_background)
                    binding.txtBtnNext.setTextColor(Color.WHITE)

                } else{
                    binding.txtBtnNext.text = "Next"
                    binding.txtBtnNext.setBackgroundColor(Color.TRANSPARENT)
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        binding.txtBtnNext.setTextColor(getColor(R.color.clr_EA2A31))
                    }

                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {



            }
        })
    }

    private fun setOnBoardingViewPagerAdapter(onBoardingData: List<OnBoardingData>) {
        onBoardingViewPager = findViewById(R.id.screenPager)
        onBoardingViewPagerAdapter = onBoardingViewPagerAdapter(context = this,onBoardingData)
        onBoardingViewPager!!.adapter = onBoardingViewPagerAdapter
        tabLayout?.setupWithViewPager(onBoardingViewPager)
    }

}