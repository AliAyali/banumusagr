package com.aliayali.banumusagr

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.aliayali.banumusagr.ui.DashboardScreen
import com.aliayali.banumusagr.ui.ReportFormScreen

private object AppRoutes {
    const val DASHBOARD = "dashboard"
    const val ADD_REPORT = "add_report"
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = AppRoutes.DASHBOARD
    ) {
        composable(AppRoutes.DASHBOARD) {
            DashboardScreen(
                onAddReportClick = {
                    navController.navigate(AppRoutes.ADD_REPORT)
                }
            )
        }


        composable(AppRoutes.ADD_REPORT) {
            ReportFormScreen(
                onReportSubmitted = {
                    navController.popBackStack()
                }
            )
        }
    }
}
