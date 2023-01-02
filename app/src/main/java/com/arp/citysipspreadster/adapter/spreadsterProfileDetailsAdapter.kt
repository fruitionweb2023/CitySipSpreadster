package com.arp.citysipspreadster.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.arp.citysipspreadster.R
import com.arp.citysipspreadster.activites.MainActivity
import com.arp.citysipspreadster.activites.MassOnBoardingActivity
import com.arp.citysipspreadster.activites.MyPromotionActivity
import com.arp.citysipspreadster.databinding.RawMyPromotionsCategoryListBinding
import com.arp.citysipspreadster.databinding.RawPromotionsCategoryListBinding
import com.arp.citysipspreadster.databinding.RawSpreadsterOfferBinding
import com.arp.citysipspreadster.model.accounts.ProfileOffer
import com.arp.citysipspreadster.model.accounts.ProfileTrendingOffer
import com.arp.citysipspreadster.model.massOnboarding.OnboardCategory
import com.arp.citysipspreadster.model.promotion.Category
import com.arp.citysipspreadster.utils.SessionManager
import com.bumptech.glide.Glide

class spreadsterProfileDetailsAdapter(var context: Context, private var users: List<ProfileTrendingOffer>, private var onItemClickListner: OnItemClickListner) :
    RecyclerView.Adapter<spreadsterProfileDetailsAdapter.UsersViewHolder?>() {
     private lateinit var sessionManager: SessionManager
    var index = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.raw_spreadster_offer, parent, false)
        return UsersViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: UsersViewHolder, position: Int) {
        val user: ProfileTrendingOffer = users[position]

        holder.binding.txtOffer.text = user.title
        holder.binding.txtBusinessName.text = user.businessName

        if (user.type == "image") {
            Glide.with(context).load(user.imageVideo)
                .error(R.drawable.bg_image)
                .placeholder(R.drawable.bg_image)
                .into(holder.binding.imgProfile)
        } else {

            Glide.with(context).load(R.drawable.video)
                .error(R.drawable.video)
                .placeholder(R.drawable.video)
                .into(holder.binding.imgProfile)

        }

        sessionManager = SessionManager(context)

        holder.itemView.setOnClickListener {
            onItemClickListner.onTrandingItemClicked(position)

        }
    }

    inner class UsersViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var binding: RawSpreadsterOfferBinding = RawSpreadsterOfferBinding.bind(itemView)

    }

    override fun getItemCount(): Int {
        return users.size
    }

    fun updateList(searchList : List<ProfileTrendingOffer>) {

        this.users = searchList
        notifyDataSetChanged()

    }

    interface OnItemClickListner {
        fun onTrandingItemClicked(postion: Int)
    }



}