package com.android.proximityapp.view

import android.content.Context
import android.os.Bundle
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
import com.android.proximityapp.viewmodel.AQMDisplayDataViewModel

// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private val TAG = AQMDisplayDataFragment::class.java.simpleName
/**
 * A simple [Fragment] subclass.
 * Use the [AQMDisplayDataFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AQMDisplayDataFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var aqmDisplayDataViewModel: AQMDisplayDataViewModel
    private lateinit var aqiRecyclerView:RecyclerView
    private lateinit var mContext: Context
    private lateinit var myAQIAdapter:AQMDisplayDataAdapter
    private lateinit var rootLayout:RelativeLayout
    private lateinit var progressBar: ProgressBar

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
        aqiRecyclerView = view.findViewById(R.id.aqi_recyclerView)
        rootLayout = view.findViewById(R.id.rootLayout)
        progressBar = ProgressBar(mContext,null, android.R.attr.progressBarStyleLarge)
        val params = RelativeLayout.LayoutParams(100, 100)
        params.addRule(RelativeLayout.CENTER_IN_PARENT)
        rootLayout.addView(progressBar, params)
        progressBar.visibility = View.VISIBLE
        aqiRecyclerView.layoutManager = LinearLayoutManager(mContext)
        myAQIAdapter = AQMDisplayDataAdapter(mContext)
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
            progressBar.visibility = View.GONE
            myAQIAdapter.setData(listOfAQIData)
            myAQIAdapter.notifyDataSetChanged()
        })
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AQMDisplayDataFragment.
         */
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AQMDisplayDataFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}