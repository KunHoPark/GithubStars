package com.leo.githubstars.enums

import com.leo.githubstars.R
import com.leo.githubstars.extension.toResString

/**
 * Main tab의 item을 분류하기 위한 enum
 * @author LeoPark
 **/
enum class MainTabItems(val value: String) {
    GITHUB(R.string.common_tab_title_github.toResString),
    BOOKMARK(R.string.common_tab_title_bookmark.toResString),
}
