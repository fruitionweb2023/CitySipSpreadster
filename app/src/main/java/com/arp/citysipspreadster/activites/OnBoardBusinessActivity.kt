package com.arp.citysipspreadster.activites

import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.CheckBox
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.arp.citysipspreadster.R
import com.arp.citysipspreadster.adapter.onboarded.InvitedOnBoardedBusinessesAdapter
import com.arp.citysipspreadster.adapter.onboarded.NotRespondedOnBoardedBusinessesAdapter
import com.arp.citysipspreadster.adapter.onboarded.OnBoardedBusinessesAdapter
import com.arp.citysipspreadster.adapter.onboarded.ReminderRequireOnBoardedBusinessesAdapter
import com.arp.citysipspreadster.databinding.ActivityOnBoardBusinessBinding
import com.arp.citysipspreadster.model.onBoardedBusinesses.OnboardedBusiness1
import com.arp.citysipspreadster.model.onBoardedBusinesses.ResponseOnBoardedBusinesses
import com.arp.citysipspreadster.utils.Api
import com.arp.citysipspreadster.utils.RetrofitClient
import com.arp.citysipspreadster.utils.SessionManager
import com.arp.citysipspreadster.utils.WS_URL_PARAMS
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class OnBoardBusinessActivity : AppCompatActivity(), View.OnClickListener,
    ReminderRequireOnBoardedBusinessesAdapter.OnItemClickListner,
    ReminderRequireOnBoardedBusinessesAdapter.OnCheckBoxClickListner,
    NotRespondedOnBoardedBusinessesAdapter.OnItemClickListner,
    NotRespondedOnBoardedBusinessesAdapter.OnCheckBoxClickListner {

    private lateinit var binding: ActivityOnBoardBusinessBinding
    var pd: ProgressDialog? = null
    private var sessionManager: SessionManager? = null

    var businessList = ArrayList<OnboardedBusiness1>()
    var tempArraySelect = ArrayList<OnboardedBusiness1>()
    var tempArraySelectNotResponded = ArrayList<OnboardedBusiness1>()
    private var requiredReminderBbusinessAdapter: ReminderRequireOnBoardedBusinessesAdapter? = null
    private var notRespondedbusinessAdapter: NotRespondedOnBoardedBusinessesAdapter? = null
    private var invitedOnBoardedBusinessesAdapter: InvitedOnBoardedBusinessesAdapter? = null
    private var onBoardedBusinessesAdapter: OnBoardedBusinessesAdapter? = null

    var btnClicked = "0"
    var isClicked = true
    var flag = ""
    var flag_check = true
    var flag_check_2 = true


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnBoardBusinessBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sessionManager = SessionManager(this)

        onBackPressedDispatcher.addCallback(this,onBackInvokedCallBack)

        binding.toolbar.toolbarBack.setOnClickListener {

                val homeActivity = Intent(applicationContext, DeshBoardActivity::class.java)
                startActivity(homeActivity)
                finish()
        }

        binding.toolbar.imgNoti.setOnClickListener {
            val intent = Intent(this,NotificationActivty::class.java)
            startActivity(intent)
        }

        binding.llBottom.visibility = View.GONE
        binding.imgError.visibility = View.GONE

        binding.horizontalScrollBar.isVerticalScrollBarEnabled = false
        binding.horizontalScrollBar.isHorizontalScrollBarEnabled = false

        getOnBoardedBussinessesDetails(sessionManager!!.getUserId().toString(), "1")

        binding.btnReminderRequire.setOnClickListener(this)
        binding.btnNotResponded.setOnClickListener(this)
        binding.btninvited.setOnClickListener(this)
        binding.btnOnBoard.setOnClickListener(this)

        flag = intent.getStringExtra("flag").toString()

        if (flag == "2") {

            binding.btnReminderRequire.requestFocus()
            binding.btnReminderRequire.hasFocus()
            isClicked = true
            btnClicked = "1"
            binding.btnReminderRequire.background =
                resources.getDrawable(R.drawable.btn_sixtydp_corner_radius)
            binding.btnReminderRequire.setTextColor(getColor(R.color.clr_FFFFFF))

            binding.btnNotResponded.setBackgroundColor(Color.TRANSPARENT)
            binding.btnNotResponded.setTextColor(getColor(R.color.clr_EA2A31))

            binding.btninvited.setBackgroundColor(Color.TRANSPARENT)
            binding.btninvited.setTextColor(getColor(R.color.clr_EA2A31))

            binding.btnOnBoard.setBackgroundColor(Color.TRANSPARENT)
            binding.btnOnBoard.setTextColor(getColor(R.color.clr_EA2A31))
            binding.llBottom.visibility = View.VISIBLE
            binding.txtCBCount.text = "Select All (0)"


            getOnBoardedBussinessesDetails(sessionManager!!.getUserId().toString(), "1")

        } else if (flag == "3") {

            binding.btninvited.requestFocus()
            binding.btninvited.hasFocus()

            isClicked = false
            btnClicked = "3"


            binding.btninvited.background =
                resources.getDrawable(R.drawable.btn_sixtydp_corner_radius)
            binding.btninvited.setTextColor(getColor(R.color.clr_FFFFFF))

            binding.btnReminderRequire.setBackgroundColor(Color.TRANSPARENT)
            binding.btnReminderRequire.setTextColor(getColor(R.color.clr_EA2A31))

            binding.btnNotResponded.setBackgroundColor(Color.TRANSPARENT)
            binding.btnNotResponded.setTextColor(getColor(R.color.clr_EA2A31))

            binding.btnOnBoard.setBackgroundColor(Color.TRANSPARENT)
            binding.btnOnBoard.setTextColor(getColor(R.color.clr_EA2A31))

            getOnBoardedBussinessesDetails(sessionManager!!.getUserId().toString(), "3")

        } else if (flag == "4") {

            binding.btnOnBoard.requestFocus()
            binding.btnOnBoard.hasFocus()

            isClicked = false
            btnClicked = "4"

            binding.btnOnBoard.background =
                resources.getDrawable(R.drawable.btn_sixtydp_corner_radius)
            binding.btnOnBoard.setTextColor(getColor(R.color.clr_FFFFFF))

            binding.btnReminderRequire.setBackgroundColor(Color.TRANSPARENT)
            binding.btnReminderRequire.setTextColor(getColor(R.color.clr_EA2A31))

            binding.btnNotResponded.setBackgroundColor(Color.TRANSPARENT)
            binding.btnNotResponded.setTextColor(getColor(R.color.clr_EA2A31))

            binding.btninvited.setBackgroundColor(Color.TRANSPARENT)
            binding.btninvited.setTextColor(getColor(R.color.clr_EA2A31))

            getOnBoardedBussinessesDetails(sessionManager!!.getUserId().toString(), "4")

        }

        binding.edtSearchView.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {

                when (btnClicked) {
                    "1" -> {

                        filterCategory(p0.toString(), "1")

                    }
                    "2" -> {

                        filterCategory(p0.toString(), "2")
                    }
                    "3" -> {

                        filterCategory(p0.toString(), "3")
                    }
                    "4" -> {

                        filterCategory(p0.toString(), "4")
                    } else -> {
                    filterCategory(p0.toString(), "1")
                    }
                }


            }
        })

        binding.cbSelectAllOnBusiness.setOnCheckedChangeListener { _, b ->

           if (isClicked) {

               if (b) {

                   tempArraySelect.clear()
                   for (i in 0 until businessList.size) {
                       val onBoard : OnboardedBusiness1 = businessList[i]
                       onBoard.isChecked = true

                       if (onBoard.isChecked) {
                           tempArraySelect.add(onBoard)
                       }
                   }

                   binding.txtCBCount.text = "Select All (${tempArraySelect.size.toString()})"



                   requiredReminderBbusinessAdapter!!.updateList(businessList)

                   flag_check = true


               } else {

                   tempArraySelect.clear()
                   if (flag_check){
                       for (i in 0 until businessList.size) {
                           val onBoard : OnboardedBusiness1 = businessList[i]
                           onBoard.isChecked = false
                           if (onBoard.isChecked) {
                               tempArraySelect.remove(onBoard)
                           }
                       }
                       binding.txtCBCount.text = "Select All (${tempArraySelect.size.toString()})"

                       requiredReminderBbusinessAdapter!!.updateList(businessList)
                   }

               }
           } else {

               if (b) {

                   tempArraySelectNotResponded.clear()
                   for (i in 0 until businessList.size) {
                       val onBoard : OnboardedBusiness1 = businessList[i]
                       onBoard.isChecked = true
                       if (onBoard.isChecked) {
                           tempArraySelectNotResponded.add(onBoard)
                       }

                   }

                   binding.txtCBCount.text = "Select All (${tempArraySelectNotResponded.size.toString()})"
                   notRespondedbusinessAdapter!!.updateList(businessList)

                   flag_check_2 = true

               } else {
                   tempArraySelectNotResponded.clear()
                   if (flag_check_2) {
                       for (i in 0 until businessList.size) {
                           val onBoard: OnboardedBusiness1 = businessList[i]
                           onBoard.isChecked = false
                           if (onBoard.isChecked) {
                               tempArraySelectNotResponded.add(onBoard)
                           }
                       }
                       binding.txtCBCount.text = "Select All (${tempArraySelectNotResponded.size.toString()})"
                       notRespondedbusinessAdapter!!.updateList(businessList)
                   }
               }

           }

        }

        binding.btnDone.setOnClickListener {

            if (isClicked) {

                var checkBoxList = ""

                for (i in 0 until businessList.size) {
                    val onBoard : OnboardedBusiness1 = businessList[i]
                    if (onBoard.isChecked) {
                        checkBoxList = checkBoxList+onBoard.id+"~~"
                    }

                }
                checkBoxList = checkBoxList.substring(0, checkBoxList.length - 2)
                Log.e("CheckBox Id",checkBoxList )

                val intent = Intent(this,RemindBusinessActivity::class.java)
                intent.putExtra("businessId",checkBoxList)
                startActivity(intent)

            } else {

                var checkBoxList = ""

                for (i in 0 until businessList.size) {
                    val onBoard : OnboardedBusiness1 = businessList[i]
                    if (onBoard.isChecked) {
                        checkBoxList = checkBoxList+onBoard.id+"~~"
                    }

                }
                checkBoxList = checkBoxList.substring(0, checkBoxList.length - 2)
                Log.e("CheckBox Id SecondTab",checkBoxList )

                val intent = Intent(this,InviteBusinessActivity::class.java)
                intent.putExtra("flag","1")
                intent.putExtra("businessId",checkBoxList)
                startActivity(intent)
            }
        }

    }

    private fun getOnBoardedBussinessesDetails(sId: String, bool: String) {

        val api: Api = RetrofitClient.client!!.create(Api::class.java)
        val call: Call<ResponseOnBoardedBusinesses>? = api.getOnBoardedBusinessesDetails(
            "Bearer " + WS_URL_PARAMS.createJWT(WS_URL_PARAMS.issuer, WS_URL_PARAMS.subject),
            WS_URL_PARAMS.access_key, sId
        )
        call!!.enqueue(object : Callback<ResponseOnBoardedBusinesses?> {
            override fun onResponse(
                call: Call<ResponseOnBoardedBusinesses?>,
                response: Response<ResponseOnBoardedBusinesses?>
            ) {
                Log.e("responseDetailsList", Gson().toJson(response.body()))
                if (response.body() != null && response.isSuccessful) {
                    if (response.body()!!.error) {


                        Toast.makeText(
                            this@OnBoardBusinessActivity,
                            response.body()!!.message,
                            Toast.LENGTH_SHORT
                        ).show()


                        binding.imgError.visibility = View.VISIBLE
                        binding.rclrOnBoardedBusiness.visibility = View.GONE

                    } else {


                        binding.imgError.visibility = View.GONE
                        binding.rclrOnBoardedBusiness.visibility = View.VISIBLE


                        if (bool == "1") {

                            businessList =
                                response.body()!!.onboardedBusiness1 as ArrayList<OnboardedBusiness1>
                            requiredReminderBbusinessAdapter =
                                ReminderRequireOnBoardedBusinessesAdapter(
                                    this@OnBoardBusinessActivity,
                                    businessList, this@OnBoardBusinessActivity,
                                    this@OnBoardBusinessActivity
                                )
                          //  binding.txtCBCount.text = "Select All (${businessList.size})"
                            binding.rclrOnBoardedBusiness.adapter = requiredReminderBbusinessAdapter

                            Log.e("responseclick", "true")

                        } else if (bool == "2") {

                            businessList =
                                response.body()!!.onboardedBusiness2 as ArrayList<OnboardedBusiness1>
                            notRespondedbusinessAdapter = NotRespondedOnBoardedBusinessesAdapter(
                                this@OnBoardBusinessActivity,
                                businessList,
                                this@OnBoardBusinessActivity,this@OnBoardBusinessActivity)
                            //binding.txtCBCount.text = "Select All (${businessList.size})"
                            binding.rclrOnBoardedBusiness.adapter = notRespondedbusinessAdapter
                            Log.e("responseclick", "false")

                        } else if (bool == "3") {

                            businessList =
                                response.body()!!.onboardedBusiness3 as ArrayList<OnboardedBusiness1>
                            invitedOnBoardedBusinessesAdapter = InvitedOnBoardedBusinessesAdapter(
                                this@OnBoardBusinessActivity,
                                businessList
                            )

                            binding.rclrOnBoardedBusiness.adapter =
                                invitedOnBoardedBusinessesAdapter

                        } else {

                            businessList =
                                response.body()!!.onboardedBusiness4 as ArrayList<OnboardedBusiness1>
                            onBoardedBusinessesAdapter = OnBoardedBusinessesAdapter(
                                this@OnBoardBusinessActivity,
                                businessList
                            )



                            binding.rclrOnBoardedBusiness.adapter = onBoardedBusinessesAdapter

                        }


                    }
                } else {

                    Toast.makeText(
                        this@OnBoardBusinessActivity,
                        resources.getString(R.string.error_admin),
                        Toast.LENGTH_SHORT
                    ).show()

                }
            }

            override fun onFailure(call: Call<ResponseOnBoardedBusinesses?>, t: Throwable) {
                Log.e("error", t.message!!)
                t.printStackTrace()
            }
        })
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onClick(p0: View?) {


        when (p0!!.id) {

            R.id.btnReminderRequire -> {


                isClicked = true
                btnClicked = "1"
                binding.btnReminderRequire.background =
                    resources.getDrawable(R.drawable.btn_sixtydp_corner_radius)
                binding.btnReminderRequire.setTextColor(getColor(R.color.clr_FFFFFF))

                binding.btnNotResponded.setBackgroundColor(Color.TRANSPARENT)
                binding.btnNotResponded.setTextColor(getColor(R.color.clr_EA2A31))

                binding.btninvited.setBackgroundColor(Color.TRANSPARENT)
                binding.btninvited.setTextColor(getColor(R.color.clr_EA2A31))

                binding.btnOnBoard.setBackgroundColor(Color.TRANSPARENT)
                binding.btnOnBoard.setTextColor(getColor(R.color.clr_EA2A31))
                binding.llBottom.visibility = View.VISIBLE
                binding.txtCBCount.text = "Select All (0)"

                binding.btnDone.text = "Send Reminder"

                getOnBoardedBussinessesDetails(sessionManager!!.getUserId().toString(), "1")


            }
            R.id.btnNotResponded -> {


                isClicked = false
                btnClicked = "2"

                binding.btnNotResponded.background =
                    resources.getDrawable(R.drawable.btn_sixtydp_corner_radius)
                binding.btnNotResponded.setTextColor(getColor(R.color.clr_FFFFFF))

                binding.btnReminderRequire.setBackgroundColor(Color.TRANSPARENT)
                binding.btnReminderRequire.setTextColor(getColor(R.color.clr_EA2A31))

                binding.btninvited.setBackgroundColor(Color.TRANSPARENT)
                binding.btninvited.setTextColor(getColor(R.color.clr_EA2A31))

                binding.btnOnBoard.setBackgroundColor(Color.TRANSPARENT)
                binding.btnOnBoard.setTextColor(getColor(R.color.clr_EA2A31))
                binding.llBottom.visibility = View.VISIBLE
                binding.txtCBCount.text = "Select All (0)"

                binding.btnDone.text = "Send Invite"

                getOnBoardedBussinessesDetails(sessionManager!!.getUserId().toString(), "2")

            }

            R.id.btninvited -> {


                isClicked = false
                btnClicked = "3"


                binding.btninvited.background =
                    resources.getDrawable(R.drawable.btn_sixtydp_corner_radius)
                binding.btninvited.setTextColor(getColor(R.color.clr_FFFFFF))

                binding.btnReminderRequire.setBackgroundColor(Color.TRANSPARENT)
                binding.btnReminderRequire.setTextColor(getColor(R.color.clr_EA2A31))

                binding.btnNotResponded.setBackgroundColor(Color.TRANSPARENT)
                binding.btnNotResponded.setTextColor(getColor(R.color.clr_EA2A31))

                binding.btnOnBoard.setBackgroundColor(Color.TRANSPARENT)
                binding.btnOnBoard.setTextColor(getColor(R.color.clr_EA2A31))
                binding.llBottom.visibility = View.GONE

                getOnBoardedBussinessesDetails(sessionManager!!.getUserId().toString(), "3")

            }

            R.id.btnOnBoard -> {


                isClicked = false
                btnClicked = "4"

                binding.btnOnBoard.background =
                    resources.getDrawable(R.drawable.btn_sixtydp_corner_radius)
                binding.btnOnBoard.setTextColor(getColor(R.color.clr_FFFFFF))

                binding.btnReminderRequire.setBackgroundColor(Color.TRANSPARENT)
                binding.btnReminderRequire.setTextColor(getColor(R.color.clr_EA2A31))

                binding.btnNotResponded.setBackgroundColor(Color.TRANSPARENT)
                binding.btnNotResponded.setTextColor(getColor(R.color.clr_EA2A31))

                binding.btninvited.setBackgroundColor(Color.TRANSPARENT)
                binding.btninvited.setTextColor(getColor(R.color.clr_EA2A31))
                binding.llBottom.visibility = View.GONE

                getOnBoardedBussinessesDetails(sessionManager!!.getUserId().toString(), "4")

            }
        }
    }




    fun filterCategory(text: String, btnClicked: String) {
        val tempReminderRequire: ArrayList<OnboardedBusiness1> = ArrayList<OnboardedBusiness1>()
        val tempNotResponded: ArrayList<OnboardedBusiness1> = ArrayList<OnboardedBusiness1>()
        val tempInvited: ArrayList<OnboardedBusiness1> = ArrayList<OnboardedBusiness1>()
        val tempOnboarded: ArrayList<OnboardedBusiness1> = ArrayList<OnboardedBusiness1>()

        if (btnClicked == "1") {
            if (businessList != null) {
                for (d in businessList) {
                    if (d.name.toUpperCase().contains(text.uppercase(Locale.getDefault()))) {
                        tempReminderRequire.add(d)

                    } else {
                        binding.imgError.visibility = View.VISIBLE
                    }
                }
            }

            if (tempReminderRequire != null){

                binding.imgError.visibility = View.GONE
                requiredReminderBbusinessAdapter!!.updateList(tempReminderRequire)
            }else{
                binding.imgError.visibility = View.VISIBLE
            }

            //update recyclerview

        } else if (btnClicked == "2") {

            if (businessList != null) {
                for (d in businessList) {
                    if (d.name.toUpperCase().contains(text.uppercase(Locale.getDefault()))) {
                        tempNotResponded.add(d)
                    } else {
                        binding.imgError.visibility = View.VISIBLE
                    }
                }
            }
            if (tempNotResponded != null){

                binding.imgError.visibility = View.GONE
                notRespondedbusinessAdapter!!.updateList(tempNotResponded)
            }else{
                binding.imgError.visibility = View.VISIBLE
            }
            //update recyclerview

        } else if (btnClicked == "3") {

            if (businessList != null) {
                for (d in businessList) {
                    if (d.name.toUpperCase().contains(text.uppercase(Locale.getDefault()))) {
                        tempInvited.add(d)
                    } else {
                        binding.imgError.visibility = View.VISIBLE
                    }
                }
            }

            if (tempInvited != null){

                binding.imgError.visibility = View.GONE
                invitedOnBoardedBusinessesAdapter!!.updateList(tempInvited)
            }else{
                binding.imgError.visibility = View.VISIBLE
            }

            //update recyclerview

        } else {

            if (businessList != null) {
                for (d in businessList) {
                    if (d.name.toUpperCase().contains(text.uppercase(Locale.getDefault()))) {
                        tempOnboarded.add(d)
                    } else {
                        binding.imgError.visibility = View.VISIBLE
                    }
                }
            }

            if (tempOnboarded != null){

                binding.imgError.visibility = View.GONE
                onBoardedBusinessesAdapter!!.updateList(tempOnboarded)
            }else{
                binding.imgError.visibility = View.VISIBLE
            }

            //update recyclerview

        }
    }

    override fun onCheckBoxClicked(postion: Int, check : CheckBox,count : Int) {

       // binding.txtCBCount.text = "Select All ($count)"
        var check_true = false

        //for (i in 0 until businessList.size) {
            val onBoard : OnboardedBusiness1 = businessList[postion]
               // onBoard.isChecked = !onBoard.isChecked
        if (!onBoard.isChecked){
            flag_check = false
            binding.cbSelectAllOnBusiness.isChecked = false
        }

        for(i in 0 until businessList.size){
            val onBoard : OnboardedBusiness1 = businessList[i]
            if (onBoard.isChecked){
                check_true = true
            }else{
                 check_true = false
                break
            }
        }
        tempArraySelect.clear()
        for (i in 0 until businessList.size) {
            val onBoard : OnboardedBusiness1 = businessList[i]

            if (onBoard.isChecked) {
                tempArraySelect.add(onBoard)
            }


        }

        binding.txtCBCount.text = "Select All (${tempArraySelect.size.toString()})"

        if (check_true){
            flag_check = true
            binding.cbSelectAllOnBusiness.isChecked = true
        }

        //}
        //requiredReminderBbusinessAdapter!!.updateList(businessList)



    }

    override fun onRequiredReminderBoardedBusinesses(postion: Int) {



    }

    override fun onNotRespondedBoardedBusinesses(postion: Int) {


    }

    private  val onBackInvokedCallBack = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            val intent = Intent(this@OnBoardBusinessActivity,DeshBoardActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onNotRespondedCheckBoxClicked(postion: Int, check: CheckBox) {

        var check_true = false

        //for (i in 0 until businessList.size) {
        val onBoard : OnboardedBusiness1 = businessList[postion]
        // onBoard.isChecked = !onBoard.isChecked
        if (!onBoard.isChecked){
            flag_check_2 = false
            binding.cbSelectAllOnBusiness.isChecked = false
        }

        for(i in 0 until businessList.size){
            val onBoard : OnboardedBusiness1 = businessList[i]
            if (onBoard.isChecked){
                check_true = true
            }else{
                check_true = false
                break
            }
        }

        tempArraySelectNotResponded.clear()
        for (i in 0 until businessList.size) {
            val onBoard : OnboardedBusiness1 = businessList[i]

            if (onBoard.isChecked) {
                tempArraySelectNotResponded.add(onBoard)
            }


        }

        binding.txtCBCount.text = "Select All (${tempArraySelectNotResponded.size.toString()})"

        if (check_true){
            flag_check_2 = true
            binding.cbSelectAllOnBusiness.isChecked = true
        }
    }

}