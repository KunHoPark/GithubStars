package com.leo.githubstars.util

import android.view.View
import android.view.ViewStub
import androidx.databinding.BindingAdapter

/**
 * parkkh on 2019-09-20
 *
 */
object ViewBindingAdapter {

    @BindingAdapter("toVisibleOrGone")
    @JvmStatic
    fun toVisibleOrGone(view: View, isVisible: Boolean) {
        view.visibility = when (isVisible) {
            true -> View.VISIBLE
            else -> View.GONE
        }

    }

    @BindingAdapter("toVisibleOrInvisible")
    @JvmStatic
    fun toVisibleOrInvisible(view: View, isVisible: Boolean) {
        view.visibility = when (isVisible) {
            true -> View.VISIBLE
            else -> View.INVISIBLE
        }

    }



}