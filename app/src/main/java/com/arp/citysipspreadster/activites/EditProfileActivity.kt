package com.arp.citysipspreadster.activites

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.arp.citysipspreadster.R
import com.arp.citysipspreadster.adapter.*
import com.arp.citysipspreadster.adapter.editProfile.CityListSearchAdapter
import com.arp.citysipspreadster.adapter.editProfile.CountryListSearchAdapter
import com.arp.citysipspreadster.adapter.editProfile.StateListSearchAdapter
import com.arp.citysipspreadster.databinding.ActivityEditProfileBinding
import com.arp.citysipspreadster.model.ResponseGetProfile
import com.arp.citysipspreadster.model.ResponseUpdateProfile
import com.arp.citysipspreadster.model.cityList.ResponseCityList
import com.arp.citysipspreadster.model.countryList.Country
import com.arp.citysipspreadster.model.countryList.ResponseGetCountry
import com.arp.citysipspreadster.model.stateList.Category
import com.arp.citysipspreadster.model.stateList.ResponseGetStateList
import com.arp.citysipspreadster.utils.*
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class EditProfileActivity : AppCompatActivity(),
    CountryListSearchAdapter.RecyclerViewItemClickListener,
    StateListSearchAdapter.RecyclerViewItemClickListener,
    CityListSearchAdapter.RecyclerViewItemClickListener {

    private lateinit var binding: ActivityEditProfileBinding
    var pd: ProgressDialog? = null
    private var sessionManager: SessionManager? = null
    var strCityName: String = ""
    var strCityId: String = ""
    var strStateName: String = ""
    var strStateId: String = ""
    var strCountryName: String = ""
    var strCountryId: String = ""
    var cityId: String = ""
    var countryId: String = ""
    var stateId: String = ""

    var countryList = ArrayList<Country>()
    private var countryListAdapter: CountryAdapter? = null

    var stateList = ArrayList<Category>()
    private var stateListAdapter: StateListAdapter? = null

    var cityList = ArrayList<com.arp.citysipspreadster.model.cityList.Category>()
    private var cityListAdapter: CityListAdapter? = null

    var countryListSearchAdapter: CountryListSearchAdapter? = null
    var customDialog: CustomListViewDialogWithSearchCountry? = null
    var country = ""

    var stateListSearchAdapter: StateListSearchAdapter? = null
    var customDialogState: CustomListViewDialogWithSearchState? = null
    var state = ""

    var cityListSearchAdapter: CityListSearchAdapter? = null
    var customDialogCity: CustomListViewDialogWithSearchCity? = null
    var city = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sessionManager = SessionManager(this)

        binding.edtPhoneNumber.isEnabled = false
        binding.edtPhoneNumber.isClickable = false

        binding.toolbar.toolbarBack.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
        }

        binding.toolbar.imgNoti.setOnClickListener {
            val intent = Intent(this, NotificationActivty::class.java)
            startActivity(intent)
        }


        binding.txtCountry.setOnClickListener {

            countryListSearchAdapter =
                CountryListSearchAdapter(
                    countryList,
                    this@EditProfileActivity
                )
            customDialog = CustomListViewDialogWithSearchCountry(
                this@EditProfileActivity,
                countryListSearchAdapter,
                countryList
            )
            // customDialog = CustomListViewDialogWithSearch(this@NewBusinessActivity, R.style.DialogStyle)
            customDialog!!.window!!.setBackgroundDrawableResource(android.R.color.transparent)

            customDialog!!.show()

        }

        binding.txtState.setOnClickListener {
            stateListSearchAdapter =
                StateListSearchAdapter(
                    stateList,
                    this@EditProfileActivity
                )
            customDialogState = CustomListViewDialogWithSearchState(
                this@EditProfileActivity,
                stateListSearchAdapter,
                stateList
            )
            // customDialog = CustomListViewDialogWithSearch(this@NewBusinessActivity, R.style.DialogStyle)
            customDialogState!!.window!!.setBackgroundDrawableResource(android.R.color.transparent)

            customDialogState!!.show()

        }
        binding.txtCity.setOnClickListener {
            cityListSearchAdapter =
                CityListSearchAdapter(
                    cityList,
                    this@EditProfileActivity
                )
            customDialogCity = CustomListViewDialogWithSearchCity(
                this@EditProfileActivity,
                cityListSearchAdapter,
                cityList
            )
            // customDialog = CustomListViewDialogWithSearch(this@NewBusinessActivity, R.style.DialogStyle)
            customDialogCity!!.window!!.setBackgroundDrawableResource(android.R.color.transparent)

            customDialogCity!!.show()
        }

        /*countryListAdapter =
            CountryAdapter(
                this@EditProfileActivity,
                R.layout.raw_recyclear_view_drop_down,
                R.id.tvName,
                countryList
            )
        binding.spCountry.adapter = countryListAdapter*/
       /* binding.spCountry.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>, view: View, i: Int, l: Long) {
                val strCuisine = adapterView.getItemAtPosition(i).toString()
                strCountryId = countryList[i].id
                strCountryName = countryList[i].name

                Log.e("selected Country :", strCountryName)
                Log.e("selected CountryId :", strCountryId)
                if (strCountryId == countryId) {
                    Log.e("if equal", "Equal Country Id", )
                } else if (strCountryId != countryId) {
                    Log.e("else if Not Equal", "Not Equal Country Id $countryId and strCountryId is $strCountryId", )
                } else {
                    Log.e("Wrong", "Else Wrong", )

                }

                if (strCountryName == "Select Country") {
                    Toast.makeText(
                        this@EditProfileActivity,
                        "Please select country first",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    getState(strCountryId)
                }
            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {}
        }
       // binding.spCountry.setSelection(countryListAdapter!!.getItemIndexById(countryId))



        //  getCountry()

        stateListAdapter = StateListAdapter(
            this@EditProfileActivity, R.layout.raw_recyclear_view_drop_down, R.id.tvName, stateList
        )
        binding.spState.adapter = stateListAdapter
        binding.spState.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>, view: View, i: Int, l: Long) {
                val strCuisine = adapterView.getItemAtPosition(i).toString()
                strStateId = stateList[i].id
                strStateName = stateList[i].name

                Log.e("selected state :", strStateName)
                Log.e("selected stateId :", strStateId)

                if (strStateId == stateId) {
                    Log.e("if equal", "Equal State Id $stateId and strState is $strStateId", )
                } else if (strStateId != stateId) {
                    Log.e("else if Not Equal", "Not Equal State Id $stateId and strState is $strStateId", )
                } else {
                    Log.e("Wrong", "Else Wrong", )

                }

                if (strStateName == "Select State") {
                    Toast.makeText(
                        this@EditProfileActivity,
                        "Please select State first",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    getCity(strStateId)
                }
            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {

            }
        }


        cityListAdapter = CityListAdapter(
            this@EditProfileActivity, R.layout.raw_recyclear_view_drop_down, R.id.tvName, cityList
        )
        binding.spCity.adapter = cityListAdapter
        binding.spCity.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>, view: View, i: Int, l: Long) {
                val strCuisine = adapterView.getItemAtPosition(i).toString()
                strCityId = cityList[i].id
                strCityName = cityList[i].name

                Log.e("selected City :", strCityName)
                Log.e("selected CityId :", strCityId)

            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {}
        }

*/
        binding.btnSave.setOnClickListener {
            if (binding.edtName.text.toString().trim() == "") {

                binding.edtName.error = "Enter your name"

            } else if (binding.edtPhoneNumber.text.toString().trim() == "") {

                binding.edtPhoneNumber.error = "Enter your phonenumber"

            } else if (binding.edtEmail.text.toString().trim() == "") {

                binding.edtEmail.error = "Enter your email"

            } else if (country == "") {

                Toast.makeText(this, "Please select your country", Toast.LENGTH_SHORT).show()

            } else if (state == "") {

                Toast.makeText(this, "Please select your state", Toast.LENGTH_SHORT).show()

            } else if (city == "") {

                Toast.makeText(this, "Please select your city", Toast.LENGTH_SHORT).show()

            } else if (binding.edtAboutMe.text.toString().trim() == "") {

                binding.edtAboutMe.error = "Enter about you"

            } else {
                updateProfile(
                    sessionManager!!.getUserId()!!,
                    binding.edtName.text.toString(),
                    binding.edtPhoneNumber.text.toString(),
                    binding.edtEmail.text.toString(),
                    binding.edtAboutMe.text.toString(),
                    country,
                    state,
                    city,
                    ""
                )
            }
        }

        sessionManager!!.getUserId()?.let { getProfile(it) }

    }


    private fun updateProfile(profileId: String, name: String, mobile: String, email: String, about: String, country: String, state: String, city: String, image: String) {
        pd = ProgressDialog(this)
        pd!!.setMessage("Loading...")
        pd!!.setCancelable(false)
        pd!!.show()
        val api: Api = RetrofitClient.client!!.create(Api::class.java)
        val call: Call<ResponseUpdateProfile?>? = api.updateProfile(
            "Bearer " + WS_URL_PARAMS.createJWT(WS_URL_PARAMS.issuer, WS_URL_PARAMS.subject),
            WS_URL_PARAMS.access_key,
            profileId,
            name,
            mobile,
            email,
            about,
            country,
            state,
            city,
            image
        )
        call!!.enqueue(object : Callback<ResponseUpdateProfile?> {
            override fun onResponse(
                call: Call<ResponseUpdateProfile?>,
                response: Response<ResponseUpdateProfile?>
            ) {

                Log.e("responeUpdateProfile", Gson().toJson(response.body()))

                if (pd!!.isShowing) {
                    pd!!.dismiss()
                }

                if (!(response.body() == null || !response.isSuccessful)) {

                    if (response.body()!!.error) {


                        Toast.makeText(
                            this@EditProfileActivity,
                            response.body()!!.message,
                            Toast.LENGTH_SHORT
                        ).show()

                    } else {

                        Toast.makeText(
                            this@EditProfileActivity,
                            "Your profile is update successfull...",
                            Toast.LENGTH_SHORT
                        ).show()
                        val intent = Intent(this@EditProfileActivity, ProfileActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                        finish()

                    }

                } else {

                    Toast.makeText(
                        this@EditProfileActivity,
                        resources.getString(R.string.error_admin),
                        Toast.LENGTH_SHORT
                    ).show()

                }
            }

            override fun onFailure(call: Call<ResponseUpdateProfile?>, t: Throwable) {
                t.printStackTrace()
                pd!!.dismiss()
            }
        })
    }

    private fun getProfile(profileId: String) {
        pd = ProgressDialog(this)
        pd!!.setMessage("Profile is loading...")
        pd!!.setCancelable(false)
        pd!!.show()
        val api: Api = RetrofitClient.client!!.create(Api::class.java)
        val call: Call<ResponseGetProfile?>? = api.getProfile(
            "Bearer " + WS_URL_PARAMS.createJWT(WS_URL_PARAMS.issuer, WS_URL_PARAMS.subject),
            WS_URL_PARAMS.access_key,
            profileId
        )
        call!!.enqueue(object : Callback<ResponseGetProfile?> {
            override fun onResponse(
                call: Call<ResponseGetProfile?>,
                response: Response<ResponseGetProfile?>
            ) {

                Log.e("responseGetProfile", Gson().toJson(response.body()))

                if (pd!!.isShowing) {
                    pd!!.dismiss()
                }

                if (!(response.body() == null || !response.isSuccessful)) {

                    if (response.body()!!.error) {


                        Toast.makeText(
                            this@EditProfileActivity,
                            response.body()!!.message,
                            Toast.LENGTH_SHORT
                        ).show()

                    } else {


                        if (response.body()!!.name == "") {
                            binding.edtName.text = Editable.Factory.getInstance().newEditable("")
                        } else {
                            binding.edtName.text =
                                Editable.Factory.getInstance().newEditable(response.body()!!.name)
                        }

                        if (response.body()!!.mobile == "") {
                            binding.edtPhoneNumber.text =
                                Editable.Factory.getInstance().newEditable("")
                        } else {
                            binding.edtPhoneNumber.text =
                                Editable.Factory.getInstance().newEditable(response.body()!!.mobile)
                        }

                        if (response.body()!!.email == "") {
                            binding.edtEmail.text = Editable.Factory.getInstance().newEditable("")
                        } else {
                            binding.edtEmail.text =
                                Editable.Factory.getInstance().newEditable(response.body()!!.email)
                        }

                        countryId = response.body()!!.country_id
                        stateId = response.body()!!.state_id
                        cityId = response.body()!!.city_id


                        if (response.body()!!.detail == "") {
                            binding.edtAboutMe.text = Editable.Factory.getInstance().newEditable("")
                        } else {
                            binding.edtAboutMe.text =
                                Editable.Factory.getInstance().newEditable(response.body()!!.detail)
                        }

                        Handler().postDelayed({

                        }, 1000)

//                        binding.spCountry.setSelection(countryListAdapter!!.getItemIndexById(countryId))


                        if (response.body()!!.country != "") {
                            binding.txtCountry.text = response.body()!!.country
                        } else {
                            binding.txtCountry.text = "Select Country"
                        }

                        if (response.body()!!.state != "") {
                            binding.txtState.text = response.body()!!.state
                        } else {

                            binding.txtState.text = "Select State"
                        }

                        if (response.body()!!.city != "") {
                            binding.txtCity.text = response.body()!!.city
                        } else {

                            binding.txtCity.text = "Select City"
                        }

                        getCountry()

                        // getState(countryId)
                       // getCity(stateId)
                    }

                } else {

                    Toast.makeText(
                        this@EditProfileActivity,
                        resources.getString(R.string.error_admin),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<ResponseGetProfile?>, t: Throwable) {
                t.printStackTrace()
                pd!!.dismiss()
            }
        })
    }


    private fun getCountry() {

        val api: Api = RetrofitClient.client!!.create(Api::class.java)
        val call: Call<ResponseGetCountry?>? = api.getCountry(
            "Bearer " + WS_URL_PARAMS.createJWT(WS_URL_PARAMS.issuer, WS_URL_PARAMS.subject),
            WS_URL_PARAMS.access_key
        )
        call!!.enqueue(object : Callback<ResponseGetCountry?> {
            override fun onResponse(
                call: Call<ResponseGetCountry?>,
                response: Response<ResponseGetCountry?>
            ) {

                Log.e("responseGetCountry", Gson().toJson(response.body()))


                if (!(response.body() == null || !response.isSuccessful)) {

                    if (response.body()!!.error) {


                        Toast.makeText(
                            this@EditProfileActivity,
                            response.body()!!.message,
                            Toast.LENGTH_SHORT
                        ).show()

                    } else {

                        countryList = response.body()!!.countryList as ArrayList<Country>

                      /*  val country1 = Country("0", "Select Country")
                        countryList.add(0, country1)*/
                        countryListSearchAdapter =
                            CountryListSearchAdapter(
                                countryList,
                                this@EditProfileActivity
                            )
                        customDialog = CustomListViewDialogWithSearchCountry(
                            this@EditProfileActivity,
                            countryListSearchAdapter,
                            countryList
                        )
                        // customDialog = CustomListViewDialogWithSearch(this@NewBusinessActivity, R.style.DialogStyle)
                        customDialog!!.window!!.setBackgroundDrawableResource(android.R.color.transparent)

                        //customDialog!!.show()
                       // customDialog!!.setCanceledOnTouchOutside(false)
                        
                       /* countryListAdapter =
                            CountryAdapter(
                                this@EditProfileActivity,
                                R.layout.raw_recyclear_view_drop_down,
                                R.id.tvName,
                                countryList
                            )
                        binding.spCountry.adapter = countryListAdapter
                        Log.e("position",countryListAdapter!!.getItemIndexById(countryId).toString())

                        //binding.spCountry.setSelection(1)

                       // Log.e("CountryList", "onResponse: $countryList")

                        Log.e("CountryId", "onResponse: $countryId")
                        binding.spCountry.setSelection(countryListAdapter!!.getItemIndexById(countryId))*/

                    }

                } else {

                    Toast.makeText(
                        this@EditProfileActivity,
                        resources.getString(R.string.error_admin),
                        Toast.LENGTH_SHORT
                    ).show()

                }
            }

            override fun onFailure(call: Call<ResponseGetCountry?>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }


    private fun getState(countryId: String) {

        val api: Api = RetrofitClient.client!!.create(Api::class.java)
        val call: Call<ResponseGetStateList?>? = api.getState(
            "Bearer " + WS_URL_PARAMS.createJWT(WS_URL_PARAMS.issuer, WS_URL_PARAMS.subject),
            WS_URL_PARAMS.access_key,
            countryId
        )
        call!!.enqueue(object : Callback<ResponseGetStateList?> {
            override fun onResponse(
                call: Call<ResponseGetStateList?>,
                response: Response<ResponseGetStateList?>
            ) {

                Log.e("responseGetState", Gson().toJson(response.body()))


                if (!(response.body() == null || !response.isSuccessful)) {

                    if (response.body()!!.error) {


                        Toast.makeText(
                            this@EditProfileActivity,
                            response.body()!!.message,
                            Toast.LENGTH_SHORT
                        ).show()

                    } else {

                        stateList = response.body()!!.categoryList as ArrayList<Category>
                      /*  val state = Category("0", "0", "Select State")
                        stateList.add(0, state)*/

                        stateListSearchAdapter =
                            StateListSearchAdapter(
                                stateList,
                                this@EditProfileActivity
                            )
                        customDialogState = CustomListViewDialogWithSearchState(
                            this@EditProfileActivity,
                            stateListSearchAdapter,
                            stateList
                        )
                        // customDialog = CustomListViewDialogWithSearch(this@NewBusinessActivity, R.style.DialogStyle)
                        customDialogState!!.window!!.setBackgroundDrawableResource(android.R.color.transparent)

                       // customDialogState!!.show()
                       // customDialog!!.setCanceledOnTouchOutside(false)
                       /* stateListAdapter = StateListAdapter(
                            this@EditProfileActivity,
                            R.layout.raw_recyclear_view_drop_down,
                            R.id.tvName,
                            stateList
                        )
                        binding.spState.adapter = stateListAdapter
                        Log.e("positionState", stateListAdapter!!.getItemIndexById(stateId).toString())
                        binding.spState.setSelection(stateListAdapter!!.getItemIndexById(stateId));*/
                    }

                } else {

                    Toast.makeText(
                        this@EditProfileActivity,
                        resources.getString(R.string.error_admin),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<ResponseGetStateList?>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }

    private fun getCity(stateId: String) {

        val api: Api = RetrofitClient.client!!.create(Api::class.java)
        val call: Call<ResponseCityList>? = api.getCity(
            "Bearer " + WS_URL_PARAMS.createJWT(WS_URL_PARAMS.issuer, WS_URL_PARAMS.subject),
            WS_URL_PARAMS.access_key,
            stateId
        )
        call!!.enqueue(object : Callback<ResponseCityList?> {
            override fun onResponse(
                call: Call<ResponseCityList?>,
                response: Response<ResponseCityList?>
            ) {

                Log.e("responseGetCity", Gson().toJson(response.body()))


                if (!(response.body() == null || !response.isSuccessful)) {

                    if (response.body()!!.error) {


                        Toast.makeText(
                            this@EditProfileActivity,
                            response.body()!!.message,
                            Toast.LENGTH_SHORT
                        ).show()

                    } else {

                        cityList =
                            response.body()!!.categoryList as ArrayList<com.arp.citysipspreadster.model.cityList.Category>
                        /*val city = com.arp.citysipspreadster.model.cityList.Category(
                            "0",
                            "Select State",
                            "0"
                        )
                        cityList.add(0, city)*/

                        cityListSearchAdapter =
                            CityListSearchAdapter(
                                cityList,
                                this@EditProfileActivity
                            )
                        customDialogCity = CustomListViewDialogWithSearchCity(
                            this@EditProfileActivity,
                            cityListSearchAdapter,
                            cityList
                        )
                        // customDialog = CustomListViewDialogWithSearch(this@NewBusinessActivity, R.style.DialogStyle)
                        customDialogCity!!.window!!.setBackgroundDrawableResource(android.R.color.transparent)

                      //  customDialogCity!!.show()
                       // customDialog!!.setCanceledOnTouchOutside(false)
                      /*  cityListAdapter = CityListAdapter(
                            this@EditProfileActivity,
                            R.layout.raw_recyclear_view_drop_down,
                            R.id.tvName,
                            cityList
                        )
                        binding.spCity.adapter = cityListAdapter
                        binding.spCity.setSelection(cityListAdapter!!.getItemIndexById(cityId));*/

                    }

                } else {

                    Toast.makeText(
                        this@EditProfileActivity,
                        resources.getString(R.string.error_admin),
                        Toast.LENGTH_SHORT
                    ).show()

                }
            }

            override fun onFailure(call: Call<ResponseCityList?>, t: Throwable) {
                t.printStackTrace()

            }
        })
    }

    override fun clickOnPartyListItem(data: String?, id: String?) {
        binding.txtCountry.text = data
        country = id.toString()
        if (country == "") {
            Toast.makeText(
                this@EditProfileActivity,
                "Please select Country first",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            getState(country)
        }

        stateList = ArrayList<Category>()
        binding.txtState.text = "Select State"
        state = ""


        cityList = ArrayList<com.arp.citysipspreadster.model.cityList.Category>()
        binding.txtCity.text = "Select City"
        city = ""

        if (customDialog!!.isShowing) {
            customDialog!!.dismiss()
        }
    }

    override fun clickOnStateListItem(data: String?, id: String?) {
        binding.txtState.text = data
        state = id.toString()
        if (state == "") {
            Toast.makeText(
                this@EditProfileActivity,
                "Please select State first",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            getCity(state)
        }

        cityList = ArrayList<com.arp.citysipspreadster.model.cityList.Category>()
        binding.txtCity.text = "Select City"
        city = ""

        if (customDialogState!!.isShowing) {
            customDialogState!!.dismiss()
        }
    }

    override fun clickOnCityListItem(data: String?, id: String?) {
        binding.txtCity.text = data
        city = id.toString()

        if (customDialogCity!!.isShowing) {
            customDialogCity!!.dismiss()
        }
    }
}