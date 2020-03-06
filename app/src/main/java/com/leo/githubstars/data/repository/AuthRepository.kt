package com.leo.githubstars.data.repository

import com.leo.githubstars.data.remote.api.RemoteApi
import com.leo.githubstars.data.remote.model.GithubAccessToken
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers

/**
 * Github Token 정보를 전달 하기 위한 Repository.
 * @author LeoPark
 */
class AuthRepository(private val unauthRestAdapter: RemoteApi) {

    /**
     * Github auth token 가져오기.
     */
    fun getAccessToken(clientId: String, clientSecret: String, code: String): Observable<GithubAccessToken> {
        return unauthRestAdapter.getAccessToken(clientId, clientSecret, code)
                .subscribeOn(Schedulers.io())
    }

}