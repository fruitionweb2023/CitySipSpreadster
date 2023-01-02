package com.arp.citysipspreadster.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.arp.citysipspreadster.R
import com.arp.citysipspreadster.databinding.RawMyPromotionListBinding
import com.arp.citysipspreadster.databinding.RawNewPromotionCategoryListMenuBinding
import com.arp.citysipspreadster.databinding.RawPromotionsCategoryListBinding
import com.arp.citysipspreadster.model.deshboard.Category
import com.arp.citysipspreadster.model.deshboard.OfferBusiness
import com.arp.citysipspreadster.model.promotion.MyPromotionOngoing
import com.arp.citysipspreadster.utils.SessionManager
import com.bumptech.glide.Glide

class MyPromotionOngoingAdapter(var context: Context, private var users: List<MyPromotionOngoing>, private var onItemClickListner: OnItemClickListner) :
    RecyclerView.Adapter<MyPromotionOngoingAdapter.UsersViewHolder?>() {
    private lateinit var sessionManager: SessionManager

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.raw_my_promotion_list, parent, false)
        return UsersViewHolder(view)
    }

    override fun onBindViewHolder(holder: UsersViewHolder, position: Int) {
        val user: MyPromotionOngoing = users[position]

        holder.binding.txtBusinessName.text = user.businessName
        holder.binding.txtOffer.text = user.title


        sessionManager = SessionManager(context)

        holder.itemView.setOnClickListener {

            onItemClickListner.onPromotionListItemClicked(position)
        }
    }

    inner class UsersViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var binding: RawMyPromotionListBinding = RawMyPromotionListBinding.bind(itemView)

    }

    override fun getItemCount(): Int {
        return users.size
    }

    fun updateList(searchList : List<MyPromotionOngoing>) {

        this.users = searchList
        notifyDataSetChanged()

    }

    interface OnItemClickListner {
        fun onPromotionListItemClicked(postion: Int)
    }

}