package com.leo.githubstars.ui.splash

import android.os.Bundle
import ccom.leo.githubstars.ui.base.BaseActivity
import com.leo.githubstars.R
import dagger.Lazy
import javax.inject.Inject

class SplashActivity : BaseActivity() {

    @Inject
    lateinit var splashFragmentProvider: Lazy<SplashFragment>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, splashFragmentProvider.get())
                    .commitNow()
        }
    }

}
