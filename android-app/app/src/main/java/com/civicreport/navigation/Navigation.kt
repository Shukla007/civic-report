package com.civicreport.navigation

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.civicreport.ui.screens.*
import com.civicreport.viewmodel.AuthViewModel

data class BottomNavItem(
    val route: String,
    val title: String,
    val selectedIcon: @Composable () -> Unit,
    val unselectedIcon: @Composable () -> Unit
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CivicReportNavigation() {
    val navController = rememberNavController()
    val authViewModel: AuthViewModel = hiltViewModel()
    val isLoggedIn by authViewModel.isLoggedIn.collectAsState()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val bottomNavItems = listOf(
        BottomNavItem(
            Screen.Report.route, "Report",
            selectedIcon = { Icon(Icons.Default.AddCircle, "Report") },
            unselectedIcon = { Icon(Icons.Outlined.AddCircle, "Report") }
        ),
        BottomNavItem(
            Screen.Track.route, "Track",
            selectedIcon = { Icon(Icons.Default.Search, "Track") },
            unselectedIcon = { Icon(Icons.Outlined.Search, "Track") }
        ),
        BottomNavItem(
            if (isLoggedIn) Screen.AdminDashboard.route else Screen.AdminLogin.route,
            "Admin",
            selectedIcon = { Icon(Icons.Default.Person, "Admin") },
            unselectedIcon = { Icon(Icons.Outlined.Person, "Admin") }
        )
    )

    val showBottomBar = currentRoute in listOf(
        Screen.Report.route,
        Screen.Track.route,
        Screen.AdminLogin.route,
        Screen.AdminDashboard.route,
        Screen.Analytics.route
    )

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar(
                    modifier = Modifier.navigationBarsPadding(),
                    containerColor = MaterialTheme.colorScheme.surface,
                    tonalElevation = 0.dp
                ) {
                    bottomNavItems.forEach { item ->
                        val selected = when {
                            item.route == Screen.AdminDashboard.route && currentRoute in listOf(
                                Screen.AdminDashboard.route,
                                Screen.Analytics.route,
                                Screen.AdminLogin.route
                            ) -> true
                            else -> currentRoute == item.route
                        }

                        NavigationBarItem(
                            selected = selected,
                            onClick = {
                                navController.navigate(item.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = { if (selected) item.selectedIcon() else item.unselectedIcon() },
                            label = {
                                Text(
                                    item.title,
                                    fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(
                                indicatorColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f),
                                selectedIconColor = MaterialTheme.colorScheme.primary,
                                selectedTextColor = MaterialTheme.colorScheme.primary,
                                unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = Screen.Report.route,
            modifier = Modifier.padding(bottom = paddingValues.calculateBottomPadding()),
            enterTransition = { fadeIn(animationSpec = tween(300)) + slideInHorizontally(initialOffsetX = { 30 }, animationSpec = tween(300)) },
            exitTransition = { fadeOut(animationSpec = tween(200)) },
            popEnterTransition = { fadeIn(animationSpec = tween(300)) + slideInHorizontally(initialOffsetX = { -30 }, animationSpec = tween(300)) },
            popExitTransition = { fadeOut(animationSpec = tween(200)) }
        ) {
            composable(Screen.Report.route) {
                ReportScreen(
                    onReportCreated = { reportId ->
                        navController.navigate(Screen.Track.route) {
                            popUpTo(Screen.Report.route) {
                                inclusive = false
                                saveState = false
                            }
                            launchSingleTop = true
                            restoreState = false
                        }
                    }
                )
            }

            composable(Screen.Track.route) {
                TrackScreen()
            }

            composable(Screen.AdminLogin.route) {
                AdminLoginScreen(
                    onLoginSuccess = {
                        navController.navigate(Screen.AdminDashboard.route) {
                            popUpTo(Screen.AdminLogin.route) { inclusive = true }
                        }
                    }
                )
            }

            composable(Screen.AdminDashboard.route) {
                AdminDashboardScreen(
                    onReportClick = { reportId ->
                        navController.navigate(Screen.ReportDetail.createRoute(reportId))
                    },
                    onAnalyticsClick = {
                        navController.navigate(Screen.Analytics.route)
                    },
                    onLogout = {
                        navController.navigate(Screen.AdminLogin.route) {
                            popUpTo(Screen.AdminDashboard.route) { inclusive = true }
                        }
                    }
                )
            }

            composable(Screen.Analytics.route) {
                AnalyticsScreen(
                    onBackClick = { navController.popBackStack() }
                )
            }

            composable(
                route = Screen.ReportDetail.route,
                arguments = listOf(navArgument("reportId") { type = NavType.StringType })
            ) { backStackEntry ->
                val reportId = backStackEntry.arguments?.getString("reportId") ?: ""
                ReportDetailScreen(
                    reportId = reportId,
                    onBackClick = { navController.popBackStack() }
                )
            }
        }
    }
}
