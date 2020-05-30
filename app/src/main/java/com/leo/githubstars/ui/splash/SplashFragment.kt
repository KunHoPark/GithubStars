package com.leo.githubstars.ui.splash

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.leo.githubstars.databinding.SplashFragmentBinding
import com.leo.githubstars.di.scope.ActivityScoped
import com.leo.githubstars.ui.base.BaseFragment
import com.leo.githubstars.util.ActivityUtil
import com.leo.githubstars.util.Constants
import io.reactivex.android.schedulers.AndroidSchedulers
import javax.inject.Inject

/**
 * Splash화면 Fragment.
 * @author LeoPark
 **/
@ActivityScoped
class SplashFragment @Inject constructor() : BaseFragment() {
    internal val tag = this.javaClass.simpleName

    lateinit var viewModel: SplashViewModel
    private lateinit var viewDataBinding: SplashFragmentBinding
    @Inject lateinit var viewModelFactory: SplashViewModelFactory

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        viewDataBinding = SplashFragmentBinding.inflate(inflater, container, false)


        return viewDataBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this, viewModelFactory).get(SplashViewModel::class.java)

        viewModel.viewDisposables = viewDisposables
        viewDataBinding.also {
            it.viewModel = viewModel
            it.lifecycleOwner = activity
        }

        subscribe()
        loadData()
    }

    /**
     * onNewIntent를 처리 하기 위함. 로그인 버튼을 누른 후 발생됨. Github site로 부터 code 값을 가져 온다. Intent-filter의 scheme는 manifest에 설정 되어 있다.
     */
    fun onNewIntent(intent: Intent) {
        intent.data?.getQueryParameter("code")?.let {
            viewModel.requestAccessToken(Constants.GITHUB_CLIENT_ID, Constants.GITHUB_CLIENT_SECRET, it)
        }
    }

    private fun loadData() {
        viewModel.loadAccessToken()
    }

    /**
     * ViewModel로 부터 전달 되는 이벤트 들을 관리 한다. ex) observe, liveData 등
     */
    override fun subscribe() {
        with(viewModel) {

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

    }


}
