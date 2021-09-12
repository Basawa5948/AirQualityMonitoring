package com.android.proximityapp.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.proximityapp.R
import com.android.proximityapp.adapter.AQMDisplayDataAdapter.MyViewHolder
import com.android.proximityapp.data.AQIData
import com.android.proximityapp.utility.HelperUtility
import com.android.proximityapp.utility.StringConstants
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class AQMDisplayDataAdapter(private val mContext:Context,private val listener:SelectedCity):
            RecyclerView.Adapter<MyViewHolder>() {

    private var listOfAQIData:ArrayList<AQIData> = ArrayList()
    private var oldValue = ""
    private var newValue = ""
    private val helperUtility = HelperUtility()

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
            val colorCode = helperUtility.getColorFromAQIValue(aqiData.aqi.toInt())
            aqiValue.setTextColor(mContext.resources.getColor(colorCode))
            val presentTime = "$hour:$minutes:$seconds $AM_PM"
            Log.d("Present Time", "= $presentTime")
            if(cityName.text.equals(aqiData.city) && aqiValue.text.equals(aqiData.aqi.toString())){
                lastUpdatedValue.text = presentTime
            }else{
                if(lastUpdatedValue.text.isNotEmpty()) {
                    val tempPresentTime:String = presentTime
                    val tempLastUpdatedValue = if(lastUpdatedValue.text.equals(StringConstants.HEADINGONE) ||
                        lastUpdatedValue.text.equals(StringConstants.HEADINGTWO)){
                        tempPresentTime
                    }else {
                        lastUpdatedValue.text as String
                    }
                    val finalValue = helperUtility.getMinuteFromTime(tempLastUpdatedValue)
                    oldValue = finalValue[0]
                    newValue = minutes.toString()
                    lastUpdatedValue.text = if(helperUtility.getLastUpdatedValue(newValue.toInt(),oldValue.toInt()).isEmpty()){
                         presentTime
                    }else{
                         helperUtility.getLastUpdatedValue(newValue.toInt(),oldValue.toInt())
                    }
                }else{
                    lastUpdatedValue.text = presentTime
                }
            }
            rootLayout.setOnClickListener(View.OnClickListener {
                listener.getSelectedCity(aqiData.city)
            })
        }
    }

    override fun getItemCount(): Int {
        return listOfAQIData.size
    }

    interface SelectedCity{
        fun getSelectedCity(cityName:String)
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var rootLayout:TableLayout = itemView.findViewById(R.id.rootTableLayout)
        var cityName:TextView = itemView.findViewById(R.id.cityName)
        var aqiValue:TextView = itemView.findViewById(R.id.aqiValue)
        var lastUpdatedValue:TextView = itemView.findViewById(R.id.lastUpdated)
    }
}