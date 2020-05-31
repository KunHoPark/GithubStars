package com.leo.githubstars.ui.detail


import androidx.databinding.ObservableField
import com.leo.githubstars.data.local.UserData
import com.leo.githubstars.data.repository.RemoteRepository
import com.leo.githubstars.ui.base.BaseViewModel
import com.leo.githubstars.util.LeoLog
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

/**
 * Detail화면의 ViewModel
 * @author LeoPark
 **/
class DetailViewModel
@Inject constructor(private val remoteRepository: RemoteRepository) : BaseViewModel() {
    internal val tag = this.javaClass.simpleName

    val userData = ObservableField<UserData>()

    fun loadData(reqData: UserData) {
        Observable.just(userData.set(reqData))
            .flatMap {
                remoteRepository.getUserDetailFromGithub(reqData).toObservable()
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map {
                it.also { userData ->
                    if (reqData.isBookmark) {
                        userData.isBookmark = true
                        remoteRepository.updateUserDataToDb(userData)
                    }
                }
            }
            .subscribe(
                {
                    userData.set(it)
                },
                {
                    LeoLog.e(tag, it.localizedMessage)
                }
            ).apply { viewDisposables.add(this) }

    }
}
