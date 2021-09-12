package com.android.proximityapp.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class AQIData {
    @SerializedName("city")
    @Expose
    var city: String = ""

    @SerializedName("aqi")
    @Expose
    var aqi: Double = 0.0
}