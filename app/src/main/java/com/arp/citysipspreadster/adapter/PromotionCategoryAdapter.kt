package com.arp.citysipspreadster.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.arp.citysipspreadster.R
import com.arp.citysipspreadster.activites.NewPromotionActivity
import com.arp.citysipspreadster.databinding.RawPromotionsCategoryListBinding
import com.arp.citysipspreadster.model.deshboard.Category
import com.arp.citysipspreadster.utils.SessionManager
import com.bumptech.glide.Glide

class PromotionCategoryAdapter(var context: Context, private var users: List<Category>, private var onItemClickListner: OnItemClickListner) :
    RecyclerView.Adapter<PromotionCategoryAdapter.UsersViewHolder?>() {
     private lateinit var sessionManager: SessionManager
    var index = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.raw_promotions_category_list, parent, false)
        return UsersViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: UsersViewHolder, position: Int) {
        val user: Category = users[position]

        holder.binding.txtCategoryName.text = user.name
        holder.binding.txtCategoryCount.text = "(" +user.count.toString()+ ")"
        Glide.with(context).load(user.image)
            .error(R.drawable.ic_baseline_person)
            .placeholder(R.drawable.ic_baseline_person)
            .into(holder.binding.imgCategory)
        sessionManager = SessionManager(context)

        index = position


        if (index == NewPromotionActivity.catposNew.toInt()) {
            if (user.bgColor != ""){

                holder.binding.categoryViewLine.setBackgroundColor(Color.parseColor(user.bgColor))
            }else{
                holder.binding.categoryViewLine.setBackgroundColor(Color.parseColor("#804689EE"))

            }
        } else {
            holder.binding.categoryViewLine.setBackgroundColor(Color.TRANSPARENT)
        }

        holder.itemView.setOnClickListener {
            onItemClickListner.onItemClicked(position)
        }
    }

    inner class UsersViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var binding: RawPromotionsCategoryListBinding = RawPromotionsCategoryListBinding.bind(itemView)

    }

    override fun getItemCount(): Int {
        return users.size
    }

    fun updateList(searchList : List<Category>) {

        this.users = searchList
        notifyDataSetChanged()

    }

    interface OnItemClickListner {
        fun onItemClicked(postion: Int)
    }



}