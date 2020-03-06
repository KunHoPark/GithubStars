package com.leo.githubstars.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.leo.githubstars.adapter.viewholder.GithubBindingViewHolder
import com.leo.githubstars.callback.OnItemClickListener
import com.leo.githubstars.data.local.UserData
import com.leo.githubstars.databinding.ItemGithubViewHolderBinding
import com.leo.githubstars.util.LeoLog

/**
 * 리스트의 Adapter
 * @author LeoPark
 */
class GithubAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    internal val tag = this.javaClass.simpleName
    private var listener: OnItemClickListener? = null
    private var listData: List<UserData>? = null
    private var searchWord: String= ""               // 검색 단어

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GithubBindingViewHolder {
        val binding = ItemGithubViewHolderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GithubBindingViewHolder(binding, listener)
    }

    val setOnItemClickListener: (OnItemClickListener) -> Unit = { this.listener = it }
    val getOnItemClickListener: () -> OnItemClickListener? = { this.listener }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        listData?.let {
            if (it.size>position){
                it[position]?.let { item ->
                    (holder as GithubBindingViewHolder).onBind(item, position, searchWord)
                }
            }
        }
    }

    fun addItems(any: List<UserData>) {
        LeoLog.i(tag, "addItems size=${any.size}")

        val items: List<UserData> = any
        listData = items
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return listData?.let {
            it.size
        }?:0
    }

    val setSearchWord: (String) -> Unit= {
        this.searchWord = it
    }
}