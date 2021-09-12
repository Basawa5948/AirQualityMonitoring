package com.android.proximityapp.network

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.android.proximityapp.data.AQIData
import com.google.gson.Gson
import okhttp3.*
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type


class WebServerClient(){
    private lateinit var instance:WebServerClient
    private val WEBSOCKET_URL = "ws://city-ws.herokuapp.com/"
    private val TAG = "WebServerClient"
    private var client = OkHttpClient()
    private var gson = Gson()
    private var liveAQIData:MutableLiveData<ArrayList<AQIData>> = MutableLiveData()
    var liveErrorData:MutableLiveData<String> = MutableLiveData()

    private inner class WebServerClientListener():WebSocketListener(){
        override fun onOpen(webSocket: WebSocket, response: Response) {
            super.onOpen(webSocket, response)
            Log.d(TAG,"onOpen = "+response.body)
        }

        override fun onMessage(webSocket: WebSocket, text: String) {
            super.onMessage(webSocket, text)
            Log.d(TAG, "onMessage = $text")
            formModelClass(text)
        }

        override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
            super.onClosing(webSocket, code, reason)
            Log.d(TAG, "onClosing = $reason")
        }

        override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
            super.onClosed(webSocket, code, reason)
            Log.d(TAG, "onClosed = $reason")
            liveErrorData.postValue("WebSocket Closed for $reason")
        }

        override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
            super.onFailure(webSocket, t, response)
            Log.d(TAG,"onFailure = "+t.localizedMessage)
            liveErrorData.postValue("WebSocket Failed for ${t.localizedMessage}")
        }
    }

    private fun formModelClass(text: String) {
        val collectionType: Type = object : TypeToken<ArrayList<AQIData>>() {}.type
        val enums: ArrayList<AQIData> = gson.fromJson(text, collectionType)
        liveAQIData.postValue(enums)
    }

    fun getLatestAQIData(): MutableLiveData<ArrayList<AQIData>> {
        return liveAQIData
    }

    fun fetchDataFromSocket() {
        val request = Request.Builder().url(WEBSOCKET_URL).build()
        val listener = WebServerClientListener()
        val ws:WebSocket = client.newWebSocket(request, listener)
        client.dispatcher.executorService.shutdown()
    }
}