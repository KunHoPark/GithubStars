package com.leo.githubstars.data.repository

import com.leo.githubstars.data.remote.api.RemoteApi
import com.leo.githubstars.data.remote.model.GithubAccessToken
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers


class AuthRepository(private val unauthRestAdapter: RemoteApi) {
    private val tag = this.javaClass.simpleName


    fun getAccessToken(clientId: String, clientSecret: String, code: String): Observable<GithubAccessToken> {
        return unauthRestAdapter.getAccessToken(clientId, clientSecret, code)
                .subscribeOn(Schedulers.io())
    }



}