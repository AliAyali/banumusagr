package com.aliayali.banumusagr.navigation

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.vector.ImageVector
import com.aliayali.banumusagr.R
import com.aliayali.home.navigation.HomeNavKey
import com.example.designsystem.icon.BanumusaIcons
import com.aliayali.home.R as homeR

data class TopLevelNavItem(
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    @param:StringRes val iconTextId: Int,
    @param:StringRes val titleTextId: Int,
)

val HOME = TopLevelNavItem(
    selectedIcon = BanumusaIcons.Home,
    unselectedIcon = BanumusaIcons.HomeOutlined,
    iconTextId = homeR.string.feature_home_title,
    titleTextId = R.string.app_name,
)

val TOP_LEVEL_NAV_ITEMS = mapOf(
    HomeNavKey to HOME,
)