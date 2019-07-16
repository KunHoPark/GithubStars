package com.leo.githubstars.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.tabs.TabLayout
import com.leo.githubstars.adapter.GithubTabPagerAdapter
import com.leo.githubstars.databinding.MainFragmentBinding
import com.leo.githubstars.di.scope.ActivityScoped
import com.leo.githubstars.ui.base.BaseFragment
import com.leo.githubstars.ui.main.MainFragment.Variable.TAB_FRAGMENT_ALL
import com.leo.githubstars.ui.main.MainFragment.Variable.TAB_ITEMS
import com.leo.githubstars.util.LeoLog
import kotlinx.android.synthetic.main.main_fragment.*
import javax.inject.Inject

// ViewFactory 타입의 의존성은 FragmentModule이 없으므로 부모인 ActivitySubComponent를 검색하여 MainModule에서 생성한다.
@ActivityScoped
class MainFragment @Inject constructor() : BaseFragment() {
    internal val tag = this.javaClass.simpleName

    private lateinit var viewModel: MainViewModel
    private lateinit var viewDataBinding: MainFragmentBinding
    @Inject lateinit var viewModelFactory: MainViewModelFactory

    object Variable {
        const val TAB_ITEMS = 2
        const val TAB_FRAGMENT_ALL: Int = 0
        const val TAB_FRAGMENT_BTC: Int = 1
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        viewDataBinding = MainFragmentBinding.inflate(inflater, container, false)

        return viewDataBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory)[MainViewModel::class.java]

        viewDataBinding.run {
            this.mainViewModel = viewModel
            setLifecycleOwner(activity)
        }

        subscribe()
        initViewPager()

    }

    private fun initViewPager() {
        viewPagerAccounts.adapter = GithubTabPagerAdapter(childFragmentManager)
        tabLayoutAccounts.apply {
            addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabReselected(tab: TabLayout.Tab?) {
                    LeoLog.i("MainFragment", "onTabReselected")
                }
                override fun onTabUnselected(tab: TabLayout.Tab?) {
                    LeoLog.i("MainFragment","onTabUnselected")
                }
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    viewPagerAccounts?.currentItem = tab!!.position
                    LeoLog.i("MainFragment","onTabSelected")
                }
            })
        }
        tabLayoutAccounts.setupWithViewPager(viewPagerAccounts)
        viewPagerAccounts.offscreenPageLimit = TAB_ITEMS
        viewPagerAccounts.setCurrentItem(TAB_FRAGMENT_ALL, true)
        tabLayoutAccounts.postDelayed({

        },500)

    }

    override fun initClickListener() {
    }

    override fun subscribe() {
        with(viewModel) {
            super.subScribeMessage(this.message)
        }

    }
}
