package com.leo.githubstars.ui.main.adapter

import android.view.View
import android.widget.EditText
import androidx.databinding.BindingAdapter
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.leo.githubstars.R
import com.leo.githubstars.adapter.GithubAdapter
import com.leo.githubstars.adapter.GithubTabPagerAdapter
import com.leo.githubstars.callback.OnItemClickListener
import com.leo.githubstars.data.local.UserData
import com.leo.githubstars.enums.MainTabItems
import com.leo.githubstars.ui.main.MainActivity
import kotlinx.android.synthetic.main.custom_main_tab_item.view.*


/**
 * Main화면의 DataBinding을 처리 한다.
 * @author LeoPark
 **/
object MainBindingAdapter {

    /**
     * MainTab 환경 구성
     */
    @BindingAdapter("mainActivity", "mainViewPager", "childFragmentManager")
    @JvmStatic
    fun mainBindTabLayoutAdapter(tabLayout: TabLayout, activity: MainActivity, viewPager: ViewPager, childFragmentManager: FragmentManager) {
        viewPager.apply {
            adapter = GithubTabPagerAdapter(childFragmentManager, activity.getTabItemList())
            offscreenPageLimit = MainTabItems.values().count()
            setCurrentItem(MainTabItems.GITHUB.ordinal, true)
        }

        tabLayout.setupWithViewPager(viewPager)
        for (i in 0 until MainTabItems.values().count()) {

            activity.layoutInflater.inflate(R.layout.custom_main_tab_item, null).apply {

                val stringRes = when(i) {
                    1 -> MainTabItems.BOOKMARK.value
                    else -> MainTabItems.GITHUB.value
                }
                tvMainTab.text = stringRes
                tabLayout.getTabAt(i)!!.customView = this
            }
        }
    }

    @BindingAdapter("onItemListener", "list_userData")
    @JvmStatic
    fun setRecyclerViewUserData(recyclerView: RecyclerView, listener: OnItemClickListener, userData: ArrayList<UserData>) {
        recyclerView.adapter?.let {
            (it as GithubAdapter).apply {
                if (getOnItemClickListener() == null) {
                    setOnItemClickListener(listener)
                }
                addItems(userData)
            }
        }
    }

    @BindingAdapter("list_searchWord")
    @JvmStatic
    fun setSearchWord(recyclerView: RecyclerView, searchWord: String) {
        recyclerView.adapter?.let {
            (it as GithubAdapter).apply {
                setSearchWord(searchWord)
            }
        }
    }

    @BindingAdapter("list_reloadGithub")
    @JvmStatic
    fun reloadGithubList(recyclerView: RecyclerView, isReload: Boolean) {
        if (isReload) {
            recyclerView.adapter?.let {
                it.notifyDataSetChanged()
            }
        }
    }

    @BindingAdapter("bind_editText")
    @JvmStatic
    fun onBindEditText(view: EditText, func: (EditText)->Unit) {
        func(view)
    }

}