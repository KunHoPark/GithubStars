package com.leo.githubstars.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import com.leo.githubstars.application.MyGithubStarsApp

object NetworkUtil {

    fun isNetworkAvailAble(): Boolean {
        val cm = MyGithubStarsApp.context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
        return activeNetwork?.isConnected == true
    }
}