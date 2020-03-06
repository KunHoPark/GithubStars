package com.leo.githubstars.util

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.leo.githubstars.R
import com.leo.githubstars.application.MyGithubStarsApp
import java.util.*

/**
 * Glide lib.
 * @author LeoPark
 **/
object GlideUtil {

    /**
     * 이미지 로딩. placeholder/error는 배경 색으로 처리 해주고 있다.
     */
    fun loadImage(context: Context, imageUrl: String?, imageView: ImageView) {
        Glide.with(context)
            .load(imageUrl)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .placeholder(getRandomPlaceHolderDrawable())
            .error(getRandomPlaceHolderDrawable())
            .into(imageView)
    }

    /**
     * Glide의 placeholder/error 를 처리 하기 위함. 단순 색으로 처리.
     */
    private fun getRandomPlaceHolderDrawable() : Drawable {
        val bgColors : Array<Int> = arrayOf(R.color.subgray, R.color.random_1, R.color.random_2)
        return ColorDrawable(ContextCompat.getColor(MyGithubStarsApp.context, bgColors[Random().nextInt(bgColors.size)]))
    }
}