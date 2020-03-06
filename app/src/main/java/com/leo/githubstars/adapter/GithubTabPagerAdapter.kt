package com.leo.githubstars.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.leo.githubstars.enums.MainTabItems

/**
 * GithubTabPagerAdapter
 * TabPagerAdapter, 서버 정보와 로컬 정보를 표시 하기 위함.
 * @author LeoPark
 */
class GithubTabPagerAdapter(fm: FragmentManager, fragments: HashMap<MainTabItems, Fragment>) : FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private val fragment = fragments

    override fun getItem(position: Int): Fragment {
        return when(position){
            MainTabItems.BOOKMARK.ordinal -> {
                fragment[MainTabItems.BOOKMARK]
            }
            else -> {
                fragment[MainTabItems.GITHUB]
            }
        }!!
    }

    override fun getCount(): Int = MainTabItems.values().count()

    override fun getPageTitle(position: Int): CharSequence? {
        return when(position){
            MainTabItems.BOOKMARK.ordinal -> {
                MainTabItems.BOOKMARK.value
            }
            else -> {
                MainTabItems.GITHUB.value
            }
        }
    }
}