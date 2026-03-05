package com.civicreport.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Report : Screen("report")
    object Track : Screen("track")
    object AdminLogin : Screen("admin_login")
    object AdminDashboard : Screen("admin_dashboard")
    object Analytics : Screen("analytics")
    object ReportDetail : Screen("report_detail/{reportId}") {
        fun createRoute(reportId: String) = "report_detail/$reportId"
    }
}
