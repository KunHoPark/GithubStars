package com.leo.githubstars.adapter.viewholder

import android.support.v7.widget.RecyclerView
import com.leo.githubstars.BR
import com.leo.githubstars.application.MyGithubStarsApp
import com.leo.githubstars.callback.OnItemClickListener
import com.leo.githubstars.data.local.UserData
import com.leo.githubstars.databinding.ItemGithubViewHolderBinding
import com.leo.githubstars.util.PicassoUtil

class GithubBindingViewHolder(private var binding: ItemGithubViewHolderBinding, private val listener: OnItemClickListener?) : RecyclerView.ViewHolder(binding.root) {
    internal val tag = this.javaClass.simpleName

    fun onBind(any: Any, position: Int, size: Int) {
        val item = any as UserData
        with(binding){

            this.setVariable(BR.userData, item)
            PicassoUtil.loadImage(MyGithubStarsApp.applicationContext(), item.avatarUrl, ivThumbnail)

            listener?.let {
                this.root.setOnClickListener {
                    listener.onItemClick(item as Object, it, position)
                }
            }
        }


    }
}