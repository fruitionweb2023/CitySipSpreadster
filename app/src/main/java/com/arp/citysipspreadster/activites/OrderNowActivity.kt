package com.arp.citysipspreadster.activites

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.MediaController
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.arp.citysipspreadster.R
import com.arp.citysipspreadster.databinding.ActivityOrderNowBinding
import com.arp.citysipspreadster.utils.SessionManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class OrderNowActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOrderNowBinding
    private var sessionManager: SessionManager? = null
    private var mediaControls: MediaController? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderNowBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sessionManager = SessionManager(this)

        binding.toolbar.toolbarBack.setOnClickListener {
            val intent = Intent(this,SpreadsterProfileActivity::class.java)
            startActivity(intent)
        }

        onBackPressedDispatcher.addCallback(this,onBackInvokedCallBack)

        val name = intent.getStringExtra("name")
        val sId = intent.getStringExtra("sId")
        val businessName = intent.getStringExtra("businessName")
        val type = intent.getStringExtra("type")
        val link = intent.getStringExtra("link")
        val title = intent.getStringExtra("title")
        val promocode = intent.getStringExtra("promocode")

        binding.txtUserName.text = name
        binding.txtSid.text = sId
        binding.txtBusinessName.text = businessName
        binding.txtDescription.text = title
        binding.txtCouponCode.text = promocode

        Log.e("OrderNowProfileImage", "${sessionManager!!.getProfileImage()}")
        if (sessionManager!!.getProfileImage() == "") {

            val options: RequestOptions = RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.profile_img)
                .error(R.drawable.profile_img)

            Glide.with(this@OrderNowActivity).load(sessionManager!!.getProfileImage()).apply(options).into(binding.profileImage)

        } else {

            val options: RequestOptions = RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.profile_img)
                .error(R.drawable.profile_img)

            Glide.with(this@OrderNowActivity).load(sessionManager!!.getProfileImage()).apply(options).into(binding.profileImage)
        }

        if (mediaControls == null) {
            mediaControls = MediaController(this)
            mediaControls!!.setAnchorView(this.binding.videoView)
        }

        binding.btnViewProfile.setOnClickListener {
            val intent = Intent(this,SpreadsterProfileActivity::class.java)
            startActivity(intent)
        }

        if (type == "image") {

            binding.videoView.visibility = View.GONE
            binding.imgPromotion.visibility = View.VISIBLE
            Glide.with(this).load(link)
                .error(R.drawable.ic_baseline_person)
                .placeholder(R.drawable.ic_baseline_person)
                .into(binding.imgPromotion)

        } else {

            binding.videoView.visibility = View.VISIBLE
            binding.imgPromotion.visibility = View.GONE

            binding.videoView.setMediaController(mediaControls)
            binding.videoView.setVideoPath(link)
            binding.videoView.requestFocus()
            binding.videoView.start()

        }

        binding.videoView.setOnCompletionListener {
            Toast.makeText(applicationContext, "Video completed",
                Toast.LENGTH_LONG).show()
        }


        binding.videoView.setOnErrorListener { _, _, _ ->
            Toast.makeText(applicationContext, "An Error Occurred " +
                    "While Playing Video !!!", Toast.LENGTH_LONG).show()
            false
        }
    }

    private  val onBackInvokedCallBack = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            val intent = Intent(this@OrderNowActivity,SpreadsterProfileActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}