package com.arp.citysipspreadster.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.arp.citysipspreadster.R
import com.arp.citysipspreadster.databinding.RawNotificationMenuBinding
import com.arp.citysipspreadster.model.notification.NotificationSetting
import com.arp.citysipspreadster.model.notification.ResponseManageSettings
import com.arp.citysipspreadster.utils.Api
import com.arp.citysipspreadster.utils.RetrofitClient
import com.arp.citysipspreadster.utils.SessionManager
import com.arp.citysipspreadster.utils.WS_URL_PARAMS
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class managedNotificationAdapter(var context: Context, private var users: List<NotificationSetting>) :
    RecyclerView.Adapter<managedNotificationAdapter.UsersViewHolder?>() {
     private lateinit var sessionManager: SessionManager

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.raw_notification_menu, parent, false)
        return UsersViewHolder(view)
    }

    override fun onBindViewHolder(holder: UsersViewHolder, position: Int) {
        val user: NotificationSetting = users[position]

        sessionManager = SessionManager(context)

        holder.binding.txtNotificationTitel.text = user.title

        holder.binding.scNotification.isChecked = user.status == 1

        holder.binding.scNotification.setOnCheckedChangeListener { _ , b ->

            if (b) {

                sendStatus(user.id,sessionManager.getUserId().toString(),"1")

            } else {

                sendStatus(user.id,sessionManager.getUserId().toString(),"0")
            }

        }
    }

    inner class UsersViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var binding: RawNotificationMenuBinding = RawNotificationMenuBinding.bind(itemView)

    }

    override fun getItemCount(): Int {
        return users.size
    }

    fun updateList(searchList : List<NotificationSetting>) {

        this.users = searchList
        notifyDataSetChanged()

    }

    interface OnItemClickListner {
        fun onItemClicked(postion: Int)
    }


    private fun sendStatus(settingId: String?, sId: String?, status: String?) {

        val api: Api = RetrofitClient.client!!.create(Api::class.java)
        val call: Call<ResponseManageSettings>? = api.managedSettingsNotification(
            "Bearer " + WS_URL_PARAMS.createJWT(WS_URL_PARAMS.issuer, WS_URL_PARAMS.subject),
            WS_URL_PARAMS.access_key,settingId,sId,status)
        call!!.enqueue(object : Callback<ResponseManageSettings?> {
            override fun onResponse(
                call: Call<ResponseManageSettings?>,
                response: Response<ResponseManageSettings?>
            ) {
                Log.e("responseStatus", Gson().toJson(response.body()))

                if (response.body() != null && response.isSuccessful) {
                    if (response.body()!!.error) {
                        Toast.makeText(context, response.body()!!.message, Toast.LENGTH_SHORT)
                            .show()
                    }

                } else {
                    Toast.makeText(context, response.body()!!.message, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseManageSettings?>, t: Throwable) {

                t.printStackTrace()
                Log.e("errorDelete", t.message!!)
            }
        })
    }


}