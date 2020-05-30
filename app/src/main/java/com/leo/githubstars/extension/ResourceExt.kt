package com.leo.githubstars.extension

import android.graphics.drawable.Drawable
import android.os.Build
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import com.leo.githubstars.application.MyGithubStarsApp



inline val Int.toResString: String get() = MyGithubStarsApp.context.resources.getString(this)

inline val Int.toDrawable: Drawable? get() = AppCompatResources.getDrawable(MyGithubStarsApp.context, this)

fun Int.toResColor(view: TextView) {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        view.setTextColor(MyGithubStarsApp.context.getColor(this))
    } else {
        view.setTextColor(ContextCompat.getColor(MyGithubStarsApp.context, this))
    }
}

inline val Int.toResColor: Int
    get() {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            MyGithubStarsApp.context.getColor(this)
        } else {
            ContextCompat.getColor(MyGithubStarsApp.context, this)
        }
    }

fun Int.toResStyle(view: TextView) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        view.setTextAppearance(this)
    } else {
        @Suppress("DEPRECATION")
        view.setTextAppearance(MyGithubStarsApp.context, this)
    }
}

fun Int.toResStyle(view: Button) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        view.setTextAppearance(this)
    } else {
        @Suppress("DEPRECATION")
        view.setTextAppearance(MyGithubStarsApp.context, this)
    }
}

inline val Int.toDimensionPixelOffset: Int get() = MyGithubStarsApp.context.resources.getDimensionPixelOffset(this)

