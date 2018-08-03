package ccom.leo.githubstars.ui.base

import android.content.Context
import android.view.inputmethod.InputMethodManager

open abstract class BaseTabFragment: BaseFragment() {

    protected fun hideKeyboard() {
        (activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).run {
            hideSoftInputFromWindow(activity!!.currentFocus?.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
        }
    }

}