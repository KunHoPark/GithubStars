package com.leo.githubstars.data.remote.api

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

/**
 * Github의 Token 정보를 Retrofit Module로 전달한다.
 * @author LeoPark
 */
class AuthInterceptor(private val token: String) : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain)
            : Response = with(chain) {
        val newRequest = request().newBuilder().run {
            addHeader("Authorization", "token " + token)
            build()
        }
        proceed(newRequest)
    }
}
