package com.leo.githubstars.adapter

import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.leo.githubstars.R
import com.leo.githubstars.data.local.UserData
import com.leo.githubstars.extension.isEnglish
import com.leo.githubstars.extension.toResColor
import com.leo.githubstars.util.GlideUtil

object AdapterBindings {

    /**
     * 리스트의 이미지 로
     */
    @BindingAdapter("loadImage")
    @JvmStatic
    fun setLoadImage(imageView: ImageView, url: String) {
        GlideUtil.loadImage(imageView.context, url, imageView)
    }

    /**
     * Bookmark 처리
     */
    @BindingAdapter("isBookmark")
    @JvmStatic
    fun setBookmark(view: ImageView, isBookmark: Boolean) {
        when(isBookmark){
            true -> {
                view.setImageResource(R.drawable.btn_done_nor)
            }
            else -> {
                view.setImageResource(R.drawable.btn_done_pre)
            }
        }
    }

    /**
     * 검색어에 대한 글자 HighLight.
     */
    @BindingAdapter( "drawHighLight_title", "drawHighLight_desc", "searchWord", "userData")
    @JvmStatic
    fun setDrawHighLight(view: View, tvLogin: TextView, tvDesc: TextView, searchWord: String, item: UserData){
        var keyword = searchWord
        var login: String
        var name: String

        if (keyword.isEnglish()) {
            keyword = keyword.toLowerCase() ?: ""
            login = item.login?.toLowerCase()
            name = item.url?.toLowerCase()
        } else {
            login = item.login
            name = item.url
        }

        if (name.isNullOrEmpty()) name = ""

        var sbLogin = SpannableStringBuilder(item.login)        // original
        val sbName = if (item.url.isNullOrEmpty()) {
            SpannableStringBuilder(name)
        } else {
            SpannableStringBuilder(item.url)           // original
        }

        if (keyword.isNotEmpty()) {

            // login에 대한 highlight.
            var indexLogin: Int = login?.indexOf(keyword)
            while (indexLogin >= 0) {
                sbLogin.setSpan(
                    ForegroundColorSpan(R.color.keycolor.toResColor), indexLogin,
                    indexLogin + keyword!!.length, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
                indexLogin = login?.indexOf(keyword, indexLogin + 1) ?: 0
            }

            // name에 대한 highlight.
            var indexName: Int = name?.indexOf(keyword)



            while (indexName >= 0) {
                sbName.setSpan(
                    ForegroundColorSpan(R.color.keycolor.toResColor), indexName,
                    indexName + keyword.length, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
                indexName = name?.indexOf(keyword, indexName + 1) ?: 0
            }
        }


        tvLogin.text = sbLogin
        tvDesc.text = sbName
    }

}