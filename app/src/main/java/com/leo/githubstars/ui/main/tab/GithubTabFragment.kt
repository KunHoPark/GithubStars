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
import com.leo.githubstars.databinding.GithubTabFragmentBinding
import com.leo.githubstars.di.scope.ActivityScoped
import com.leo.githubstars.ui.main.MainViewModel
import com.leo.githubstars.ui.main.MainViewModelFactory
import com.leo.githubstars.util.InfiniteScrollListener
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.github_tab_fragment.*
import java.util.concurrent.TimeUnit
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

            recyclerViewGithub?.apply {
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

        // 검색 필드 리스너.
        RxTextView.textChangeEvents(svGithubInput)
                .subscribeOn(Schedulers.io())
                .debounce(400, TimeUnit.MILLISECONDS)
                .filter {
                    it.text().toString().length >= 2
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe{
                    val words = it.text().toString()
                    viewModel?.run {
                        words?.let {
                            setSearchWord(it)
                            loadSearchDataFromGithub(it, false)
                            scrollListener?.let {
                                it.previousTotal = 0
                            }
                        }
                    }
                }.apply {
                    disposables.add(this)
                }

        // Enter key에 대한 처리.
        svGithubInput.setOnEditorActionListener { textView, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH){
                val text = textView.text.toString()
                text?.let {
                    setSearchWord(text)
                    loadSearchDataFromGithub(it, true)
                        scrollListener?.let {
                            it.previousTotal = 0
                        }
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
            getUserDataFromDb().observe(this@GithubTabFragment, Observer<List<UserData>> {
                it?.let {
                    this.mergeSearchDataAndBookmarkData(it)
                }
            })

            // 리스트 내용 업데이트.
            reloadListData.observe(this@GithubTabFragment, Observer<Boolean> {
                if (it == true) {
                    githubAdapter.notifyDataSetChanged()
                }
            })

        }
    }

}