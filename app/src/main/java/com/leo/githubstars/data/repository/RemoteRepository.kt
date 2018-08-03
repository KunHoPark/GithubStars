package com.leo.githubstars.data.repository

import com.leo.githubstars.application.GithubStarsApp
import com.leo.githubstars.data.local.SearchData
import com.leo.githubstars.data.remote.api.RemoteApi
import com.leo.githubstars.util.NetworkUtils
import io.reactivex.Flowable
import io.reactivex.schedulers.Schedulers
import java.io.IOException
import java.util.*


class RemoteRepository(private val remoteApi: RemoteApi) {
    private val tag = this.javaClass.simpleName

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

            remoteApi.getGithub(quereis).subscribeOn(Schedulers.io())
        }else{
            Flowable.error {throw IOException("Network connection fail")}
        }
    }

    /**
     * 네트워크 연결 상태 확인. 만약 미 연결되어 있으면 Exception 처리 한다.
     */
    private fun isNetworkAvailAble(): Boolean {
        if (NetworkUtils.isNetworkAvailable(GithubStarsApp.applicationContext())) {
            return true
        }

        return false
    }


}