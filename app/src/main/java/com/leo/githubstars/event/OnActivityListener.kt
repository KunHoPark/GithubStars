package gov.laos.laototo.event

import android.content.Intent

interface OnActivityListener {
    fun onIntent(): Intent
    fun onStartFragment(fragment: Any)
    fun onTopMenu(resLayoutId: Int, resStringId: Int, isShow: Boolean)
}