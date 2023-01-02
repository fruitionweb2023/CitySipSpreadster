package com.arp.citysipspreadster.activites

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.arp.citysipspreadster.R
import com.arp.citysipspreadster.adapter.MyPromotionOngoingAdapter
import com.arp.citysipspreadster.model.promotion.MyPromotionOngoing
import com.arp.citysipspreadster.model.promotion.ResponseMyPromotion
import com.arp.citysipspreadster.utils.Api
import com.arp.citysipspreadster.utils.RetrofitClient
import com.arp.citysipspreadster.utils.SessionManager
import com.arp.citysipspreadster.utils.WS_URL_PARAMS
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class OnGoingFragment : Fragment(), MyPromotionOngoingAdapter.OnItemClickListner {

    var promationList = ArrayList<MyPromotionOngoing>()
    private var promotionListAdapter: MyPromotionOngoingAdapter? = null
    private var sessionManager: SessionManager? = null

    @SuppressLint("LongLogTag")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        sessionManager = SessionManager(requireContext())
        Log.e("Id : ",sessionManager!!.getUserId().toString())
        Log.e("CatId : " ,sessionManager!!.getCatId().toString())
        getPromotionList(sessionManager!!.getUserId().toString(),sessionManager!!.getCatId().toString())
        return inflater.inflate(R.layout.fragment_on_going, container, false)

    }


         fun getPromotionList(sId : String, catId: String) {
            val api: Api = RetrofitClient.client!!.create(Api::class.java)
            val call: Call<ResponseMyPromotion>? = api.getMyPromotion(
                "Bearer " + WS_URL_PARAMS.createJWT(WS_URL_PARAMS.issuer, WS_URL_PARAMS.subject),
                WS_URL_PARAMS.access_key,
                sId,catId)
            call!!.enqueue(object : Callback<ResponseMyPromotion?> {
                override fun onResponse(
                    call: Call<ResponseMyPromotion?>,
                    response: Response<ResponseMyPromotion?>
                ) {
                    Log.e("responseDetailsList", Gson().toJson(response.body()))
                    if (response.body() != null && response.isSuccessful) {
                        if (response.body()!!.error) {


                            Toast.makeText(
                                requireContext(),
                                response.body()!!.message,
                                Toast.LENGTH_SHORT
                            ).show()

                           view!!.findViewById<ImageView>(R.id.imgError).visibility = View.VISIBLE
                            view!!.findViewById<RecyclerView>(R.id.rclrOngoing).visibility = View.GONE

                        } else {

                            promationList = response.body()!!.myPromotionOngoingList as ArrayList<MyPromotionOngoing>


                            view!!.findViewById<ImageView>(R.id.imgError).visibility = View.GONE
                            view!!.findViewById<RecyclerView>(R.id.rclrOngoing).visibility = View.VISIBLE


                            promotionListAdapter = MyPromotionOngoingAdapter(
                                requireContext(),
                                promationList,
                                this@OnGoingFragment
                            )



                            view!!.findViewById<RecyclerView>(R.id.rclrOngoing).adapter = promotionListAdapter

                            promotionListAdapter!!.notifyDataSetChanged()


                        }
                    } else {

                        Toast.makeText(
                            requireContext(),
                            resources.getString(R.string.error_admin),
                            Toast.LENGTH_SHORT
                        ).show()

                    }
                }

                override fun onFailure(call: Call<ResponseMyPromotion?>, t: Throwable) {
                    Log.e("error", t.message!!)
                    t.printStackTrace()
                }
            })
        }

    override fun onPromotionListItemClicked(postion: Int) {

        sessionManager!!.setPromotionId(promationList[postion].businessOfferId)
        activity?.let{
            val intent = Intent (it, NewPromotionDetailsActivity::class.java)
            intent.putExtra("businessOfferId", promationList[postion].businessOfferId)
            it.startActivity(intent)
        }

    }

}