package com.leo.githubstars.data.repository

import androidx.lifecycle.LiveData
import com.leo.githubstars.application.MyGithubStarsApp
import com.leo.githubstars.data.local.SearchData
import com.leo.githubstars.data.local.UserDao
import com.leo.githubstars.data.local.UserData
import com.leo.githubstars.data.remote.api.RemoteApi
import com.leo.githubstars.util.NetworkUtil
import io.reactivex.Flowable
import io.reactivex.schedulers.Schedulers
import org.intellij.lang.annotations.Flow
import java.io.IOException
import java.util.*

/**
 * Github 서버 & DB의 Repository.
 * @author LeoPark
 */
class RemoteRepository(private val remoteApi: RemoteApi, private val userDao: UserDao) {

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
     * 검색어를 통해 DB로 부터 검색데이타를 가져 온다.
     */
    fun loadSearchDataFromDb(keyword: String): Flowable<List<UserData>> {
        return userDao.searchLiveUserDataRaw("%$keyword%")
    }

    fun getUserDetailFromGithub(userData: UserData): Flowable<UserData> {
        return remoteApi.getUserDetail(userData.login)
    }

    /**
     * Bookmark db에 유저 정보를 저장 한다.
     */
    fun insertUserDataFromDb(userData: UserData) {
        if (userDao.getUserDataById(userData.id.toInt()) == null){
            userDao.insert(userData)
        }
    }

    /**
     * Bookmark db에서 삭제 한다.
     */
    fun deleteUserDataFromDb(userData: UserData) {
        userDao.delete(userData)
    }

    fun getUserDataFromDb(): LiveData<List<UserData>> = userDao.getLiveUserData()


    /**
     * 네트워크 연결 상태 확인. 만약 미 연결되어 있으면 Exception 처리 한다.
     */
    private fun isNetworkAvailAble(): Boolean {
        if (NetworkUtil.isNetwork()) {
            return true
        }

        return false
    }


}