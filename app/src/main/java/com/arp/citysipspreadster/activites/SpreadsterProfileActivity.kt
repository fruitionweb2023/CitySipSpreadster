package com.arp.citysipspreadster.activites

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.arp.citysipspreadster.R
import com.arp.citysipspreadster.adapter.SpreadsterOfferAllDetailsAdapter
import com.arp.citysipspreadster.adapter.SpreadsterProfileCategoryAdapter
import com.arp.citysipspreadster.adapter.spreadsterProfileDetailsAdapter
import com.arp.citysipspreadster.databinding.ActivitySpreadsterzprofileBinding
import com.arp.citysipspreadster.model.accounts.*
import com.arp.citysipspreadster.utils.*
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SpreadsterProfileActivity : AppCompatActivity(),
    SpreadsterProfileCategoryAdapter.OnItemClickListner,
    SpreadsterOfferAllDetailsAdapter.OnItemClickListner,
    spreadsterProfileDetailsAdapter.OnItemClickListner {

    private lateinit var binding: ActivitySpreadsterzprofileBinding
    private var bottomButtonClickListner: BottomButtonClickListner? = null
    var pd: ProgressDialog? = null
    private var sessionManager: SessionManager? = null

    var profileCategory = ArrayList<ProfileCategory>()
    private var profileSpreadsterAdapter: SpreadsterProfileCategoryAdapter? = null

    var offerList = ArrayList<ProfileTrendingOffer>()
    var allOfferList = ArrayList<ProfileOffer>()
    private var allOfferListAdapter: SpreadsterOfferAllDetailsAdapter? = null
    private var offerListAdapter: spreadsterProfileDetailsAdapter? = null

    companion object {
        var catposNew = "0"
    }

    var catId = ""
    var twitterLink = ""
    var youtubeLink = ""
    var instaLink = ""
    var facebookLink = ""
    var businessName = ""
    var imageUrl = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySpreadsterzprofileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sessionManager = SessionManager(this)

        getPromotionCategory(sessionManager!!.getUserId().toString())

        binding.toolbar.toolbarBack.setOnClickListener {
            val intent = Intent(this,MyAccountActivity::class.java)
            startActivity(intent)
        }

        onBackPressedDispatcher.addCallback(this,onBackInvokedCallBack)

        binding.toolbar.toolbarBack.setOnClickListener {
            val intent = Intent(this@SpreadsterProfileActivity,MyAccountActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.toolbar.imgNoti.setOnClickListener {
            val intent = Intent(this,NotificationActivty::class.java)
            startActivity(intent)
        }

        binding.btnFacebook.setOnClickListener {

           if (facebookLink != null) {
               val intent = Intent(Intent.ACTION_VIEW, Uri.parse(facebookLink))
               startActivity(intent)
           }
        }

        binding.btnTwitter.setOnClickListener {

            if (twitterLink != null) {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(twitterLink))
                startActivity(intent)
            }

        }

        binding.btnInsta.setOnClickListener {

            if (instaLink != null) {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(instaLink))
                startActivity(intent)
            }
        }

        binding.btnYoutube.setOnClickListener {

            if (youtubeLink != null) {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(youtubeLink))
                startActivity(intent)
            }

        }

        imageUrl = intent.getStringExtra("image").toString()
        Log.e("SpreadsterProfileImage",imageUrl)

        if (imageUrl == "") {

            val options: RequestOptions = RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.profile_img)
                .error(R.drawable.profile_img)

            Glide.with(this@SpreadsterProfileActivity).load(imageUrl).apply(options).into(binding.profileImage)

        } else {

            val options: RequestOptions = RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.profile_img)
                .error(R.drawable.profile_img)

            Glide.with(this@SpreadsterProfileActivity).load(imageUrl).apply(options).into(binding.profileImage)
        }

    }

    private fun getPromotionCategory(sId:String) {
        val api: Api = RetrofitClient.client!!.create(Api::class.java)
        val call: Call<ResponseSpreadsterCategory>? = api.getSpreadsterCategory(
            "Bearer " + WS_URL_PARAMS.createJWT(WS_URL_PARAMS.issuer, WS_URL_PARAMS.subject),
            WS_URL_PARAMS.access_key,sId)
        call!!.enqueue(object : Callback<ResponseSpreadsterCategory?> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(
                call: Call<ResponseSpreadsterCategory?>,
                response: Response<ResponseSpreadsterCategory?>
            ) {
                Log.e("responseCategory", Gson().toJson(response.body()))
                if (response.body() != null && response.isSuccessful) {
                    if (response.body()!!.error) {

                        Toast.makeText(
                            this@SpreadsterProfileActivity,
                            response.body()!!.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {

                        profileCategory =
                            response.body()!!.profileCategoryList as ArrayList<ProfileCategory>

                        profileSpreadsterAdapter = SpreadsterProfileCategoryAdapter(
                            this@SpreadsterProfileActivity,
                            profileCategory,
                            this@SpreadsterProfileActivity
                        )

                       /* if (response.body()!!.imageVideo == "") {

                            val options: RequestOptions = RequestOptions()
                                .centerCrop()
                                .placeholder(R.drawable.bg_image)
                                .error(R.drawable.bg_image)

                            Glide.with(this@SpreadsterProfileActivity).load(Api.imageUrl+response.body()!!.imageVideo).apply(options).into(binding.profileImage)


                        } else {

                            val options: RequestOptions = RequestOptions()
                                .centerCrop()
                                .placeholder(R.drawable.bg_image)
                                .error(R.drawable.bg_image)

                            Glide.with(this@SpreadsterProfileActivity).load(Api.imageUrl+response.body()!!.imageVideo).apply(options).into(binding.profileImage)


                        }*/
                        binding.txtUserName.text =  response.body()?.name
                        binding.txtSid.text =  "SID - " + response.body()?.id
                        binding.rclrCategoryList.adapter = profileSpreadsterAdapter
                        
                        if(response.body()!!.twitter != null) {
                            twitterLink = response.body()!!.twitter
                        }
                        if(response.body()!!.youtube != null) {
                            youtubeLink = response.body()!!.youtube
                        }
                        if(response.body()!!.instagram != null) {
                            instaLink = response.body()!!.instagram
                        }
                        if(response.body()!!.facebook != null) {
                        facebookLink = response.body()!!.facebook
                        }
                        catposNew = "0"
                        val id = profileCategory[catposNew.toInt()].id
                        profileSpreadsterAdapter!!.notifyDataSetChanged()
                        catId = id
                        businessName = profileCategory[catposNew.toInt()].name
                        getOfferList(sessionManager!!.getUserId().toString(),id)

                    }
                } else {

                    Toast.makeText(
                        this@SpreadsterProfileActivity,
                        resources.getString(R.string.error_admin),
                        Toast.LENGTH_SHORT
                    ).show()

                }
            }

            override fun onFailure(call: Call<ResponseSpreadsterCategory?>, t: Throwable) {
                Log.e("error", t.message!!)
                t.printStackTrace()
            }
        })
    }


    private fun getOfferList(sId: String,catId:String) {
        val api: Api = RetrofitClient.client!!.create(Api::class.java)
        val call: Call<ResponseSpreadsterProfile>? = api.getSpreadsterProfile(
            "Bearer " + WS_URL_PARAMS.createJWT(WS_URL_PARAMS.issuer, WS_URL_PARAMS.subject),
            WS_URL_PARAMS.access_key,
            sId,catId)
        call!!.enqueue(object : Callback<ResponseSpreadsterProfile?> {
            override fun onResponse(
                call: Call<ResponseSpreadsterProfile?>,
                response: Response<ResponseSpreadsterProfile?>
            ) {
                Log.e("responseOfferList", Gson().toJson(response.body()))
                if (response.body() != null && response.isSuccessful) {
                    if (response.body()!!.error) {

                       // binding.imgError.visibility = View.VISIBLE
                        binding.rclrTrandingOffer.visibility = View.GONE
                        binding.rclrAllOffer.visibility = View.GONE
                        binding.imgErrorAllOffer.visibility = View.VISIBLE
                        binding.imgErrorTrandingOffer.visibility = View.VISIBLE

                        Toast.makeText(
                            this@SpreadsterProfileActivity,
                            response.body()!!.message,
                            Toast.LENGTH_SHORT
                        ).show()

                    } else {

                        offerList = response.body()!!.profileTrendingOfferList as ArrayList<ProfileTrendingOffer>
                        allOfferList = response.body()!!.profileOfferList as ArrayList<ProfileOffer>

                        if (offerList.isEmpty()) {

                            binding.imgErrorTrandingOffer.visibility = View.VISIBLE
                        } else {
                            binding.imgErrorTrandingOffer.visibility = View.GONE
                        }

                        if (allOfferList.isEmpty()) {

                            binding.imgErrorAllOffer.visibility = View.VISIBLE
                        } else {
                            binding.imgErrorAllOffer.visibility = View.GONE
                        }


                        val layoutManager = GridLayoutManager(this@SpreadsterProfileActivity, 3)
                        val layoutManager2 = GridLayoutManager(this@SpreadsterProfileActivity, 3)
                      //  binding.imgError.visibility = View.GONE
                        binding.rclrTrandingOffer.visibility = View.VISIBLE
                        binding.rclrAllOffer.visibility = View.VISIBLE

                        allOfferListAdapter = SpreadsterOfferAllDetailsAdapter(
                            this@SpreadsterProfileActivity,
                            allOfferList,
                            this@SpreadsterProfileActivity
                        )

                        offerListAdapter = spreadsterProfileDetailsAdapter(
                            this@SpreadsterProfileActivity,
                            offerList,
                            this@SpreadsterProfileActivity
                        )

                        binding.rclrTrandingOffer.layoutManager = layoutManager
                        binding.rclrTrandingOffer.adapter = offerListAdapter
                        binding.rclrAllOffer.layoutManager = layoutManager2
                        binding.rclrAllOffer.adapter = allOfferListAdapter


                    }
                } else {

                    Toast.makeText(
                        this@SpreadsterProfileActivity,
                        resources.getString(R.string.error_admin),
                        Toast.LENGTH_SHORT
                    ).show()

                }
            }

            override fun onFailure(call: Call<ResponseSpreadsterProfile?>, t: Throwable) {
                Log.e("error", t.message!!)
                t.printStackTrace()
            }
        })
    }


    private  val onBackInvokedCallBack = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            val intent = Intent(this@SpreadsterProfileActivity,MyAccountActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onCategoryItemClicked(postion: Int) {
        catposNew = postion.toString()
        catId = profileCategory[postion].id
        businessName = profileCategory[postion].name
        profileSpreadsterAdapter!!.updateList(profileCategory)
        getOfferList(sessionManager!!.getUserId().toString(),profileCategory[postion].id)
    }

    override fun onItemClicked(postion: Int) {

        val intent = Intent(this,OrderNowActivity::class.java)
        intent.putExtra("name",binding.txtUserName.text.toString())
        intent.putExtra("type",offerList[postion].type)
        intent.putExtra("title",offerList[postion].description)
        intent.putExtra("promocode",offerList[postion].promoCode)
        intent.putExtra("link",offerList[postion].imageVideo)
        intent.putExtra("sId",binding.txtSid.text.toString())
        intent.putExtra("businessName",offerList[postion].title)
        startActivity(intent)

    }

    override fun onTrandingItemClicked(postion: Int) {
        val intent = Intent(this,OrderNowActivity::class.java)
        intent.putExtra("name",binding.txtUserName.text.toString())
        intent.putExtra("type",allOfferList[postion].type)
        intent.putExtra("title",offerList[postion].description)
        intent.putExtra("promocode",offerList[postion].promoCode)
        intent.putExtra("link",allOfferList[postion].imageVideo)
        intent.putExtra("sId",binding.txtSid.text.toString())
        intent.putExtra("businessName",allOfferList[postion].title)

        startActivity(intent)
    }
}