package com.arp.citysipspreadster.adapter.onboarded

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView
import com.arp.citysipspreadster.R
import com.arp.citysipspreadster.databinding.RawOnboardedBusinessesBinding
import com.arp.citysipspreadster.databinding.RawRequiredReminderOnboardedBusinessesBinding
import com.arp.citysipspreadster.model.onBoardedBusinesses.OnboardedBusiness1
import com.arp.citysipspreadster.utils.SessionManager
import kotlin.math.acos
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin

class NotRespondedOnBoardedBusinessesAdapter(var context: Context, private var users: List<OnboardedBusiness1>, private var OncheckBoxClickListner: OnCheckBoxClickListner, private var onItemClickListner: OnItemClickListner) :
    RecyclerView.Adapter<NotRespondedOnBoardedBusinessesAdapter.UsersViewHolder?>() {
    private lateinit var sessionManager: SessionManager
    var offer = ArrayList<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.raw_required_reminder_onboarded_businesses, parent, false)
        return UsersViewHolder(view)
    }

    override fun onBindViewHolder(holder: UsersViewHolder, position: Int) {
        val user: OnboardedBusiness1 = users[position]

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

        holder.binding.txtOffer.text = "${roundoff} Km | ${user.address}"

        holder.binding.cbOnBusiness.isChecked = user.isChecked

        holder.binding.txtBusinessName.text = user.name

        holder.binding.txtPercentage.text = "₹ " + user.amount

        sessionManager = SessionManager(context)

        holder.binding.cbOnBusiness.setOnCheckedChangeListener { compoundButton, b ->

            if (b) {
                Log.e("cuttent Id", user.id)
                offer.add(user.id)
                user.isChecked = true
                Log.e("Selected :", offer.toString())

            } else {
                Log.e("cuttent Id", "Not Checked")
                offer.remove(user.id)
                user.isChecked = false
                Log.e("UnSelected :", offer.toString())
            }

        }

        holder.itemView.setOnClickListener {
            onItemClickListner.onNotRespondedBoardedBusinesses(position)
        }

        holder.binding.cbOnBusiness.setOnClickListener {
            OncheckBoxClickListner.onNotRespondedCheckBoxClicked(position,holder.binding.cbOnBusiness)
        }

    }

    inner class UsersViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var binding: RawRequiredReminderOnboardedBusinessesBinding = RawRequiredReminderOnboardedBusinessesBinding.bind(itemView)

    }

    override fun getItemCount(): Int {
        return users.size
    }

    fun updateList(searchList : List<OnboardedBusiness1>) {

        this.users = searchList
        notifyDataSetChanged()

    }

    interface OnItemClickListner {
        fun onNotRespondedBoardedBusinesses(postion: Int)
    }

    interface OnCheckBoxClickListner {
        fun onNotRespondedCheckBoxClicked(postion: Int, check : CheckBox)
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