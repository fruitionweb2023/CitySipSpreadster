package com.arp.citysipspreadster.utils

import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import com.arp.citysipspreadster.R
import android.widget.TextView
import com.arp.citysipspreadster.utils.WS_URL_PARAMS
import android.content.Intent
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.arp.citysipspreadster.activites.NotificationActivty
import java.lang.Exception
import java.util.*

open class BaseActivity : AppCompatActivity() {
   // var baseContext : Context? = null
    var activityBase : Context? = null
    var pd: ProgressDialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityBase = this
        pd = ProgressDialog(this)
        pd!!.setMessage("Loading...")
        pd!!.setCancelable(false)
    }

    fun toolbarPatch(activity: AppCompatActivity, title: String) {
        val title_lay = activity.findViewById<View>(R.id.toolbar)
        val toolbar = title_lay.findViewById<RelativeLayout>(R.id.toolbar)
        //activity.setSupportActionBar(toolbar)
     //   val toolbar_title = title_lay.findViewById<TextView>(R.id.toolbar_title)
        val cartitem = title_lay.findViewById<TextView>(R.id.cart_toolbar_added_item_count)
        val barimg = title_lay.findViewById<ImageView>(R.id.imgNoti)
       // toolbar_title.text = title
        if (WS_URL_PARAMS.cartItems.size > 0) {
            cartitem.visibility = View.VISIBLE
            cartitem.text = WS_URL_PARAMS.cartItems.size.toString()
        } else {
            cartitem.visibility = View.GONE
        }
        val img = title_lay.findViewById<ImageView>(R.id.imgNoti)
        //Objects.requireNonNull(activity.supportActionBar)?.setDisplayShowTitleEnabled(false)
        if (title == "") {
            barimg.visibility = View.VISIBLE
         //   toolbar_title.setPadding(30, 0, 0, 0)
            try {
            //    toolbar_title.text = title
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        img.setOnClickListener {
            val i = Intent(applicationContext, NotificationActivty::class.java)
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(i)
        }
        /*toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.onBackPressed();
            }
        });*/
    }

    fun CartDisplay(activity: AppCompatActivity) {
        val title_lay = activity.findViewById<View>(R.id.toolbar)
        val cartitem = title_lay.findViewById<TextView>(R.id.cart_toolbar_added_item_count)
        if (WS_URL_PARAMS.cartItems.size > 0) {
            cartitem.visibility = View.VISIBLE
            cartitem.text = WS_URL_PARAMS.cartItems.size.toString()
        } else {
            cartitem.visibility = View.GONE
        }
    }

    companion object {
        fun print(msg: Any?) {
            println(msg)
        }

        fun Toast(mContext: Context?, s: String?) {
            Toast.makeText(mContext, s, Toast.LENGTH_SHORT).show()
        }
    }
}