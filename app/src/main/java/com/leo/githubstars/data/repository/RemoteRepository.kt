package com.leo.githubstars.data.repository

import android.arch.lifecycle.LiveData
import android.arch.persistence.db.SimpleSQLiteQuery
import com.leo.githubstars.application.MyGithubStarsApp
import com.leo.githubstars.data.local.SearchData
import com.leo.githubstars.data.local.UserDao
import com.leo.githubstars.data.local.UserData
import com.leo.githubstars.data.remote.api.RemoteApi
import com.leo.githubstars.util.NetworkUtils
import io.reactivex.Flowable
import io.reactivex.schedulers.Schedulers
import java.io.IOException
import java.util.*


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
     * Bookmark db에서 문자열 검색.
     */
    fun loadSearchDataFromDb(searchValue: String): LiveData<List<UserData>> {
        val searchQuery = SimpleSQLiteQuery("SELECT * FROM bookmark_user_table WHERE login LIKE '%$searchValue%' ORDER BY LOWER(login) ASC")
        return userDao.searchLiveUserDataRaw(searchQuery)
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
        if (NetworkUtils.isNetworkAvailable(MyGithubStarsApp.applicationContext())) {
            return true
        }

        return false
    }


}