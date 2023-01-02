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
import com.arp.citysipspreadster.databinding.RawWalletHistoryBinding
import com.arp.citysipspreadster.model.accounts.Histroy
import com.arp.citysipspreadster.model.accounts.WithdrawRequest
import com.arp.citysipspreadster.model.deshboard.Category
import com.arp.citysipspreadster.model.deshboard.OfferBusiness
import com.arp.citysipspreadster.model.promotion.MyPromotionOngoing
import com.arp.citysipspreadster.utils.SessionManager
import com.bumptech.glide.Glide

class MyWalletWidthrawListAdapter(var context: Context, private var users: List<WithdrawRequest>, private var onItemClickListner: OnItemClickListner) :
    RecyclerView.Adapter<MyWalletWidthrawListAdapter.UsersViewHolder?>() {
    private lateinit var sessionManager: SessionManager

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.raw_wallet_history, parent, false)
        return UsersViewHolder(view)
    }

    override fun onBindViewHolder(holder: UsersViewHolder, position: Int) {
        val user: WithdrawRequest = users[position]

        holder.binding.txtAmount.text = " ₹ ${user.amount}"
        holder.binding.txtTimeAndDate.text = user.dateTime
        holder.binding.txtId.text = "Id - ${user.id}"
        //holder.binding.txtRequest.text = ""
        holder.binding.txtStatus.text = user.status


        sessionManager = SessionManager(context)

        holder.itemView.setOnClickListener {

            onItemClickListner.onWidthrawItemClicked(position)
        }
    }

    inner class UsersViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var binding: RawWalletHistoryBinding = RawWalletHistoryBinding.bind(itemView)

    }

    override fun getItemCount(): Int {
        return users.size
    }

    fun updateList(searchList : List<WithdrawRequest>) {

        this.users = searchList
        notifyDataSetChanged()

    }

    interface OnItemClickListner {
        fun onWidthrawItemClicked(postion: Int)
    }

}