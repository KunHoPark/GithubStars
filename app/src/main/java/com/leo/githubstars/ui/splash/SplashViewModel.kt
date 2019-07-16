package com.leo.githubstars.ui.splash


import androidx.lifecycle.MutableLiveData
import com.leo.githubstars.R
import com.leo.githubstars.application.MyGithubStarsApp.Companion.applicationContext
import com.leo.githubstars.application.MyGithubStarsApp.Companion.resources
import com.leo.githubstars.data.repository.AuthRepository
import com.leo.githubstars.ui.base.BaseViewModel
import com.leo.githubstars.util.LeoSharedPreferences
import com.leo.githubstars.util.SupportOptional
import com.leo.githubstars.util.optionalOf
import io.reactivex.Single
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

class SplashViewModel
@Inject constructor(private val authRepository: AuthRepository) : BaseViewModel() {
    internal val tag = this.javaClass.simpleName

    var isLoadingSuccess: MutableLiveData<Boolean> = MutableLiveData()
    val accessToken: PublishSubject<SupportOptional<String>> = PublishSubject.create()

    /**
     * token 정보를 local에 가지고 있는지 확인. 없으면 무시, 있으면 main 화면으로 넘어 가기 위해 subject를 호출 한다.
     */
    fun loadAccessToken(): Disposable
            = Single.fromCallable { optionalOf( LeoSharedPreferences(applicationContext()).getString(resources().getString(R.string.pref_action_key_auth_token) )) }
            .subscribeOn(Schedulers.io())
            .subscribe(Consumer<SupportOptional<String>> {
                accessToken.onNext(it)
            })

    /**
     * Token 정보를 가져 오는 api. Github 로긘 후 code 값을 받아 오면 처리 된다. code 값은 onNewIntent를 통해 받아 온다.
     */
    fun requestAccessToken(clientId: String, clientSecret: String, code: String): Disposable
            = authRepository.getAccessToken(clientId, clientSecret, code)
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
