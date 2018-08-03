package com.leo.githubstars.data.remote.api


import com.leo.githubstars.data.local.SearchData
import io.reactivex.Flowable
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.QueryMap


interface RemoteApi {

    @GET("collections/archive/slim-aarons.aspx")
    fun getCollections(): Call<String>

    @GET("picture-library/image.aspx")
    fun getImageDetail(@Query("id") id: String): Call<String>


    @GET("search/users")
    fun getGithub(@QueryMap queries: Map<String, String> ): Flowable<SearchData>

}