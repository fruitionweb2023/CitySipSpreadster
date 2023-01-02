package com.arp.citysipspreadster.activites

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class FargmentViewModel : ViewModel() {

    val data = MutableLiveData<String>()

    fun setData(newData : String) {
        data.value = newData
    }
}