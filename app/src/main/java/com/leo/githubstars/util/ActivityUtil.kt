package com.leo.githubstars.util

import android.content.Context
import android.content.Intent
import com.leo.githubstars.ui.main.MainActivity

/**
 * Activity간 화면 이동을 위해 사용 한다.
 * @author LeoPark
 **/
object ActivityUtil {

    /**
     *  Main
     */
    fun startMainActivity(context: Context) {
        Intent(context, MainActivity::class.java).run {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context?.startActivity(this)
        }
    }

}