package com.leo.githubstars.ui.detail

import android.content.Intent
import android.os.Bundle
import com.leo.githubstars.R
import com.leo.githubstars.event.OnActivityListener
import com.leo.githubstars.ui.base.BaseActivity
import dagger.Lazy
import javax.inject.Inject

/**
 * Detail화면 Activity
 * Detail화면에 attached된 Fragment들을 생성한다.
 * Github의 auth token 값을 가져온다.
 * @author LeoPark
 **/
class DetailActivity : BaseActivity() {

    @Inject
    lateinit var detailFragmentProvider: Lazy<DetailFragment>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.detail_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, detailFragmentProvider.get())
                    .commitNow()
        }
    }

    override var onActivityListener = object: OnActivityListener {
        override fun onIntent(): Intent {
            return intent
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right)
    }

}
