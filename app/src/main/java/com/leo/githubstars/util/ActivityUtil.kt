package com.leo.githubstars.util

import android.content.Context
import android.content.Intent
import com.leo.githubstars.ui.main.MainActivity

object ActivityUtil {

    /**
     *  Main
     */
    fun startMainActivity(context: Context) {
        Intent(context, MainActivity::class.java).run {
            context?.startActivity(this)
        }
    }

}