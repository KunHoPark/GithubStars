package com.leo.githubstars.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.leo.githubstars.BR
import com.leo.githubstars.callback.OnItemClickListener
import com.leo.githubstars.data.local.UserData
import com.leo.githubstars.databinding.ItemGithubViewHolderBinding

/**
 * 리스트의 viewholder
 * @author LeoPark
 */
class GithubBindingViewHolder(private var binding: ItemGithubViewHolderBinding, private val listener: OnItemClickListener?) : RecyclerView.ViewHolder(binding.root) {
    internal val tag = this.javaClass.simpleName

    fun onBind(any: Any, position: Int, searchWord: String?) {
        val item = any as UserData
        binding.apply{

            this.setVariable(BR.userData, item)
            this.setVariable(BR.searchword, searchWord)
            this.executePendingBindings()

            listener?.let {
                this.root.setOnClickListener {
                    listener.onItemClick(item, it, position)
                }
                ivBookmark.setOnClickListener {
                    listener.onItemClick(item, it, position)
                }
            }
        }
    }
}