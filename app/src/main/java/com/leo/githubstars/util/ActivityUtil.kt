package com.leo.githubstars.util

import android.app.Activity
import android.content.Intent
import androidx.fragment.app.Fragment
import com.leo.githubstars.R
import com.leo.githubstars.data.local.UserData
import com.leo.githubstars.ui.detail.DetailActivity
import com.leo.githubstars.ui.main.MainActivity

/**
 * Activity간 화면 이동을 위해 사용 한다.
 * @author LeoPark
 **/
object ActivityUtil {

    fun startMainActivity(activity: Activity, fragment: Fragment?=null) {
        Intent(activity, MainActivity::class.java).run {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK
            fragment?.let {
                it.startActivity(this)
            } ?: activity.startActivity(this)
            activity.overridePendingTransition(
                R.anim.anim_slide_in_right,
                R.anim.anim_slide_out_left
            )
        }
    }

    fun startDetailActivity(activity: Activity, fragment: Fragment?=null, userData: UserData) {
        Intent(activity, DetailActivity::class.java).run {
            putExtra(Constants.INTENT_ACTION_KEY_USERDATA, userData)
            fragment?.let {
                it.startActivity(this)
            } ?: activity.startActivity(this)
            activity.overridePendingTransition(
                R.anim.anim_slide_in_right,
                R.anim.anim_slide_out_left
            )
        }
    }

}