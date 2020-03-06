package com.leo.githubstars.util

import android.util.Log
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.google.gson.JsonSyntaxException
import okhttp3.logging.HttpLoggingInterceptor

/**
 * Retrofit의 header/body의 로그를 출력 해준다.
 * @author LeoPark
 **/
class RetrofitLogger(private val prettyPrinting : Boolean) : HttpLoggingInterceptor.Logger{

    private val tag = "OkHttp"
    private val logChunkSize = 3200

    override fun log(message: String) {
        if(message.isNullOrEmpty()) return

        if(!prettyPrinting) {
            splitLogs(message!!)
            return
        }

        var jsonString: JsonObject? = null
        var isJson = false

        try {
            jsonString = JsonParser().parse(message) as JsonObject
            isJson = true
        } catch (e: JsonSyntaxException) {
            //e.printStackTrace()
            isJson = false
        } catch (e: Exception) {
            //e.printStackTrace()
            isJson = false
        } catch (e: Throwable) {
            isJson = false
        }

        if (!isJson) {
            printLogChunk(message!!)
            return
        }

        if (jsonString == null) return
        val prettyJsonString = GsonBuilder().setPrettyPrinting().create().toJson(jsonString)

        if (prettyJsonString.length > logChunkSize) {
            splitLogs(prettyJsonString)
        } else {
            printLogChunk(prettyJsonString)
        }
    }

    private fun splitLogs(message: String) {
        var i = 0
        val len = message.length

        while (i < len) {
            val end = Math.min(len, i + logChunkSize)
            printLogChunk(message.substring(i, end))
            i += logChunkSize
        }
    }

    private fun printLogChunk(chunk: String) {
        Log.d(tag, chunk)
    }


}