package ccom.leo.githubstars.adapter

import android.databinding.BindingAdapter
import android.databinding.ObservableArrayList
import android.support.v7.widget.RecyclerView
import android.widget.ImageView
import com.leo.githubstars.adapter.GithubAdapter
import com.leo.githubstars.application.MyGithubApp
import com.leo.githubstars.data.local.UserData
import com.leo.githubstars.util.PicassoUtil

object AdapterBindings {

    @BindingAdapter("githubItems")
    @JvmStatic
    fun setGettyImageItems(recyclerView: RecyclerView, userData: ObservableArrayList<UserData>) {
        val adapter = recyclerView.adapter as GithubAdapter
        if (adapter != null) {
            adapter!!.addItems(userData)
        }
    }

    @BindingAdapter("loadImage")
    @JvmStatic
    fun setLoadImage(imageView: ImageView, url: String) {
        PicassoUtil.loadImage(MyGithubApp.applicationContext(), url, imageView)
    }

}