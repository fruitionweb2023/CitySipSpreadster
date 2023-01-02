package com.arp.citysipspreadster.adapter

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.arp.citysipspreadster.activites.ArchiveFragment
import com.arp.citysipspreadster.activites.MainActivity
import com.arp.citysipspreadster.activites.OnGoingFragment
import com.arp.citysipspreadster.model.countryList.Country
import com.arp.citysipspreadster.model.promotion.MyPromotionOngoing

class PromotionFargmentAdapter(fragmentManager:FragmentManager,lifecycle:Lifecycle) : FragmentStateAdapter(fragmentManager, lifecycle) {


    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {

        return when(position) {

            0 -> {

                OnGoingFragment()
            }
            1 -> {

                ArchiveFragment()

            } else -> {

                Fragment()
            }
        }
    }
}