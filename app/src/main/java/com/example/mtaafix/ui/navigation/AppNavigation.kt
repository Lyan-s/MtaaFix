package com.example.mtaafix.ui.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mtaafix.ui.auth.LoginScreen
import com.example.mtaafix.ui.auth.SignUpScreen
import com.example.mtaafix.ui.home.HomeScreen
import com.example.mtaafix.ui.reports.NewReportScreen
import com.example.mtaafix.ui.reports.ReportDetailScreen
import com.example.mtaafix.ui.reports.ReportViewModel
import com.example.mtaafix.ui.settings.SettingsScreen

object Routes {
    const val LOGIN = "login"
    const val SIGN_UP = "signup"
    const val HOME = "home"
    const val SETTINGS = "settings"
    const val NEW_REPORT = "new_report"
    const val REPORT_DETAIL = "report_detail"
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    // Shared across Home, NewReport, and ReportDetail so the selected
    // report and the reports list stay in sync without re-fetching
    val reportViewModel: ReportViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = Routes.LOGIN
    ) {
        composable(Routes.LOGIN) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                },
                onNavigateToSignUp = {
                    navController.navigate(Routes.SIGN_UP)
                }
            )
        }

        composable(Routes.SIGN_UP) {
            SignUpScreen(
                onSignUpSuccess = {
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                },
                onNavigateToLogin = {
                    navController.navigate(Routes.LOGIN)
                }
            )
        }

        composable(Routes.HOME) {
            HomeScreen(
                reportViewModel = reportViewModel,
                onNavigateToSettings = {
                    navController.navigate(Routes.SETTINGS)
                },
                onNavigateToNewReport = {
                    navController.navigate(Routes.NEW_REPORT)
                },
                onReportClick = {
                    navController.navigate(Routes.REPORT_DETAIL)
                }
            )
        }

        composable(Routes.SETTINGS) {
            SettingsScreen(
                onLoggedOut = {
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.NEW_REPORT) {
            NewReportScreen(
                reportViewModel = reportViewModel,
                onSubmitSuccess = {
                    navController.popBackStack()
                }
            )
        }

        composable(Routes.REPORT_DETAIL) {
            ReportDetailScreen(report = reportViewModel.selectedReport)
        }
    }
}