package ccom.leo.githubstars.ui.base

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.leo.githubstars.adapter.GithubAdapter
import com.leo.githubstars.callback.OnItemClickListener
import com.leo.githubstars.data.local.UserData
import com.leo.githubstars.ui.main.MainViewModel

open abstract class BaseTabFragment: BaseFragment() {

    protected val githubAdapter = GithubAdapter()
    protected var viewModel: MainViewModel?= null
    private var searchWord: String?= null

    override fun initClickListener() {
        githubAdapter.setOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(item: Object, view: View, position: Int) {
                hideKeyboard()
                viewModel?.run {
                    updateBookmark(item as UserData)
                }
            }
        })
    }

    protected fun hideKeyboard() {
        (activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).run {
            hideSoftInputFromWindow(activity!!.currentFocus?.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
        }
    }

    protected fun getSearchWord() = searchWord

    protected fun setSearchWord(searchWord: String?){
        this.searchWord = searchWord
        githubAdapter.setSearchWord(searchWord)
    }

}