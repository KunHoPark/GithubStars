package com.leo.githubstars.ui.base

import android.os.Build
import android.os.Bundle
import android.widget.Toast
import com.leo.githubstars.R
import com.leo.githubstars.event.OnActivityListener
import com.leo.githubstars.extension.plusAssign
import com.leo.githubstars.util.AutoClearedDisposable
import dagger.android.support.DaggerAppCompatActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit

/**
 * Activity의 Base class.
 * @author LeoPark
 **/
open abstract class BaseActivity: DaggerAppCompatActivity() {

    internal var viewDisposables =  AutoClearedDisposable(lifecycleOwner = this, alwaysClearOnStop = false)
    protected val backButtonClickSource = PublishSubject.create<Boolean>()!!
    open val onActivityListener: OnActivityListener? = null

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        lifecycle += viewDisposables
    }

    override fun onResume() {
        super.onResume()
        //2회 back key 호출 하면 종료
        viewDisposables.add(backButtonClickSource
                .debounce(100, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext { showToast(getString(R.string.common_app_exit_msg)) }
                .timeInterval(TimeUnit.MILLISECONDS)
                .skip(1)
                .filter { interval -> interval.time() < 2000 }
                .subscribe {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        finishAffinity()
                    }else{
                        finish()
                    }

                })
    }

    private fun showToast(title: String) {
        Toast.makeText(this, title, Toast.LENGTH_SHORT).show()
    }


}