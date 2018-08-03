package com.leo.githubstars.ui.main


import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.databinding.ObservableArrayList
import com.leo.githubstars.application.Constants
import com.leo.githubstars.data.local.UserData
import com.leo.githubstars.data.repository.RemoteRepository
import com.leo.githubstars.util.LeoLog
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class MainViewModel
@Inject constructor(private val remoteRepository: RemoteRepository) : ViewModel() {
    internal val tag = this.javaClass.simpleName

    private var compositeDisposable = CompositeDisposable()
    var isLoadingSuccess: MutableLiveData<Boolean> = MutableLiveData()

    var userData= ObservableArrayList<UserData>()
    private var totalCount = 0
    private var currentPage = 0
    private var beforeSearchValue: String?= null

    fun loadSearchDataFromGithub(searchValue: String, isReload: Boolean) {
        LeoLog.i(tag, "loadSearchDataFromGithub searchValue=$searchValue")

        Flowable.just(searchValue)
                .subscribeOn(Schedulers.io())
                .filter {
                    searchValue.length>=3                                   //3 이상 일때만.
                }
                .debounce(300, TimeUnit.MILLISECONDS)               //정해진 안에 들어 오는 키는 무시하고 마지막에 입력된 키만 처리 한다.
                .map {
                    LeoLog.i(tag, "loadSearchDataFromGithub debounce searchValue=$searchValue")
                    beforeSearchValue?.let {                                // 이전에 입력 했던 키 값이 변경 되었으면 모든 값을 초기화 한다.
                        if (it != searchValue){
                            totalCount = 0
                            currentPage = 0
                            userData.removeAll(userData)
                        }
                    }
                    beforeSearchValue = searchValue
                    return@map it
                }
                .map {
                    if (isReload){
                        totalCount = 0
                        currentPage = 0
                        userData.removeAll(userData)
                    }
                }
                .filter {
                    totalCount > currentPage*Constants.PERPAGE || currentPage==0        //다음 페이지가 있는지를 확인 한다.
                }
                .flatMap {
                    remoteRepository.loadSearchDataFromGithub(searchValue, ++currentPage, Constants.PERPAGE)        //Github 서버를 통해 값을 가져 온다.
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {
                            it?.let {
                                totalCount = it.totalCount.toInt()
                                userData.addAll(it.items)
                                LeoLog.i(tag, "loadSearchDataFromGithub userData.size=${userData.size}")
                            }
                        },
                        {
                            LeoLog.e(it.localizedMessage)
                        },
                        {}
                )
                .apply {
                    compositeDisposable.add(this)
                }

//        beforeSearchValue?.let {
//            if (it != searchValue){
//                totalCount = 0
//                currentPage = 0
//            }
//            beforeSearchValue = searchValue
//        }
//
//        if (totalCount > currentPage*Constants.PERPAGE || currentPage==0){
//
//            remoteRepository.loadSearchDataFromGithub(searchValue, ++currentPage, Constants.PERPAGE)
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(
//                            {
//                                it?.let {
//                                    totalCount = it.totalCount.toInt()
//                                    userData.addAll(it.items)
//                                }
//                            },
//                            {
//                                LeoLog.e(it.localizedMessage)
//                            },
//                            {}
//                    )
//                    .apply {
//                        compositeDisposable.add(this)
//                    }
//        }

    }

    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }
}
