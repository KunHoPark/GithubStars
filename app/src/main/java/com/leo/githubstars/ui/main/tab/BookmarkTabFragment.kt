package com.leo.githubstars.ui.main.tab

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import ccom.leo.githubstars.ui.base.BaseTabFragment
import com.jakewharton.rxbinding2.widget.RxTextView
import com.leo.githubstars.data.local.UserData
import com.leo.githubstars.databinding.BookmarkTabFragmentBinding
import com.leo.githubstars.di.scope.ActivityScoped
import com.leo.githubstars.ui.main.MainViewModel
import com.leo.githubstars.ui.main.MainViewModelFactory
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.bookmark_tab_fragment.*
import java.util.concurrent.TimeUnit
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

            recyclerViewBookmark?.apply {
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
        RxTextView.textChangeEvents(svBookmarkInput)
                .subscribeOn(Schedulers.io())
                .debounce(300, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe{
                    val words = it.text().toString()
                    viewModel?.run {
                        words?.let {
                            setSearchWord(it)
                            loadData()
                        }
                    }
                }.apply {
                    disposables.add(this)
                }

        // Enter key에 대한 처리.
        svBookmarkInput.setOnEditorActionListener { textView, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH){
                val text = textView.text.toString()
                text?.let {
                    setSearchWord(it)
                    loadData()
                }
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }
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