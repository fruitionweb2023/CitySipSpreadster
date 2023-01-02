package com.arp.citysipspreadster.utils

import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.Toast
import com.arp.citysipspreadster.R
import com.arp.citysipspreadster.activites.*

class BottomButtonClickListner(var context: Context) : View.OnClickListener {
    override fun onClick(view: View) {
        when (view.id) {
            R.id.bb_home -> {
                context.startActivity(Intent(context, DeshBoardActivity::class.java))
            }
            R.id.bb_my_business -> {
                context.startActivity(Intent(context, DeshBoardActivity::class.java))
            }
            R.id.bb_order -> {
                context.startActivity(Intent(context, MyPromotionActivity::class.java))
            }
            R.id.bb_wallet -> {
              //  Toast.makeText(context, "Clicked Wallet...", Toast.LENGTH_SHORT).show()
                context.startActivity(Intent(context, MyWalletActivity::class.java))
            }
            R.id.bb_menu -> {
                //Toast.makeText(context, "Clicked...", Toast.LENGTH_SHORT).show()
                context.startActivity(Intent(context, MyAccountActivity::class.java))
               // context.startActivity(Intent(context, MyAccountActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK))
            }
        }
    }
}