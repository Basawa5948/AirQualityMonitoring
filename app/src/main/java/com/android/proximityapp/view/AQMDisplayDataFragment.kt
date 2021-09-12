package com.android.proximityapp.view

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.proximityapp.R
import com.android.proximityapp.adapter.AQMDisplayDataAdapter
import com.android.proximityapp.data.AQIData
import com.android.proximityapp.viewmodel.AQMDisplayDataViewModel

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private val TAG = AQMDisplayDataFragment::class.java.simpleName

class AQMDisplayDataFragment : Fragment(),AQMDisplayDataAdapter.SelectedCity {

    private var param1: String? = null
    private var param2: String? = null
    private lateinit var aqmDisplayDataViewModel: AQMDisplayDataViewModel
    private lateinit var aqiRecyclerView:RecyclerView
    private lateinit var mContext: Context
    private lateinit var myAQIAdapter:AQMDisplayDataAdapter
    private lateinit var rootLayout:RelativeLayout
    private lateinit var progressBar: ProgressBar
    private lateinit var mapForGraph: HashMap<String,ArrayList<Int>>
    private lateinit var dataForGraph: DataForGraph

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String, listener:DataForGraph) =
            AQMDisplayDataFragment().apply {
                dataForGraph = listener
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
            aqmDisplayDataViewModel = ViewModelProvider(this).get(AQMDisplayDataViewModel::class.java)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_aqm_displaydata, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mapForGraph = HashMap()
        aqiRecyclerView = view.findViewById(R.id.aqi_recyclerView)
        rootLayout = view.findViewById(R.id.rootLayout)
        progressBar = ProgressBar(mContext,null, android.R.attr.progressBarStyleLarge)
        val params = RelativeLayout.LayoutParams(100, 100)
        params.addRule(RelativeLayout.CENTER_IN_PARENT)
        rootLayout.addView(progressBar, params)
        progressBar.visibility = View.VISIBLE
        aqiRecyclerView.layoutManager = LinearLayoutManager(mContext)
        myAQIAdapter = AQMDisplayDataAdapter(mContext,this)
        aqiRecyclerView.adapter = myAQIAdapter
        lookForData()
        lookForErrorIfAny()
    }

    private fun lookForErrorIfAny() {
        aqmDisplayDataViewModel.getErrorMessageFromSocket().observe(viewLifecycleOwner,{
            progressBar.visibility = View.GONE
            Toast.makeText(mContext,it,Toast.LENGTH_LONG).show()
        })
    }

    private fun lookForData() {
        aqmDisplayDataViewModel.initiateConnection()
        aqmDisplayDataViewModel.getLatestDataForAirQuality().observe(viewLifecycleOwner, Observer { listOfAQIData ->
            processDataForGraph(listOfAQIData)
            progressBar.visibility = View.GONE
            myAQIAdapter.setData(listOfAQIData)
            myAQIAdapter.notifyDataSetChanged()
        })
    }

    private fun processDataForGraph(listOfAQIData: ArrayList<AQIData>?) {
        listOfAQIData?.forEachIndexed { index, aqiData ->
            val listAQIValues = ArrayList<Int>()
            listAQIValues.add(aqiData.aqi.toInt())
            if(mapForGraph.size!=0){
                if (!mapForGraph.containsKey(aqiData.city)) {
                    //city does not exists, hence adding to the hashMap
                    mapForGraph[aqiData.city] = listAQIValues
                } else {
                    //city already exists, add the new aqi values
                    for ((key, value) in mapForGraph) {
                        if (key == aqiData.city) {
                            value.addAll(listAQIValues)
                        }
                    }
                }
            }else{
                //first time hashmap is empty, hence adding the city
                mapForGraph[aqiData.city] = listAQIValues
            }
        }
        for((key,value ) in mapForGraph){
            Log.d("AlgorithmForGraph","Key = $key and Value = $value")
        }
    }

    override fun getSelectedCity(cityName: String) {
        Log.d(TAG,"Selected City = $cityName")
        for ((key, value) in mapForGraph) {
            if (key == cityName) {
                dataForGraph.getDataForGraph(cityName,value)
                break
            }
        }
    }

    interface DataForGraph{
        fun getDataForGraph(cityName:String,list:ArrayList<Int>)
    }
}