package com.leo.githubstars.ui.main.tab

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ccom.leo.githubstars.ui.base.BaseTabFragment
import com.leo.githubstars.data.local.UserData
import com.leo.githubstars.databinding.BookmarkTabFragmentBinding
import com.leo.githubstars.di.scope.ActivityScoped
import com.leo.githubstars.ui.main.MainViewModel
import com.leo.githubstars.ui.main.MainViewModelFactory
import com.leo.githubstars.util.LeoLog
import kotlinx.android.synthetic.main.view_searchview_layout.*
import javax.inject.Inject

/**
 * BookmarkTabFragment
 * 로컬DB를 통해서 이름 검색에 대한 뷰 페이지
 * @author KunHoPark
 * @since 2018. 8. 2. PM 20:11
 **/
@ActivityScoped
class BookmarkTabFragment @Inject constructor() : BaseTabFragment() {
    internal val tag = this.javaClass.simpleName

    private lateinit var viewDataBinding: BookmarkTabFragmentBinding

    @Inject lateinit var viewModelFactory: MainViewModelFactory

    companion object {
        fun newInstance(): BookmarkTabFragment = BookmarkTabFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewDataBinding = BookmarkTabFragmentBinding.inflate(inflater, container, false)
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
            }

            this.mainViewModel = viewModel
            setLifecycleOwner(activity)
        }

        subscribe()
        loadData()
    }

    private fun loadData() {
        viewModel?.run {
            getSearchWord()?.let {
                this.loadSearchDataFromDb(it)
                        .observe(this@BookmarkTabFragment, Observer<List<UserData>> {
                            it?.let {
                                githubAdapter.addItems(it)
                            }
                        })

            }
        }
    }

    override fun initClickListener() {
        super.initClickListener()

        // 검색 필드 리스너.
        svInput.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {                   // 소프트 키보드의 검색 버튼.
                LeoLog.i(tag, "setOnQueryTextListener query= $query")
                query?.let {
                    setSearchWord(it)
                    loadData()
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {                 // 입력 필드의 값이 변경 되면 호출 됨.
                LeoLog.i(tag, "onQueryTextChange newText= $newText")
                viewModel?.run {
                    newText?.let {
                        setSearchWord(it)
                        loadData()
                    }
                }
                return true
            }

        })
    }

    /**
     * ViewModel로 부터 전달 되는 이벤트 들을 관리 한다. ex) observe, liveData 등
     */
    override fun subscribe() {

        viewModel?.run {
            super.subScribeMessage(this.message)

            // Bookmark db에 등록된 유저 정보가 변경 되었을때.
            getUserDataFromDb().observe(this@BookmarkTabFragment, Observer<List<UserData>> {
                it?.let {
                    getSearchWord()?.let {
                        loadData()
                    }?:githubAdapter.addItems(it)
                }

            })

        }

    }
}