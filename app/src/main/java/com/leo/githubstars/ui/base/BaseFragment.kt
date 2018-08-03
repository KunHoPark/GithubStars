package ccom.leo.githubstars.ui.base

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import com.leo.githubstars.extension.plusAssign
import com.leo.githubstars.util.AutoClearedDisposable
import dagger.android.support.DaggerFragment

open abstract class BaseFragment: DaggerFragment() {

    lateinit var viewDisposables : AutoClearedDisposable

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewDisposables = AutoClearedDisposable(lifecycleOwner = activity as AppCompatActivity, alwaysClearOnStop = false)
        lifecycle += viewDisposables
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onResume() {
        super.onResume()
        initClickListener()
    }

    abstract fun initClickListener()

    protected fun showToast(message: String) {
        Toast.makeText(activity!!.applicationContext, message, Toast.LENGTH_SHORT).show()
    }

}