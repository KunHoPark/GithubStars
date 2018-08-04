package com.leo.githubstars.ui.splash

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.net.Uri
import android.os.Bundle
import android.support.customtabs.CustomTabsIntent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ccom.leo.githubstars.ui.base.BaseFragment
import com.leo.githubstars.application.Constants
import com.leo.githubstars.databinding.SplashFragmentBinding
import com.leo.githubstars.di.scope.ActivityScoped
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

            this.loadAccessToken()

        }


        SplashFragment.onNewIntent
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    viewModel.requestAccessToken(Constants.GITHUB_CLIENT_ID, Constants.GITHUB_CLIENT_SECRET, it)
                }

    }


}
