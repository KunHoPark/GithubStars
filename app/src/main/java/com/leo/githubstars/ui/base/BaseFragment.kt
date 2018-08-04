package ccom.leo.githubstars.ui.base

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import com.leo.githubstars.extension.plusAssign
import com.leo.githubstars.util.AutoClearedDisposable
import com.leo.githubstars.util.LeoLog
import dagger.android.support.DaggerFragment
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.subjects.PublishSubject

open abstract class BaseFragment: DaggerFragment() {
    internal val tag = this.javaClass.simpleName

    lateinit var disposables: AutoClearedDisposable
    lateinit var viewDisposables : AutoClearedDisposable

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        disposables = AutoClearedDisposable(activity as AppCompatActivity)
        viewDisposables = AutoClearedDisposable(lifecycleOwner = activity as AppCompatActivity, alwaysClearOnStop = false)
        lifecycle += disposables
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

    abstract fun subscribe()

    abstract fun initClickListener()

    protected fun subScribeMessage(message: PublishSubject<String>) {
        message
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    LeoLog.e(tag, it)
                }
                .apply {
                    disposables.add(this)
                }
    }

    protected fun showToast(message: String) {
        Toast.makeText(activity!!.applicationContext, message, Toast.LENGTH_SHORT).show()
    }

}