package com.leo.githubstars.util

import android.content.Context
import android.content.SharedPreferences

/**
* LeoSharedPreferences
* @author LeoPark
**/
class LeoSharedPreferences(context: Context) {

    private val preferenceFileName = "MyGithubStarsPref"
    private val sharedPreferences: SharedPreferences


    init {
        sharedPreferences = context.getSharedPreferences(preferenceFileName, Context.MODE_PRIVATE)
    }

    fun setInt(key: String, value: Int) {
        sharedPreferences.edit().run {
            putInt(key,value)
            apply()
        }
    }

    fun setIntCommit(key: String, value: Int) {
        sharedPreferences.edit().run {
            putInt(key,value)
            commit()
        }
    }

    fun getInt(key: String) : Int = sharedPreferences.getInt(key, -1)

    fun setString(key: String, value: String) {
        sharedPreferences.edit().run {
            putString(key, value)
            apply()
        }
    }

    fun setStringCommit(key: String, value : String) {
        sharedPreferences.edit().run {
            putString(key, value)
            commit()
        }
    }

    fun getString(key: String) : String? = sharedPreferences.getString(key, null)

    fun setBoolean(key: String, value: Boolean) {
        sharedPreferences.edit().run {
            putBoolean(key, value)
            apply()
        }
    }

    fun getBoolean(key: String) : Boolean = sharedPreferences.getBoolean(key, false)

    fun remove(key: String) {
        sharedPreferences.edit().remove(key).commit()
    }

    fun removeAll() {
        sharedPreferences.edit().clear().commit()
    }



}



