package com.leo.githubstars.ui.splash


import android.net.Uri
import android.view.View
import androidx.browser.customtabs.CustomTabsIntent
import androidx.databinding.ObservableField
import com.leo.githubstars.R
import com.leo.githubstars.data.repository.AuthRepository
import com.leo.githubstars.ui.base.BaseViewModel
import com.leo.githubstars.util.Constants
import com.leo.githubstars.util.LeoSharedPreferences
import com.leo.githubstars.util.SupportOptional
import com.leo.githubstars.util.optionalOf
import io.reactivex.Single
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

/**
 * Splash화면의 ViewModel
 * @author LeoPark
 **/
class SplashViewModel
@Inject constructor(private val authRepository: AuthRepository, private val leoSharedPreferences: LeoSharedPreferences) : BaseViewModel() {
    internal val tag = this.javaClass.simpleName

    val isLoading = ObservableField<Boolean>(false)
    val accessToken: PublishSubject<SupportOptional<String>> = PublishSubject.create()

    fun onClickListener(view: View) {
        when(view.id) {
            R.id.btnSignIn -> {
                val authUri = Uri.Builder().scheme("https").authority("github.com")
                    .appendPath("login")
                    .appendPath("oauth")
                    .appendPath("authorize")
                    .appendQueryParameter("client_id", Constants.GITHUB_CLIENT_ID)
                    .build()


                val intent = CustomTabsIntent.Builder().build()
                intent.launchUrl(view.context as SplashActivity, authUri)
            }
        }
    }

    /**
     * token 정보를 local에 가지고 있는지 확인. 없으면 무시, 있으면 main 화면으로 넘어 가기 위해 subject를 호출 한다.
     */
    fun loadAccessToken(): Disposable
            = Single.fromCallable { optionalOf(leoSharedPreferences.getString(Constants.PREF_ACTION_KEY_AUTH_TOKEN)) }
            .subscribeOn(Schedulers.io())
            .subscribe(Consumer<SupportOptional<String>> {
                accessToken.onNext(it)
            }).apply { viewDisposables.add(this) }

    /**
     * Token 정보를 가져 오는 api. Github 로긘 후 code 값을 받아 오면 처리 된다. code 값은 onNewIntent를 통해 받아 온다.
     */
    fun requestAccessToken(clientId: String, clientSecret: String, code: String): Disposable =
        authRepository.getAccessToken(clientId, clientSecret, code)
            .map { it.accessToken }
            .doOnSubscribe { isLoading.set(true) }
            .doOnTerminate { isLoading.set(false) }
            .subscribe(
                { token ->
                    leoSharedPreferences.setString(
                        Constants.PREF_ACTION_KEY_AUTH_TOKEN,
                        token
                    )
                    accessToken.onNext(optionalOf(token))
                },
                {
                    message.onNext(it.message ?: "Unexpected error")
                }
            ).apply { viewDisposables.add(this) }
}
