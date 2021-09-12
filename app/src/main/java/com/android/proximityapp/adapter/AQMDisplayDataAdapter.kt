package com.android.proximityapp.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.proximityapp.R
import com.android.proximityapp.adapter.AQMDisplayDataAdapter.MyViewHolder
import com.android.proximityapp.data.AQIData
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class AQMDisplayDataAdapter(private val mContext:Context): RecyclerView.Adapter<MyViewHolder>() {

    private var listOfAQIData:ArrayList<AQIData> = ArrayList()
    private var oldValue = ""
    private var newValue = ""
    private val HEADINGONE = "A Few Seconds Ago"
    private val HEADINGTWO = "A Minute Ago"

    fun setData(list:List<AQIData>){
        listOfAQIData.clear()
        listOfAQIData.addAll(list)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view: View = LayoutInflater.from(mContext).inflate(R.layout.aqi_data_list,parent,false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val aqiData = listOfAQIData[position]
        val date = Calendar.getInstance()
        val hour = date.get(Calendar.HOUR_OF_DAY)
        val minutes  = date.get(Calendar.MINUTE)
        val seconds  = date.get(Calendar.SECOND)
        val AM_PM = if(date.get(Calendar.AM_PM) == 1){
            "PM"
        }else{
            "AM"
        }
        with(holder){
            cityName.text = aqiData.city
            aqiValue.text = aqiData.aqi.toString()
            when(aqiData.aqi.toInt()) {
                in 1..50 ->{
                    //Category = Good
                    aqiValue.setTextColor(mContext.resources.getColor(R.color.good))
                }in 51..100 ->{
                    //Category = Satisfactory
                    aqiValue.setTextColor(mContext.resources.getColor(R.color.satisfactory))
                }in 101..200 ->{
                    //Category = Moderate
                    aqiValue.setTextColor(mContext.resources.getColor(R.color.moderate))
                }in 201..300 ->{
                    //Category = Poor
                    aqiValue.setTextColor(mContext.resources.getColor(R.color.poor))
                }in 301..400 ->{
                    //Category = Very Poor
                    aqiValue.setTextColor(mContext.resources.getColor(R.color.very_poor))
                }else ->{
                    //Category = Severe
                    aqiValue.setTextColor(mContext.resources.getColor(R.color.severe))
                }
            }
            val presentTime = "$hour:$minutes:$seconds $AM_PM"
            Log.d("Present Time", "= $presentTime")
            if(cityName.text.equals(aqiData.city) && aqiValue.text.equals(aqiData.aqi.toString())){
                lastUpdatedValue.text = presentTime
            }else{
                if(lastUpdatedValue.text.isNotEmpty()) {
                    val tempPresentTime:String = presentTime
                    val tempLastUpdatedValue = if(lastUpdatedValue.text.equals(HEADINGONE) ||
                        lastUpdatedValue.text.equals(HEADINGTWO)){
                        tempPresentTime
                    }else {
                        lastUpdatedValue.text as String
                    }
                    val finalValue = tempLastUpdatedValue.replace("(?s)^.*?:|:[^:]*$".toRegex(), "").split("(?s):([^:]*\\s[^:]*:)?".toRegex())
                    oldValue = finalValue[0]
                    newValue = minutes.toString()
                    when((newValue.toInt() - oldValue.toInt())){
                        0 ->{
                            lastUpdatedValue.text = HEADINGONE
                        }in 1..1 ->{
                            lastUpdatedValue.text = HEADINGTWO
                        }else ->{
                            lastUpdatedValue.text = presentTime
                        }
                    }
                }else{
                    lastUpdatedValue.text = presentTime
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return listOfAQIData.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var cityName:TextView = itemView.findViewById(R.id.cityName)
        var aqiValue:TextView = itemView.findViewById(R.id.aqiValue)
        var lastUpdatedValue:TextView = itemView.findViewById(R.id.lastUpdated)
    }
}