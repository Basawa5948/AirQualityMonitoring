package com.android.proximityapp.view

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.android.proximityapp.R
import android.net.NetworkInfo

import android.net.ConnectivityManager
import android.widget.Toast

class AQMActivity : AppCompatActivity() {
    companion object{
        val TAG = AQMActivity::class.java.simpleName
        val DISPLAY_FRAGMENT = AQMDisplayDataFragment::class.java.simpleName
        val MONITOR_FRAGMENT = AQMMonitoringFragment::class.java.simpleName
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
        val loginFragment = supportFragmentManager.findFragmentByTag(DISPLAY_FRAGMENT)
        if(loginFragment==null){
            supportFragmentManager.beginTransaction().replace(R.id.aqmFragment,
                AQMDisplayDataFragment.newInstance("",""), DISPLAY_FRAGMENT).commit()
        }
    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }
}