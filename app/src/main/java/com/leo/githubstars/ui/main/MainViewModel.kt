package com.leo.githubstars.ui.main


import android.arch.lifecycle.MutableLiveData
import android.databinding.ObservableArrayList
import ccom.leo.githubstars.ui.base.BaseViewModel
import com.leo.githubstars.application.Constants
import com.leo.githubstars.data.local.UserData
import com.leo.githubstars.data.repository.RemoteRepository
import com.leo.githubstars.util.LeoLog
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class MainViewModel
@Inject constructor(private val remoteRepository: RemoteRepository) : BaseViewModel() {
    internal val tag = this.javaClass.simpleName

    private var compositeDisposable = CompositeDisposable()

    var searchUserData= ObservableArrayList<UserData>()
    var bookmarkUserHash = HashMap<String, UserData>()
    var reloadListData: MutableLiveData<Boolean> = MutableLiveData()        //List에 있는 데이타를 업데이트

    private var totalCount = 0
    private var currentPage = 0
    private var beforeSearchValue: String?= null

    /**
     * 검색어를 통해 github api로 부터 검색데이타를 가져 온다.
     */
    fun loadSearchDataFromGithub(searchValue: String, isReload: Boolean) {
        LeoLog.i(tag, "loadSearchDataFromGithub searchValue=$searchValue")

        Flowable.just(searchValue)
                .subscribeOn(Schedulers.io())
                .filter {
                    //3 이상 일때만.
                    searchValue.length >= 3
                }
                .debounce(300, TimeUnit.MILLISECONDS)               //정해진 안에 들어 오는 키는 무시하고 마지막에 입력된 키만 처리 한다.
                .map {
                    LeoLog.i(tag, "loadSearchDataFromGithub debounce searchValue=$searchValue")
                    // 이전에 입력 했던 키 값이 변경 되었으면 모든 값을 초기화 한다.
                    beforeSearchValue?.let {
                        if (it != searchValue){
                            totalCount = 0
                            currentPage = 0
                            searchUserData.removeAll(searchUserData)
                        }
                    }
                    beforeSearchValue = searchValue
                    return@map it
                }
                .map {
                    if (isReload){
                        totalCount = 0
                        currentPage = 0
                        searchUserData.removeAll(searchUserData)
                    }
                }
                .filter {
                    //다음 페이지가 있는지를 확인 한다.
                    totalCount > currentPage*Constants.PERPAGE || currentPage==0
                }
                .flatMap {
                    //Github 서버를 통해 값을 가져 온다.
                    remoteRepository.loadSearchDataFromGithub(searchValue, ++currentPage, Constants.PERPAGE)
                }
                .map {
                    // 서버로 부터 가져온 데이터와 DB 값을 비교 후 bookmark를 표시 한다.
                    it.items
                            .map {
                                it.isBookmark = bookmarkUserHash[it.id] != null
                            }
                    return@map it
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {
                            it?.let {
                                totalCount = it.totalCount.toInt()
                                searchUserData.addAll(it.items)
                                LeoLog.i(tag, "loadSearchDataFromGithub userData.size=${searchUserData.size}")
                            }
                        },
                        {
                            LeoLog.e(it.localizedMessage)
                        }
                )
                .apply {
                    compositeDisposable.add(this)
                }
    }

    /**
     * DB 정보가 갱신 되면 remote 데이타도 같이 업데이트 한다.
     */
    fun mergeSearchDataAndBookmarkData(userDataFromDb: List<UserData>) {
        synchronized(this) {

            Observable.just(userDataFromDb)
                    .subscribeOn(Schedulers.io())
                    .debounce(300, TimeUnit.MILLISECONDS)
                    .map {
                        // hash 값은 만들어 주자, 검색 할 때 사용 하기 때문.
                        bookmarkUserHash.clear()
                        it.forEach {
                            bookmarkUserHash[it.id] = it
                        }
                        return@map searchUserData
                    }
                    .filter {
                        searchUserData.size > 0
                    }
                    .map {
                        it
                                .map {
                                    it.isBookmark = bookmarkUserHash[it.id] != null
                                }
                    }
                    .subscribe {
                        reloadListData.postValue(true)
                    }
                    .apply {
                        compositeDisposable.add(this)
                    }

        }
    }

    /**
     * Bookmark db에 유저 정보를 업데이트 한다. 이 bookmark 여부를 업데이트 한다.
     */
    fun updateBookmark(userData: UserData){
        when (userData.isBookmark) {
            true -> {
                userData.isBookmark = false
                remoteRepository.deleteUserDataFromDb(userData)
            }
            else -> {
                userData.isBookmark = true
                remoteRepository.insertUserDataFromDb(userData)
            }
        }
    }


    fun getUserDataFromDb()  = remoteRepository.getUserDataFromDb()


    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }


}
