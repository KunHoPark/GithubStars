package com.leo.githubstars.ui.main.tab

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ccom.leo.githubstars.ui.base.BaseTabFragment
import com.jakewharton.rxbinding2.widget.RxTextView
import com.leo.githubstars.adapter.GithubAdapter
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
 * @author LeoPark
 **/
@ActivityScoped
class BookmarkTabFragment @Inject constructor() : BaseTabFragment() {
    internal val tag = this.javaClass.simpleName

    private lateinit var viewDataBinding: BookmarkTabFragmentBinding

    @Inject lateinit var viewModelFactory: MainViewModelFactory


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewDataBinding = BookmarkTabFragmentBinding.inflate(inflater, container, false)
        return viewDataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)
        viewDataBinding.also {
            it.viewModel = viewModel
            it.lifecycleOwner = activity

            recyclerViewBookmark?.apply {
                setHasFixedSize(true)
                this.adapter = GithubAdapter()
                layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
            }

        }

        subscribe()
    }

    /**
     * ViewModel로 부터 전달 되는 이벤트 들을 관리 한다. ex) observe, liveData 등
     */
    @SuppressLint("FragmentLiveDataObserve")
    override fun subscribe() {
        viewModel?.run {
            // Bookmark db에 등록된 유저 정보가 변경 되었을때.
//            getUserData.observe(this@BookmarkTabFragment, Observer<List<UserData>> {
//                it?.let {
//
//                    //Bookmark tab list의 데이타 변경.
//                    bookmarkUserData.clear()
//                    bookmarkUserData.addAll(it)
//                }
//            })
        }
    }
}