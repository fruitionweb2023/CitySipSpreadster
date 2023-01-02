package com.arp.citysipspreadster.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.viewpager.widget.PagerAdapter
import com.arp.citysipspreadster.R
import com.arp.citysipspreadster.model.AllDialogData

class AllDialogOnBoardingAdapter(private var context : Context, private var onBoardingDataList : List<AllDialogData>) : PagerAdapter() {
    override fun getCount(): Int {
       return onBoardingDataList.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view : View = LayoutInflater.from(context).inflate(R.layout.raw_all_in_one_dialog_item, null)

        val imageView : ImageView = view.findViewById(R.id.img_logo)
        val titleOne : TextView = view.findViewById(R.id.txt_title_one)
        val titleTwo : TextView = view.findViewById(R.id.txt_title_two)

        imageView.setImageResource(onBoardingDataList[position].imageUrl)
        titleOne.text = onBoardingDataList[position].titleOne
        titleTwo.text = onBoardingDataList[position].titleTwo

        container.addView(view)
        return view



    }
}