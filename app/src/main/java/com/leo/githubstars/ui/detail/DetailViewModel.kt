package com.leo.githubstars.ui.detail


import android.net.Uri
import android.view.View
import androidx.browser.customtabs.CustomTabsIntent
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.leo.githubstars.R
import com.leo.githubstars.application.MyGithubStarsApp
import com.leo.githubstars.data.repository.AuthRepository
import com.leo.githubstars.extension.toResString
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
import kotlinx.android.synthetic.main.splash_fragment.*
import javax.inject.Inject

/**
 * Detail화면의 ViewModel
 * @author LeoPark
 **/
class DetailViewModel
@Inject constructor(private val authRepository: AuthRepository) : BaseViewModel() {
    internal val tag = this.javaClass.simpleName

    val isLoading = ObservableField<Boolean>(false)

    init {
//        MyGithubStarsApp.appComponent.inject(this)
    }

    fun onClickListener(view: View) {
        when(view.id) {
        }
    }

}
