package com.leo.githubstars.ui.splash


import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.leo.githubstars.R
import com.leo.githubstars.application.MyGithubStarsApp.Companion.applicationContext
import com.leo.githubstars.application.MyGithubStarsApp.Companion.resources
import com.leo.githubstars.data.repository.RemoteRepository
import com.leo.githubstars.util.LeoSharedPreferences
import com.leo.githubstars.util.SupportOptional
import com.leo.githubstars.util.optionalOf
import io.reactivex.Single
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

class SplashViewModel
@Inject constructor(private val remoteRepository: RemoteRepository) : ViewModel() {
    internal val tag = this.javaClass.simpleName

    var isLoadingSuccess: MutableLiveData<Boolean> = MutableLiveData()
    var isAccessToken: MutableLiveData<Boolean> = MutableLiveData()
    val accessToken: BehaviorSubject<SupportOptional<String>> = BehaviorSubject.create()
    val message: PublishSubject<String> = PublishSubject.create()

    fun loadAccessToken(): Disposable
            = Single.fromCallable { optionalOf( LeoSharedPreferences(applicationContext()).getString(resources().getString(R.string.pref_action_key_auth_token) )) }
            .subscribeOn(Schedulers.io())
            .subscribe(Consumer<SupportOptional<String>> {
                accessToken.onNext(it)
            })

    fun requestAccessToken(clientId: String, clientSecret: String, code: String): Disposable
            = remoteRepository.getAccessToken(clientId, clientSecret, code)
            .map { it.accessToken }
            .doOnSubscribe { isLoadingSuccess.postValue(true) }
            .doOnTerminate { isLoadingSuccess.postValue(false) }
            .subscribe({ token ->
                LeoSharedPreferences(applicationContext()).setString(resources().getString(R.string.pref_action_key_auth_token), token)
                accessToken.onNext(optionalOf(token))
            }) {
                message.onNext(it.message ?: "Unexpected error")
            }
}
