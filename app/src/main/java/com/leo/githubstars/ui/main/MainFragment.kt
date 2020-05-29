package com.leo.githubstars.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.leo.githubstars.R
import com.leo.githubstars.data.local.UserData
import com.leo.githubstars.databinding.MainFragmentBinding
import com.leo.githubstars.di.scope.ActivityScoped
import com.leo.githubstars.ui.base.BaseFragment
import com.leo.githubstars.ui.detail.DetailActivity
import javax.inject.Inject

/**
 * Main화면의 MainFragment.
 * TabLayout을 사용하여 TabFragment(GithubTabFragment, BookmarkTabFragment)를 추가해준다.
 * ViewFactory 타입의 의존성은 FragmentModule이 없으므로 부모인 ActivitySubComponent를 검색하여 MainModule에서 생성한다.
 * @author LeoPark
 **/
@ActivityScoped
class MainFragment @Inject constructor() : BaseFragment() {
    internal val tag = this.javaClass.simpleName

    private lateinit var viewModel: MainViewModel
    private lateinit var viewDataBinding: MainFragmentBinding
    @Inject lateinit var viewModelFactory: MainViewModelFactory

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        viewDataBinding = MainFragmentBinding.inflate(inflater, container, false)

        return viewDataBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)

        viewDataBinding.also {
            it.viewModel = viewModel
            it.activity = (activity as MainActivity)
            it.childFragmentManager = childFragmentManager
            it.lifecycleOwner = activity
        }

        subscribe()

    }

    override fun subscribe() {
        viewModel?.run {
            startDetailActivityLiveData.observe(this@MainFragment, Observer {
                Intent(activity, DetailActivity::class.java).run {
                    activity?.let {
                        it.startActivity(this)
                        it.overridePendingTransition(
                            R.anim.anim_slide_in_right,
                            R.anim.anim_slide_out_left
                        )
                    }
                }
            })
        }

    }
}
