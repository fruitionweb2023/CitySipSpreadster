package com.arp.citysipspreadster.adapter

import android.content.Context
import android.location.Location
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.arp.citysipspreadster.R
import com.arp.citysipspreadster.databinding.RawIndividualListviewBinding
import com.arp.citysipspreadster.databinding.RawNewPromotionCategoryListMenuBinding
import com.arp.citysipspreadster.databinding.RawPromotionsCategoryListBinding
import com.arp.citysipspreadster.model.deshboard.Category
import com.arp.citysipspreadster.model.deshboard.OfferBusiness
import com.arp.citysipspreadster.model.individualOnBoard.OnboardBusinessLead
import com.arp.citysipspreadster.utils.SessionManager
import com.bumptech.glide.Glide
import com.google.android.gms.location.FusedLocationProviderClient
import kotlin.math.acos
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin

class IndividualOnBoardListViewAdapter(var context: Context, private var users: List<OnboardBusinessLead>, private var onItemClickListner: OnItemClickListner) :
    RecyclerView.Adapter<IndividualOnBoardListViewAdapter.UsersViewHolder?>() {
    private lateinit var sessionManager: SessionManager

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.raw_individual_listview, parent, false)
        return UsersViewHolder(view)
    }

    override fun onBindViewHolder(holder: UsersViewHolder, position: Int) {
        val user: OnboardBusinessLead = users[position]

        sessionManager = SessionManager(context)

        val newLat = user.latitude.toDouble()
        val newLng = user.longitude.toDouble()
        Log.e("Lat", sessionManager.getLat().toString())
        Log.e("Lat New", newLat.toString())
        Log.e("Lng", sessionManager.getLng().toString())
        Log.e("Lng New", newLng.toString())

        val dis: Double = distance(sessionManager.getLat()!!.toDouble(), sessionManager.getLng()!!.toDouble(), newLat, newLng)

        val roundoff = (dis * 100.0).roundToInt() / 100.0
        Log.e("dis", "$roundoff Km")

        holder.binding.txtBusinessName.text = user.name
        holder.binding.txtOffer.text = "${roundoff} Km | ${user.address}"
        holder.binding.txtPercentage.text = "₹ "+user.amount



        holder.itemView.setOnClickListener {

            onItemClickListner.onListViewItemClicked(position)
        }
    }

    inner class UsersViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var binding: RawIndividualListviewBinding = RawIndividualListviewBinding.bind(itemView)

    }

    override fun getItemCount(): Int {
        return users.size
    }

    fun updateList(searchList : List<OnboardBusinessLead>) {

        this.users = searchList
        notifyDataSetChanged()

    }

    interface OnItemClickListner {
        fun onListViewItemClicked(postion: Int)
    }

    private fun distance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val theta = lon1 - lon2
        var dist = (sin(deg2rad(lat1))
                * sin(deg2rad(lat2))
                + (cos(deg2rad(lat1))
                * cos(deg2rad(lat2))
                * cos(deg2rad(theta))))
        dist = acos(dist)
        dist = rad2deg(dist)
        dist *= 60
        return dist
    }

    private fun deg2rad(deg: Double): Double {
        return deg * Math.PI / 180.0
    }

    private fun rad2deg(rad: Double): Double {
        return rad * 180.0 / Math.PI
    }


}