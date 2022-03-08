package com.zj.play.ui.main.nav

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.zj.play.R

enum class CourseTabs(
    @StringRes val title: Int,
    @DrawableRes val icon: Int,
    @DrawableRes val selectIcon: Int
) {
    HOME_PAGE(R.string.home_page, R.drawable.ic_home, R.drawable.ic_home_fill),
    SYSTEM(
        R.string.home_system,
        R.drawable.ic_system,
        R.drawable.ic_system_fill
    ),
    PROJECT(R.string.project, R.drawable.ic_project, R.drawable.ic_project_fill),
    OFFICIAL_ACCOUNT(
        R.string.official_account,
        R.drawable.ic_official,
        R.drawable.ic_official_fill
    ),
    MINE(R.string.mine, R.drawable.ic_mine, R.drawable.ic_mine_fill)
}
