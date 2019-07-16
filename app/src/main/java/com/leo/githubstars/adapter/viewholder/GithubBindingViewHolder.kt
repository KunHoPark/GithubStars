package com.leo.githubstars.adapter.viewholder

import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.leo.githubstars.BR
import com.leo.githubstars.R
import com.leo.githubstars.application.MyGithubStarsApp
import com.leo.githubstars.callback.OnItemClickListener
import com.leo.githubstars.data.local.UserData
import com.leo.githubstars.databinding.ItemGithubViewHolderBinding
import com.leo.githubstars.util.PicassoUtil
import com.leo.githubstars.util.isEnglish

class GithubBindingViewHolder(private var binding: ItemGithubViewHolderBinding, private val listener: OnItemClickListener?) : RecyclerView.ViewHolder(binding.root) {
    internal val tag = this.javaClass.simpleName

    fun onBind(any: Any, position: Int, searchWord: String?) {
        val item = any as UserData
        with(binding){

            this.setVariable(BR.userData, item)
            PicassoUtil.loadImage(MyGithubStarsApp.applicationContext(), item.avatarUrl, ivThumbnail)

            // for text highlight.
            if (searchWord.isNullOrEmpty()){
                tvTitle.text = item.login
                tvDesc.text = item.url
            }else{
                drawHighLight(tvTitle, tvDesc, searchWord, item)
            }

            // Bookmark 아이콘.
            when(item.isBookmark){
                true -> {
                    ivBookmark.setImageResource(R.drawable.btn_done_nor)
                }
                else -> {
                    ivBookmark.setImageResource(R.drawable.btn_done_pre)
                }
            }

            listener?.let {
                this.root.setOnClickListener {
                    listener.onItemClick(item as Object, it, position)
                }
            }
        }
    }

    /**
     * 검색어에 대한 글자 HighLight.
     */
    private fun drawHighLight(tvLogin: TextView, tvName: TextView, searchWord: String?, item: UserData){
        var keyword = searchWord
        var login: String
        var name: String

        if (keyword.isEnglish()) {
            keyword = keyword?.toLowerCase() ?: ""
            login = item.login?.toLowerCase()
            name = item.url?.toLowerCase()
        } else {
            login = item.login
            name = item.url
        }

        if (name.isNullOrEmpty()) name = ""


        // login에 대한 highlight.
        var indexLogin: Int = login?.indexOf(keyword!!)
        var sbLogin = SpannableStringBuilder(item.login)        // original
        while (indexLogin >= 0) {
            sbLogin.setSpan(ForegroundColorSpan( MyGithubStarsApp.applicationContext().resources.getColor(R.color.search_highlighted_color)), indexLogin,
                    indexLogin + keyword!!.length, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
            indexLogin = login?.indexOf(keyword!!, indexLogin + 1) ?: 0
        }

        // name에 대한 highlight.
        var indexName: Int = name?.indexOf(keyword!!)

        var sbName: SpannableStringBuilder?= null
        sbName = if (item.url.isNullOrEmpty()) {
            SpannableStringBuilder(name)
        } else {
            SpannableStringBuilder(item.url)           // original
        }

        while (indexName >= 0) {
            sbName.setSpan(ForegroundColorSpan( MyGithubStarsApp.applicationContext().resources.getColor(R.color.search_highlighted_color)), indexName,
                    indexName + keyword!!.length, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
            indexName = name?.indexOf(keyword!!, indexName + 1) ?: 0
        }

        tvLogin.text = sbLogin
        tvName.text = sbName
    }
}