package com.android.proximityapp.view

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.proximityapp.R
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet

private const val CITY_NAME = "cityName"
private const val AQI_VALUES = "aqiValues"

class AQMMonitoringFragment : Fragment() {
    private lateinit var selectedCity: String
    private lateinit var selectedCityValues: ArrayList<Int>
    private lateinit var mContext: Context
    private val TAG = AQMMonitoringFragment::class.java.simpleName
    private lateinit var mpLineChart: LineChart
    private lateinit var iLineDataSet:ArrayList<ILineDataSet>
    private lateinit var lineData:LineData

    companion object {
        @JvmStatic
        fun newInstance(cityName: String, aqiValues: ArrayList<Int>) =
            AQMMonitoringFragment().apply {
                arguments = Bundle().apply {
                    putString(CITY_NAME, cityName)
                    putIntegerArrayList(AQI_VALUES, aqiValues)
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            selectedCity = it.getString(CITY_NAME).toString()
            selectedCityValues = it.getIntegerArrayList(AQI_VALUES) as ArrayList<Int>
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_aqm_monitoring, container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG,"City Name = $selectedCity And " +
                "AQIValues = ${selectedCityValues.joinToString()}")
        mpLineChart = view.findViewById(R.id.rootLineChart)
        //forming datasets for graph
        val lineDataSet = LineDataSet(formEntryListForGraph(),selectedCity)
        iLineDataSet = ArrayList()
        iLineDataSet.add(lineDataSet)
        lineData = LineData(iLineDataSet)
        mpLineChart.data = lineData
        mpLineChart.setDrawGridBackground(true)
        mpLineChart.setDrawBorders(true)
        mpLineChart.setBorderColor(mContext.resources.getColor(R.color.good))
        mpLineChart.setBorderWidth(2F)
        mpLineChart.invalidate()

        val desc = Description()
        desc.text = "Real Time Chart"
        desc.textColor = mContext.resources.getColor(R.color.purple_700)
        desc.textSize = 12F
        mpLineChart.description = desc
    }

    fun formEntryListForGraph(): ArrayList<Entry> {
        val xyList = ArrayList<Entry>()
        selectedCityValues.forEachIndexed { index, value ->
            xyList.add(Entry(index.toFloat(), value.toFloat()))
        }
        return xyList
    }
}