package com.arp.citysipspreadster.utils

import retrofit2.Retrofit
import com.arp.citysipspreadster.utils.RetrofitClient
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    private var retrofit: Retrofit? = null
    val client: Retrofit?
        get() {
            if (retrofit == null) {
                val gson = GsonBuilder()
                    .setLenient()
                    .create()
                retrofit = Retrofit.Builder()
                    .baseUrl(Api.url)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build()
            }
            return retrofit
        }
}