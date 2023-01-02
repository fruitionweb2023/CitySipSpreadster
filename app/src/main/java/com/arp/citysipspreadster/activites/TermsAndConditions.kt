package com.arp.citysipspreadster.activites

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import com.arp.citysipspreadster.R
import com.arp.citysipspreadster.databinding.ActivitySocialAccountsBinding
import com.arp.citysipspreadster.databinding.ActivityTermsAndConditionsBinding
import com.arp.citysipspreadster.utils.SessionManager

class TermsAndConditions : AppCompatActivity() {

    private lateinit var binding: ActivityTermsAndConditionsBinding
    var pd: ProgressDialog? = null
    private var sessionManager: SessionManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTermsAndConditionsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sessionManager = SessionManager(this)

        binding.toolbar.toolbarBack.setOnClickListener {
            val homeActivity = Intent(applicationContext, MyAccountActivity::class.java)
            startActivity(homeActivity)
            finish()
        }

        binding.toolbar.imgNoti.setOnClickListener {
            val intent = Intent(this,NotificationActivty::class.java)
            startActivity(intent)
        }


        binding.webView.settings.javaScriptEnabled = true

        binding.webView.webViewClient = object : WebViewClient() {
            @Deprecated("Deprecated in Java")
            override fun shouldOverrideUrlLoading(view: WebView?, url: String): Boolean {
                view?.loadUrl(url)
                return true
            }
        }
        binding.webView.loadUrl("http://medicaiditpark.com/city_slip/api-firebase/spredster/web_view_terms.php")
    }
}