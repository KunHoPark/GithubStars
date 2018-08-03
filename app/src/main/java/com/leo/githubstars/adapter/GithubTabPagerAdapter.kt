package com.leo.githubstars.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import com.leo.githubstars.R
import com.leo.githubstars.application.MyGithubStarsApp
import com.leo.githubstars.ui.main.MainFragment
import com.leo.githubstars.ui.main.tab.BookmarkTabFragment
import com.leo.githubstars.ui.main.tab.GithubTabFragment

/**
 * GithubTabPagerAdapter
 * TabPagerAdapter, 서버 정보와 로컬 정보를 표시 하기 위함.
 * @author KunHoPark
 * @since 2018. 8. 2. PM 20:09
 **/
class GithubTabPagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {

    override fun getItem(position: Int): Fragment? {
        return when(position){
            MainFragment.Variable.TAB_FRAGMENT_BTC -> {
                BookmarkTabFragment.newInstance()
            }
            else -> {
                GithubTabFragment.newInstance()
            }
        }

    }

    override fun getCount(): Int = MainFragment.Variable.TAB_ITEMS

    override fun getPageTitle(position: Int): CharSequence? {
        return when(position) {
            MainFragment.Variable.TAB_FRAGMENT_BTC -> {
                MyGithubStarsApp.applicationContext().resources.getString(R.string.common_tab_title_bookmark)
            }
            else -> {
                MyGithubStarsApp.applicationContext().resources.getString(R.string.common_tab_title_github)
            }
        }
    }
}