package com.leo.githubstars.callback

import android.view.View

interface OnItemClickListener {
    fun onItemClick(item: Object, view: View, position: Int)
}