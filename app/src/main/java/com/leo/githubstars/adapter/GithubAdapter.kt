package com.leo.githubstars.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.leo.githubstars.adapter.viewholder.GithubBindingViewHolder
import com.leo.githubstars.callback.OnItemClickListener
import com.leo.githubstars.data.local.UserData
import com.leo.githubstars.databinding.ItemGithubViewHolderBinding
import com.leo.githubstars.util.LeoLog

/**
 * GettyImageAdapter
 * @author KunHoPark
 * @since 2018. 7. 29. AM 10:43
 **/
class GithubAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    internal val tag = this.javaClass.simpleName
    private var listener: OnItemClickListener? = null
    private var listData: List<UserData>? = null
    private var searchWord: String?= null               // 검색 단어

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GithubBindingViewHolder {
        val binding = ItemGithubViewHolderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GithubBindingViewHolder(binding, listener)
    }

    fun setOnItemClickListener(listener: OnItemClickListener){
        this.listener = listener
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        listData?.let {
            if (it.size>position){
                val item = it[position]
                (holder as GithubBindingViewHolder).onBind(item, position, searchWord)
            }
        }
    }

    fun addItems(any: List<Any>) {
        LeoLog.i(tag, "addItems size=${any.size}")

        val items: List<UserData> = any as List<UserData>
        listData = items
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return listData?.let {
            it.size
        }?:0
    }

    fun setSearchWord(searchWord: String?){
        this.searchWord = searchWord
    }

}