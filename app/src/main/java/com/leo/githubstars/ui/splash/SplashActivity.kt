package com.leo.githubstars.ui.splash

import android.content.Intent
import android.os.Bundle
import com.leo.githubstars.ui.base.BaseActivity
import com.leo.githubstars.R
import com.leo.githubstars.event.OnActivityListener
import dagger.Lazy
import javax.inject.Inject

/**
 * Splash화면 Activity
 * Splash화면에 attached된 Fragment들을 생성한다.
 * Github의 auth token 값을 가져온다.
 * @author LeoPark
 **/
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

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        splashFragmentProvider.get().onNewIntent(intent)
    }
}
