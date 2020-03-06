package com.leo.githubstars.ui.main.tab

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ccom.leo.githubstars.ui.base.BaseTabFragment
import com.leo.githubstars.adapter.GithubAdapter
import com.leo.githubstars.data.local.UserData
import com.leo.githubstars.databinding.GithubTabFragmentBinding
import com.leo.githubstars.di.scope.ActivityScoped
import com.leo.githubstars.ui.main.MainViewModel
import com.leo.githubstars.ui.main.MainViewModelFactory
import com.leo.githubstars.util.InfiniteScrollListener
import kotlinx.android.synthetic.main.github_tab_fragment.*
import javax.inject.Inject



/**
 * GithubTabFragment
 * 서버를 통해서 이름 검색에 대한 뷰 페이지
 * @author LeoPark
 **/
@ActivityScoped
class GithubTabFragment @Inject constructor() : BaseTabFragment() {
    internal val tag = this.javaClass.simpleName

    private lateinit var viewDataBinding: GithubTabFragmentBinding

    @Inject lateinit var viewModelFactory: MainViewModelFactory



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewDataBinding = GithubTabFragmentBinding.inflate(inflater, container, false)
        return viewDataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)
        viewDataBinding.also {

            it.viewModel = viewModel
            it.lifecycleOwner = activity

            recyclerViewGithub?.apply {
                setHasFixedSize(true)
                this.adapter = GithubAdapter()
                val gridLayoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
                layoutManager = gridLayoutManager
                viewModel!!.scrollListener = InfiniteScrollListener({loadSearchDataFromGithub(false)}, gridLayoutManager)
                addOnScrollListener(viewModel!!.scrollListener!!)
            }
        }

        subscribe()
    }

    private fun loadSearchDataFromGithub(isReload: Boolean) {
        viewModel?.run {
            githubSearchWord.get()?.let {
                this.onGithubSearchMoreData(it, isReload)
            }
        }
    }

    /**
     * ViewModel로 부터 전달 되는 이벤트 들을 관리 한다. ex) observe, liveData 등
     */
    override fun subscribe() {

        viewModel?.run {

            // Bookmark db에 등록된 유저 정보가 변경 되었을때.
            getUserData.observe(this@GithubTabFragment, Observer<List<UserData>> {
                it?.let {
                    updateBookmarkData(it)
                }
            })
        }
    }

}