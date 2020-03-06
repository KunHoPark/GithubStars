package com.leo.githubstars.extension

import android.content.Context
import android.graphics.Rect
import android.graphics.Typeface
import android.graphics.drawable.AnimationDrawable
import android.os.Build
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.ViewCompat
import androidx.core.view.ViewPropertyAnimatorListener
import androidx.databinding.BindingAdapter

/**
 *  make view gone
 */
fun View.toGone() {
    this.visibility = View.GONE
}

fun View.toVisible() {
    this.visibility = View.VISIBLE
}

/**
 *  make view invisible
 */
fun View.toInvisible() {
    this.visibility = View.INVISIBLE

}

/**
 *
 */
fun View.showFromTop(duration: Long) {
    clearAnimation()
    visibility = View.VISIBLE
    translationY = (-height).toFloat()
    ViewCompat.animate(this)
        .translationY(0f)
        .setDuration(duration)
        .start()
}

/**
 * 소프트 키보드를 띄운다
 */
fun View?.showKeyboardImplicit() {
    this?.let { view ->
        view.requestFocus()
        (view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).showSoftInput(
            view,
            InputMethodManager.SHOW_IMPLICIT
        )
    }
}

/**
 * 소프트 키보드를 숨긴다
 */
fun View?.hideKeyboard() {
    this?.let { view ->
        (view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(
            view.windowToken,
            0
        )
    }
}

fun View.isKeyboardShow(): Boolean {
    val softKeyboardHeight = 100
    val r = Rect()
    this.getWindowVisibleDisplayFrame(r)
    val dm = this.resources.displayMetrics
    val heightDiff = this.bottom - r.bottom
    return heightDiff > softKeyboardHeight * dm.density
}



