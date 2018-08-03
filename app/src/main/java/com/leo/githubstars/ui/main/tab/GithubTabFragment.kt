package com.leo.githubstars.ui.main.tab

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ccom.leo.githubstars.ui.base.BaseFragment
import com.leo.githubstars.adapter.GithubAdapter
import com.leo.githubstars.databinding.GithubTabFragmentBinding
import com.leo.githubstars.di.scope.ActivityScoped
import com.leo.githubstars.ui.main.MainViewModel
import com.leo.githubstars.ui.main.MainViewModelFactory
import com.leo.githubstars.util.InfiniteScrollListener
import javax.inject.Inject

/**
 * GithubTabFragment
 * 서버를 통해서 이름 검색에 대한 뷰 페이지
 * @author KunHoPark
 * @since 2018. 8. 2. PM 20:11
 **/
@ActivityScoped
class GithubTabFragment @Inject constructor() : BaseFragment() {

    private var viewModel: MainViewModel?= null
    private lateinit var viewDataBinding: GithubTabFragmentBinding

    @Inject lateinit var viewModelFactory: MainViewModelFactory
    private val gettyImageAdapter = GithubAdapter()


    companion object {
        fun newInstance(): GithubTabFragment = GithubTabFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewDataBinding = GithubTabFragmentBinding.inflate(inflater, container, false)
        return viewDataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProviders.of(this, viewModelFactory)[MainViewModel::class.java]
        with(viewDataBinding) {

            recyclerView?.apply {
                val gridLayoutManager = GridLayoutManager(activity, 1)
                gridLayoutManager.orientation = LinearLayoutManager.VERTICAL
                setHasFixedSize(true)
                adapter = gettyImageAdapter
                layoutManager = gridLayoutManager
                addOnScrollListener(InfiniteScrollListener({loadData()}, gridLayoutManager))
            }

            this.mainViewModel = viewModel
            setLifecycleOwner(activity)
        }

        loadData()
        observeLiveData()
    }

    private fun loadData() {
        viewModel?.let {
            it.loadGithub("kunho")
        }
    }

    override fun initClickListener() {
    }

    private fun observeLiveData() {
//        viewModel.getAllAccountEntities().observe(this, Observer<List<AccountEntity>> {
//            it?.run {
//                accountsTabAdapter.addItems(this)
//            }
//        })
    }
}