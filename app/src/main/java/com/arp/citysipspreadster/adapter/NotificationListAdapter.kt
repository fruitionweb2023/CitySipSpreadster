package com.arp.citysipspreadster.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.arp.citysipspreadster.R
import com.arp.citysipspreadster.databinding.RawNewPromotionCategoryListMenuBinding
import com.arp.citysipspreadster.databinding.RawNotificationItemBinding
import com.arp.citysipspreadster.databinding.RawPromotionsCategoryListBinding
import com.arp.citysipspreadster.model.deshboard.Category
import com.arp.citysipspreadster.model.deshboard.OfferBusiness
import com.arp.citysipspreadster.model.notification.Notification
import com.arp.citysipspreadster.utils.SessionManager
import com.bumptech.glide.Glide

class NotificationListAdapter(var context: Context, private var users: List<Notification>) :
    RecyclerView.Adapter<NotificationListAdapter.UsersViewHolder?>() {
    private lateinit var sessionManager: SessionManager

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.raw_notification_item, parent, false)
        return UsersViewHolder(view)
    }

    override fun onBindViewHolder(holder: UsersViewHolder, position: Int) {
        val user: Notification = users[position]

        holder.binding.txtNotification.text = user.notification
        holder.binding.txtNotification.setTextColor(Color.parseColor(user.textColor))

        sessionManager = SessionManager(context)

        holder.itemView.setOnClickListener {

           // onItemClickListner.onPromotionListItemClicked(position)
        }
    }

    inner class UsersViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var binding: RawNotificationItemBinding = RawNotificationItemBinding.bind(itemView)

    }

    override fun getItemCount(): Int {
        return users.size
    }

    fun updateList(searchList : List<Notification>) {

        this.users = searchList
        notifyDataSetChanged()

    }

    interface OnItemClickListner {
        fun onPromotionListItemClicked(postion: Int)
    }

}