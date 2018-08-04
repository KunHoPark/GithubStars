package com.leo.githubstars.ui.main.tab

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.graphics.Color
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import ccom.leo.githubstars.ui.base.BaseTabFragment
import com.leo.githubstars.data.local.UserData
import com.leo.githubstars.databinding.GithubTabFragmentBinding
import com.leo.githubstars.di.scope.ActivityScoped
import com.leo.githubstars.ui.main.MainViewModel
import com.leo.githubstars.ui.main.MainViewModelFactory
import com.leo.githubstars.util.InfiniteScrollListener
import com.leo.githubstars.util.LeoLog
import kotlinx.android.synthetic.main.view_searchview_layout.*
import javax.inject.Inject

/**
 * GithubTabFragment
 * 서버를 통해서 이름 검색에 대한 뷰 페이지
 * @author KunHoPark
 * @since 2018. 8. 2. PM 20:11
 **/
@ActivityScoped
class GithubTabFragment @Inject constructor() : BaseTabFragment() {
    internal val tag = this.javaClass.simpleName

    private lateinit var viewDataBinding: GithubTabFragmentBinding

    @Inject lateinit var viewModelFactory: MainViewModelFactory

    private var scrollListener: InfiniteScrollListener?= null

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
                this.adapter = githubAdapter
                layoutManager = gridLayoutManager
                scrollListener = InfiniteScrollListener({loadSearchDataFromGithub(getSearchWord(), false)}, gridLayoutManager)
                addOnScrollListener(scrollListener)
            }

            this.mainViewModel = viewModel
            setLifecycleOwner(activity)
        }

        //검색텍스트 흰색으로 변경
        val searchEditText = svInput.findViewById(android.support.v7.appcompat.R.id.search_src_text) as EditText
        searchEditText.setTextColor(Color.WHITE)

        subscribe()
    }

    private fun loadSearchDataFromGithub(searchWord: String?, isReload: Boolean) {
        viewModel?.run {
            searchWord?.let {
                this.loadSearchDataFromGithub(searchWord, isReload)
            }
        }
    }

    override fun initClickListener() {
        super.initClickListener()

        svInput.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                LeoLog.i(tag, "setOnQueryTextListener query= $query")
                    query?.let {
                        setSearchWord(it)
                        loadSearchDataFromGithub(it, true)
                        scrollListener?.let {
                            it.previousTotal = 0
                        }
                    }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                LeoLog.i(tag, "onQueryTextChange newText= $newText")
                viewModel?.run {
                    newText?.let {
                        setSearchWord(it)
                        loadSearchDataFromGithub(it, false)
                        scrollListener?.let {
                            it.previousTotal = 0
                        }
                    }
                }
                return true
            }

        })
    }

    override fun subscribe() {

        viewModel?.run {
            super.subScribeMessage(this.message)

            // Bookmark db에 등록된 유저 정보가 변경 되었을때.
            getUserDataFromDb().observe(this@GithubTabFragment, Observer<List<UserData>> {
                it?.let {
                    this.mergeSearchDataAndBookmarkData(it)
                }
            })

            // 리스트 내용 업데이트.
            reloadListData.observe(this@GithubTabFragment, Observer<Boolean> {
                if (it == true) {
                    githubAdapter?.let {
                        githubAdapter.notifyDataSetChanged()
                    }
                }
            })

        }
    }

}