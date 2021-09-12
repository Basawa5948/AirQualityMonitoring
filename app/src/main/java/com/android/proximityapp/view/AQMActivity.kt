package com.android.proximityapp.view

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.android.proximityapp.R

import android.net.ConnectivityManager
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.FragmentManager

class AQMActivity : AppCompatActivity(),AQMDisplayDataFragment.DataForGraph {
    companion object{
        val TAG = AQMActivity::class.java.simpleName
        val DISPLAY_FRAGMENT = AQMDisplayDataFragment::class.java.simpleName
        val MONITOR_FRAGMENT = AQMMonitoringFragment::class.java.simpleName
    }

    override fun onBackPressed() {
        val fm: FragmentManager = supportFragmentManager
        if (fm.backStackEntryCount > 0) {
            Log.i("MainActivity", "popping backstack")
            fm.popBackStack()
        } else {
            Log.i("MainActivity", "nothing on backstack, calling super")
            super.onBackPressed()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_aqm)
        if(savedInstanceState==null && isNetworkAvailable()){
            loadDataFragment()
        }else{
            Toast.makeText(this,"No  Internet Connection",Toast.LENGTH_LONG).show()
            finish()
        }
    }

    private fun loadDataFragment() {
        val dataFragment = supportFragmentManager.findFragmentByTag(DISPLAY_FRAGMENT)
        if(dataFragment==null){
            supportFragmentManager.beginTransaction().add(R.id.aqmFragment,
                AQMDisplayDataFragment.newInstance("","",this), DISPLAY_FRAGMENT)
                .commit()
        }
    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

    override fun getDataForGraph(cityName:String,list: ArrayList<Int>) {
        //Final Data With All AQIValues For The Selected City
        Log.d(TAG,"List of AQI Values = ${list.joinToString()}")
        loadMonitorFragment(cityName,list)
    }

    private fun loadMonitorFragment(cityName:String,list: ArrayList<Int>) {
        val monitorFragment = supportFragmentManager.findFragmentByTag(MONITOR_FRAGMENT)
        if(monitorFragment==null){
            supportFragmentManager.beginTransaction().replace(R.id.aqmFragment,
                AQMMonitoringFragment.newInstance(cityName,list), MONITOR_FRAGMENT).addToBackStack(MONITOR_FRAGMENT).commit()
        }
    }
}