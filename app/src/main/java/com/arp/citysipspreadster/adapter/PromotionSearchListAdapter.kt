package com.arp.citysipspreadster.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.arp.citysipspreadster.R
import com.arp.citysipspreadster.databinding.RawNewPromotionCategoryListMenuBinding
import com.arp.citysipspreadster.databinding.RawPromotionsCategoryListBinding
import com.arp.citysipspreadster.model.deshboard.Category
import com.arp.citysipspreadster.model.deshboard.OfferBusiness
import com.arp.citysipspreadster.model.deshboard.OfferBusinessSearch
import com.arp.citysipspreadster.utils.SessionManager
import com.bumptech.glide.Glide

class PromotionSearchListAdapter(var context: Context, private var users: List<OfferBusinessSearch>, private var onItemClickListner: OnItemClickListner) :
    RecyclerView.Adapter<PromotionSearchListAdapter.UsersViewHolder?>() {
    private lateinit var sessionManager: SessionManager

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.raw_new_promotion_category_list_menu, parent, false)
        return UsersViewHolder(view)
    }

    override fun onBindViewHolder(holder: UsersViewHolder, position: Int) {
        val user: OfferBusinessSearch = users[position]

        holder.binding.txtBusinessName.text = user.businessName
        holder.binding.txtOffer.text = user.title
        holder.binding.txtPercentage.text = user.percentage

        sessionManager = SessionManager(context)

        holder.itemView.setOnClickListener {

            onItemClickListner.onPromotionSearchListItemClicked(position)
        }
    }

    inner class UsersViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var binding: RawNewPromotionCategoryListMenuBinding = RawNewPromotionCategoryListMenuBinding.bind(itemView)

    }

    override fun getItemCount(): Int {
        return users.size
    }

    fun updateSearchList(searchList : List<OfferBusinessSearch>) {

        this.users = searchList
        notifyDataSetChanged()

    }

    interface OnItemClickListner {
        fun onPromotionSearchListItemClicked(postion: Int)
    }

}