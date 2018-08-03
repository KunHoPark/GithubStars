package com.leo.githubstars.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo

class NetworkUtils {

    companion object {
        fun isNetworkAvailable(context: Context): Boolean {
            getConnectivityManager(context).activeNetworkInfo?.run {
                return (state == NetworkInfo.State.CONNECTED || state == NetworkInfo.State.CONNECTING)
            } ?: return false
        }

        fun getNetworkType(context: Context): Int = getConnectivityManager(context).activeNetworkInfo.type

        fun getNetworkTypeAsString(context: Context): String =
                when (getConnectivityManager(context).activeNetworkInfo.type) {
                    ConnectivityManager.TYPE_WIFI -> "WiFi"
                    ConnectivityManager.TYPE_MOBILE -> "Mobile"
                    else -> ""
                }


        private fun getConnectivityManager(context: Context): ConnectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }


}