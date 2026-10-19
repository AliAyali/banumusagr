package com.example.designsystem.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.NavigationRailItemDefaults
import androidx.compose.material3.adaptive.WindowAdaptiveInfo
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteItemColors
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffoldDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun BanumusaNavigationSuiteScaffold(
    navigationSuiteItems: BanumusaNavigationSuiteScope.() -> Unit,
    modifier: Modifier = Modifier,
    windowAdaptiveInfo: WindowAdaptiveInfo = currentWindowAdaptiveInfo(),
    content: @Composable () -> Unit,
) {
    val navigationSuiteItemColors = NavigationSuiteItemColors(
        navigationBarItemColors = NavigationBarItemDefaults.colors(
            selectedIconColor = BanumusaNavigationDefaults.navigationSelectedItemColor(),
            unselectedIconColor = BanumusaNavigationDefaults.navigationContentColor(),
            selectedTextColor = BanumusaNavigationDefaults.navigationSelectedItemColor(),
            unselectedTextColor = BanumusaNavigationDefaults.navigationContentColor(),
            indicatorColor = BanumusaNavigationDefaults.navigationIndicatorColor(),
        ),
        navigationRailItemColors = NavigationRailItemDefaults.colors(
            selectedIconColor = BanumusaNavigationDefaults.navigationSelectedItemColor(),
            unselectedIconColor = BanumusaNavigationDefaults.navigationContentColor(),
            selectedTextColor = BanumusaNavigationDefaults.navigationSelectedItemColor(),
            unselectedTextColor = BanumusaNavigationDefaults.navigationContentColor(),
            indicatorColor = BanumusaNavigationDefaults.navigationIndicatorColor(),
        ),
        navigationDrawerItemColors = NavigationDrawerItemDefaults.colors(
            selectedIconColor = BanumusaNavigationDefaults.navigationSelectedItemColor(),
            unselectedIconColor = BanumusaNavigationDefaults.navigationContentColor(),
            selectedTextColor = BanumusaNavigationDefaults.navigationSelectedItemColor(),
            unselectedTextColor = BanumusaNavigationDefaults.navigationContentColor(),
        ),
    )
    NavigationSuiteScaffold(
        navigationSuiteItems = {
            BanumusaNavigationSuiteScope(
                navigationSuiteScope = this,
                navigationSuiteItemColors = navigationSuiteItemColors,
            ).run(navigationSuiteItems)
        },
        layoutType = NavigationSuiteScaffoldDefaults.calculateFromAdaptiveInfo(windowAdaptiveInfo),
        modifier = modifier,
        content = content,
        navigationSuiteColors = NavigationSuiteDefaults.colors(
            navigationBarContainerColor = MaterialTheme.colorScheme.surfaceContainer,
            navigationRailContainerColor = MaterialTheme.colorScheme.surfaceContainer,
        )
    )
}

class BanumusaNavigationSuiteScope internal constructor(
    private val navigationSuiteScope: NavigationSuiteScope,
    private val navigationSuiteItemColors: NavigationSuiteItemColors,
) {
    fun item(
        selected: Boolean,
        onClick: () -> Unit,
        modifier: Modifier = Modifier,
        icon: @Composable () -> Unit,
        selectedIcon: @Composable () -> Unit = icon,
        label: @Composable (() -> Unit)? = null,
    ) = navigationSuiteScope.item(
        selected = selected,
        onClick = onClick,
        icon = {
            if (selected) selectedIcon() else icon()
        },
        label = label,
        modifier = modifier,
        colors = navigationSuiteItemColors,
    )
}

object BanumusaNavigationDefaults {

    @Composable
    fun navigationContentColor() = MaterialTheme.colorScheme.onSurfaceVariant

    @Composable
    fun navigationSelectedItemColor() = MaterialTheme.colorScheme.onBackground

    @Composable
    fun navigationIndicatorColor() = Color.Transparent
}