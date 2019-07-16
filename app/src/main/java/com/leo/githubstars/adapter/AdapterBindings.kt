package ccom.leo.githubstars.adapter

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.databinding.ObservableArrayList
import androidx.recyclerview.widget.RecyclerView
import com.leo.githubstars.adapter.GithubAdapter
import com.leo.githubstars.application.MyGithubStarsApp
import com.leo.githubstars.data.local.UserData
import com.leo.githubstars.util.PicassoUtil

object AdapterBindings {

    @BindingAdapter("githubItems")
    @JvmStatic
    fun setGettyImageItems(recyclerView: RecyclerView, userData: ObservableArrayList<UserData>) {
        val adapter = recyclerView.adapter as GithubAdapter
        adapter.addItems(userData)
    }

    @BindingAdapter("loadImage")
    @JvmStatic
    fun setLoadImage(imageView: ImageView, url: String) {
        PicassoUtil.loadImage(MyGithubStarsApp.applicationContext(), url, imageView)
    }

}