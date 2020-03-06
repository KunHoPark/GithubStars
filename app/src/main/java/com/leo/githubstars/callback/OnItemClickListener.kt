package com.leo.githubstars.callback

import android.view.View
import com.leo.githubstars.data.local.UserData

/**
 * 리스트의 Callback
 * @author LeoPark
 */
interface OnItemClickListener {
    fun onItemClick(item: UserData, view: View, position: Int)
}