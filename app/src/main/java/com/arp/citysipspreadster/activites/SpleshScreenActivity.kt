package com.arp.citysipspreadster.activites

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.arp.citysipspreadster.databinding.ActivitySpleshScreenBinding
import com.arp.citysipspreadster.utils.SessionManager

@Suppress("DEPRECATION")
class SpleshScreenActivity : AppCompatActivity() {


    private lateinit var binding: ActivitySpleshScreenBinding
    private var sessionManager : SessionManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySpleshScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sessionManager = SessionManager(this)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        Handler().postDelayed({
           if (sessionManager!!.checkLogin()) {

               val intent = Intent(this, DeshBoardActivity::class.java)
               intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
               startActivity(intent)
               finish()

           } else {

               val intent = Intent(this, OnBoardingActivity::class.java)
               intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
               startActivity(intent)
               finish()

           }
        }, 2000)


    }
}