package com.aliayali.banumusagr.navigation

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.vector.ImageVector
import com.aliayali.banumusagr.R
import com.aliayali.home.navigation.HomeNavKey
import com.aliayali.report.navigation.ReportNavKey
import com.example.designsystem.icon.BanumusaIcons
import com.aliayali.home.R as homeR
import com.aliayali.report.R as reportR

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

val REPORT = TopLevelNavItem(
    selectedIcon = BanumusaIcons.Report,
    unselectedIcon = BanumusaIcons.ReportOutlined,
    iconTextId = reportR.string.feature_report_title,
    titleTextId = R.string.app_name,
)


val TOP_LEVEL_NAV_ITEMS = mapOf(
    ReportNavKey to REPORT,
    HomeNavKey to HOME,
)