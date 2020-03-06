package com.leo.githubstars.data.remote.api


import com.leo.githubstars.data.local.SearchData
import com.leo.githubstars.data.local.UserData
import com.leo.githubstars.data.remote.model.GithubAccessToken
import io.reactivex.Flowable
import io.reactivex.Observable
import retrofit2.http.*

/**
 * Github APIs
 * @author LeoPark
 */
interface RemoteApi {

    @FormUrlEncoded
    @POST("login/oauth/access_token")
    @Headers("Accept: application/json")
    fun getAccessToken(
            @Field("client_id") clientId: String,
            @Field("client_secret") clientSecret: String,
            @Field("code") code: String): Observable<GithubAccessToken>

    @GET("search/users")
    fun getGithub(@QueryMap queries: Map<String, String> ): Flowable<SearchData>

    @GET("users/{id}")
    fun getUserDetail(@Path("id") id: String ): Flowable<UserData>

}