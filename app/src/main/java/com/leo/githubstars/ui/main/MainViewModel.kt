package com.leo.githubstars.ui.main


import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.databinding.ObservableArrayList
import com.leo.githubstars.application.Constants
import com.leo.githubstars.data.local.UserData
import com.leo.githubstars.data.repository.RemoteRepository
import com.leo.githubstars.util.LeoLog
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class MainViewModel
@Inject constructor(private val remoteRepository: RemoteRepository) : ViewModel() {
    internal val tag = this.javaClass.simpleName

    private var compositeDisposable = CompositeDisposable()
    var isLoadingSuccess: MutableLiveData<Boolean> = MutableLiveData()

    var userData= ObservableArrayList<UserData>()
    private var totalCount = 0
    private var currentPage = 0

    fun loadGithub(searchValue: String) {
        if (totalCount > currentPage*Constants.PERPAGE || currentPage==0){

            remoteRepository.loadSearchData(searchValue, ++currentPage, Constants.PERPAGE)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            {
                                it?.let {
                                    totalCount = it.totalCount.toInt()
                                    userData.addAll(it.items)
                                }
                            },
                            {
                                LeoLog.e(it.localizedMessage)
                            },
                            {}
                    )
        }

    }

    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }
}
