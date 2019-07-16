package com.leo.githubstars.ui.main


import androidx.databinding.ObservableArrayList
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.leo.githubstars.ui.base.BaseViewModel
import com.leo.githubstars.application.Constants
import com.leo.githubstars.data.local.UserData
import com.leo.githubstars.data.repository.RemoteRepository
import com.leo.githubstars.util.LeoLog
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.Executors
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
                .map {
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
                    // isReload == true 이면 기존 데이타는 무조건 날린다.
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
//                                getUserDetailFromGithub(it.items)
                                totalCount = it.totalCount.toInt()
                                searchUserData.addAll(it.items)     //리스트 갱신. AdapterBindings를 통해 업데이트 된다.
                            }
                        },
                        {
                            message.onNext(it.localizedMessage)
                        }
                )
                .apply {
                    compositeDisposable.add(this)
                }
    }

    /**
     * 검색어를 통해 DB로 부터 검색데이타를 가져 온다.
     */
    fun loadSearchDataFromDb(searchValue: String): LiveData<List<UserData>> {
        return remoteRepository.loadSearchDataFromDb(searchValue)
    }

    /**
     * GithubTabFragment에서 호출.
     * DB 정보가 갱신 되면 github api를 통해 가져온 UserData 중 bookmark 데이터도 같이 업데이트 한다.
     * 결론적으로 리스트에 북마크 아이콘 변경.
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
     * 개인의상세 정보 가져 오기.
     */
    private fun getUserDetailFromGithub(userData: ArrayList<UserData>){
        val executorService = Executors.newFixedThreadPool(5)

        Flowable.just(userData)
                .subscribeOn(Schedulers.io())
                .window(5)
                .flatMap { window ->
                    Flowable.just(window)
                            .subscribeOn(Schedulers.from(executorService))

                }
                .subscribe {
                    it.subscribe {
                        it.forEachIndexed { index, userData ->
                            //                                        LeoLog.i(tag, "index=$index, login=${userData.login}")
                            remoteRepository.getUserDetailFromGithub(userData)
                                    .subscribe {
                                        it?.let {
//                                            LeoLog.i(tag, "index=$index, login=${userData.login}, name=${it.name}")
//                                            userData.name = it.name
                                        }
                                    }
                        }
                    }

                    reloadListData.postValue(true)
                }
                .apply {
                    compositeDisposable.add(this)
//                    executorService.shutdown()
                }

    }

    /**
     * Bookmark db에 저장 또는 삭제 처리 한다. bookmark 여부멩 따라 처리 된다.
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


    /**
     * LiveData+Room, Bookmark db에서 값이 변경 되면 자동 호출 된다.
     */
    fun getUserDataFromDb()  = remoteRepository.getUserDataFromDb()


    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }


}
