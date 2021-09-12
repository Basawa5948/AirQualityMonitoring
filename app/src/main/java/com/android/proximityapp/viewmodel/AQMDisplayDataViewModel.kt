package com.android.proximityapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.proximityapp.data.AQIData
import com.android.proximityapp.network.WebServerClient

class AQMDisplayDataViewModel:ViewModel() {

    private  var webServerClient = WebServerClient()
    var mutableLiveData: MutableLiveData<ArrayList<AQIData>> = MutableLiveData()

    fun initiateConnection(){
        webServerClient.fetchDataFromSocket()
        mutableLiveData = webServerClient.getLatestAQIData()
    }

    fun getLatestDataForAirQuality(): MutableLiveData<ArrayList<AQIData>> {
        return mutableLiveData
    }

    fun getErrorMessageFromSocket(): MutableLiveData<String>{
        return webServerClient.liveErrorData
    }

}