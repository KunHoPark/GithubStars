package com.leo.githubstars.ui.splash

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ccom.leo.githubstars.ui.base.BaseFragment
import com.leo.githubstars.databinding.SplashFragmentBinding
import com.leo.githubstars.di.scope.ActivityScoped
import com.leo.githubstars.util.ActivityUtil
import kotlinx.android.synthetic.main.splash_fragment.*
import javax.inject.Inject

@ActivityScoped
class SplashFragment @Inject constructor() : BaseFragment() {
    internal val tag = this.javaClass.simpleName

    private lateinit var viewModel: SplashViewModel
    private lateinit var viewDataBinding: SplashFragmentBinding
    @Inject lateinit var viewModelFactory: SplashViewModelFactory

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

        subscribeLiveData()
        loadData()

        ActivityUtil.startMainActivity(activity!!)
        activity!!.finish()
    }

    private fun loadData() {
        viewModel.loadCollections()
    }

    override fun initClickListener() {

    }

    private fun subscribeLiveData() {
        with(viewModel){
            isLoadingSuccess.observe(this@SplashFragment, Observer<Boolean> {
                dataLoading.visibility = View.GONE
                if(it == true){
                    ActivityUtil.startMainActivity(activity!!)
                    activity!!.finish()
                }else{
                    showToast("서버 연결에 실패 하였습니다. 네트워크 상태를 확인해 주세요.")
                    ActivityUtil.startMainActivity(activity!!)
                    activity!!.finish()
                }
            })
        }
    }



}
