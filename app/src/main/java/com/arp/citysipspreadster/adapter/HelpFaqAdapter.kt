package com.arp.citysipspreadster.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.arp.citysipspreadster.R
import com.arp.citysipspreadster.databinding.RawSupportMenuBinding
import com.arp.citysipspreadster.model.accounts.Help
import com.arp.citysipspreadster.utils.SessionManager

class HelpFaqAdapter(var context: Context, private var users: List<Help>, private var onItemClickListner: OnItemClickListner) :
    RecyclerView.Adapter<HelpFaqAdapter.UsersViewHolder?>() {
     private lateinit var sessionManager: SessionManager
    var index = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.raw_support_menu, parent, false)
        return UsersViewHolder(view)
    }

    override fun onBindViewHolder(holder: UsersViewHolder, position: Int) {
        val user: Help = users[position]

        holder.binding.txtTitle.text = user.title


        holder.itemView.setOnClickListener {
            onItemClickListner.onItemClicked(position)
        }
    }

    inner class UsersViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var binding: RawSupportMenuBinding = RawSupportMenuBinding.bind(itemView)

    }

    override fun getItemCount(): Int {
        return users.size
    }

    fun updateList(searchList : List<Help>) {

        this.users = searchList
        notifyDataSetChanged()

    }

    interface OnItemClickListner {
        fun onItemClicked(postion: Int)
    }



}