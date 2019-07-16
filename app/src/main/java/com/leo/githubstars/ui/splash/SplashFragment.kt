package com.leo.githubstars.ui.splash

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.browser.customtabs.CustomTabsIntent
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.leo.githubstars.application.Constants
import com.leo.githubstars.databinding.SplashFragmentBinding
import com.leo.githubstars.di.scope.ActivityScoped
import com.leo.githubstars.ui.base.BaseFragment
import com.leo.githubstars.util.ActivityUtil
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.splash_fragment.*
import javax.inject.Inject

@ActivityScoped
class SplashFragment @Inject constructor() : BaseFragment() {
    internal val tag = this.javaClass.simpleName

    lateinit var viewModel: SplashViewModel
    private lateinit var viewDataBinding: SplashFragmentBinding
    @Inject lateinit var viewModelFactory: SplashViewModelFactory

    companion object {
        var code: String?= null
        val onNewIntent: PublishSubject<String> = PublishSubject.create()

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        viewDataBinding = SplashFragmentBinding.inflate(inflater, container, false)


        return viewDataBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory)[SplashViewModel::class.java]

        viewDataBinding.run {
            this.viewModel = viewModel
            setLifecycleOwner(activity)
        }

        subscribe()
        loadData()
    }

    private fun loadData() {
    }

    override fun initClickListener() {
        // Token 정보를 가져 오기.
        btnSignIn.setOnClickListener {
            val authUri = Uri.Builder().scheme("https").authority("github.com")
                    .appendPath("login")
                    .appendPath("oauth")
                    .appendPath("authorize")
                    .appendQueryParameter("client_id", Constants.GITHUB_CLIENT_ID)
                    .build()

            val intent = CustomTabsIntent.Builder().build()
            intent.launchUrl(activity, authUri)
        }
    }

    /**
     * ViewModel로 부터 전달 되는 이벤트 들을 관리 한다. ex) observe, liveData 등
     */
    override fun subscribe() {
        with(viewModel) {
            super.subScribeMessage(this.message)

            this.isLoadingSuccess.observe(this@SplashFragment, Observer<Boolean> {

                if (it == true) {
                    dataLoading.visibility = View.VISIBLE
                } else {
                    dataLoading.visibility = View.GONE
                }
            })

            // loadAccessToken()를 통해 token 정보가 있다고 전달 받음. Main화면 으로 전환.
            this.accessToken
                    .filter { !it.isEmpty }
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe {
                        ActivityUtil.startMainActivity(activity!!)
                        activity!!.finish()
                    }
                    .apply {
                        viewDisposables.add(this)
                    }

            // Token 정보를 보유 하고 있는지 확인.
            this.loadAccessToken()

        }

        // onNewIntent를 처리 하기 위함. 로그인 버튼을 누른 후 발생됨. Github site로 부터 code 값을 가져 온다. Intent-filter의 scheme는 manifest에 설정 되어 있다.
        SplashFragment.onNewIntent
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    viewModel.requestAccessToken(Constants.GITHUB_CLIENT_ID, Constants.GITHUB_CLIENT_SECRET, it)
                }

    }


}
