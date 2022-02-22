package com.zj.play.ui.main.nav

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.zj.play.R

enum class CourseTabs(
    @StringRes val title: Int,
    @DrawableRes val icon: Int,
    @DrawableRes val selectIcon: Int
) {
    HOME_PAGE(R.string.home_page, R.drawable.ic_nav_news_normal, R.drawable.ic_nav_news_actived),
    SYSTEM(
        R.string.home_system,
        R.drawable.ic_nav_discover_normal,
        R.drawable.ic_nav_discover_actived
    ),
    PROJECT(R.string.project, R.drawable.ic_nav_tweet_normal, R.drawable.ic_nav_tweet_actived),
    OFFICIAL_ACCOUNT(
        R.string.official_account,
        R.drawable.ic_nav_discover_normal,
        R.drawable.ic_nav_discover_actived
    ),
    MINE(R.string.mine, R.drawable.ic_nav_my_normal, R.drawable.ic_nav_my_pressed)
}
