package com.arp.citysipspreadster.activites

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.arp.citysipspreadster.R
import com.arp.citysipspreadster.databinding.ActivityFaqAnswerBinding
import com.arp.citysipspreadster.databinding.ActivitySpreadsterFaqsBinding
import com.arp.citysipspreadster.utils.SessionManager

class FaqAnswerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFaqAnswerBinding
    var pd: ProgressDialog? = null
    private var sessionManager: SessionManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFaqAnswerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sessionManager = SessionManager(this)

        binding.toolbar.toolbarBack.setOnClickListener {
            val homeActivity = Intent(applicationContext, SpreadsterFaqsActivity::class.java)
            startActivity(homeActivity)
            finish()
        }

        binding.toolbar.imgNoti.setOnClickListener {
            val intent = Intent(this,NotificationActivty::class.java)
            startActivity(intent)
        }


        binding.txtTitle.text = intent.getStringExtra("title").toString()
        binding.txtDescription.text = intent.getStringExtra("description").toString()

    }
}