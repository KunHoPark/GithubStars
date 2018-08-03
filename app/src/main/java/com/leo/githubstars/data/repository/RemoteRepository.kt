package com.leo.githubstars.data.repository

import com.leo.githubstars.application.MyGithubStarsApp
import com.leo.githubstars.data.local.SearchData
import com.leo.githubstars.data.remote.api.RemoteApi
import com.leo.githubstars.data.remote.model.GithubAccessToken
import com.leo.githubstars.util.NetworkUtils
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import java.io.IOException
import java.util.*


class RemoteRepository(private val unauthRestAdapter: RemoteApi, private val authRestAdapter: RemoteApi) {
    private val tag = this.javaClass.simpleName


    fun getAccessToken(clientId: String, clientSecret: String, code: String): Observable<GithubAccessToken> {
        return unauthRestAdapter.getAccessToken(clientId, clientSecret, code)
                .subscribeOn(Schedulers.io())
    }

    /**
     * 서버를 통해 검색어에 대한 결과 값을 가져 온다.
     */
    fun loadSearchDataFromGithub(searchValue: String, page: Int, perPage: Int=20): Flowable<SearchData> {

        return if (isNetworkAvailAble()) {
            val quereis = HashMap<String, String>()
            quereis["q"] = searchValue
            quereis["in"] = "Alogin"
            quereis["page"] = page.toString()
            quereis["per_page"] = perPage.toString()
            quereis["sort"] = "login"
            quereis["order"] = "asc"

            authRestAdapter.getGithub(quereis).subscribeOn(Schedulers.io())
        }else{
            Flowable.error {throw IOException("Network connection fail")}
        }
    }

    /**
     * 네트워크 연결 상태 확인. 만약 미 연결되어 있으면 Exception 처리 한다.
     */
    private fun isNetworkAvailAble(): Boolean {
        if (NetworkUtils.isNetworkAvailable(MyGithubStarsApp.applicationContext())) {
            return true
        }

        return false
    }


}