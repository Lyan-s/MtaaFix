package com.example.mtaafix.ui.navigation

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mtaafix.ui.auth.AuthViewModel
import com.example.mtaafix.ui.auth.LoginScreen
import com.example.mtaafix.ui.auth.SignUpScreen
import com.example.mtaafix.ui.home.AdminHomeScreen
import com.example.mtaafix.ui.onboarding.OnboardingScreen
import com.example.mtaafix.ui.onboarding.SplashScreen
import com.example.mtaafix.ui.home.HomeScreen
import com.example.mtaafix.ui.reports.NewReportScreen
import com.example.mtaafix.ui.reports.ReportDetailScreen
import com.example.mtaafix.ui.reports.ReportViewModel
import com.example.mtaafix.ui.reports.UpdatesScreen
import com.example.mtaafix.ui.settings.SettingsScreen

object Routes {
    const val LOGIN = "login"
    const val SIGN_UP = "signup"
    const val HOME = "home"
    const val ADMIN_HOME = "admin_home"
    const val SETTINGS = "settings"
    const val NEW_REPORT = "new_report"
    const val SPLASH = "splash"
    const val ONBOARDING = "onboarding"
    const val REPORT_DETAIL = "report_detail"
    const val UPDATES = "updates"
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val authViewModel: AuthViewModel = viewModel()
    val reportViewModel: ReportViewModel = viewModel()

    val context = LocalContext.current
    val prefs = remember { context.getSharedPreferences("mtaafix_prefs", Context.MODE_PRIVATE) }
    var onboardingComplete by remember {
        mutableStateOf(prefs.getBoolean("onboarding_complete", false))
    }

    NavHost(
        navController = navController,
        startDestination = Routes.SPLASH
    ) {
        composable(Routes.SPLASH) {
            SplashScreen(
                onFinished = {
                    val destination = if (onboardingComplete) Routes.LOGIN else Routes.ONBOARDING
                    navController.navigate(destination) {
                        popUpTo(Routes.SPLASH) { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.ONBOARDING) {
            OnboardingScreen(
                onFinished = {
                    prefs.edit().putBoolean("onboarding_complete", true).apply()
                    onboardingComplete = true
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.ONBOARDING) { inclusive = true }
                    }
                }
            )
        }
        composable(Routes.LOGIN) {
            LoginScreen(
                authViewModel = authViewModel,
                onLoginSuccess = {
                    val destination = if (authViewModel.isAdmin) Routes.ADMIN_HOME else Routes.HOME
                    navController.navigate(destination) {
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
                authViewModel = authViewModel,
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
                onNavigateToUpdates = {
                    navController.navigate(Routes.UPDATES)
                },
                onReportClick = {
                    navController.navigate(Routes.REPORT_DETAIL)
                }
            )
        }

        composable(Routes.UPDATES) {
            UpdatesScreen(reportViewModel = reportViewModel)
        }

        composable(Routes.ADMIN_HOME) {
            AdminHomeScreen(
                reportViewModel = reportViewModel,
                onNavigateToSettings = {
                    navController.navigate(Routes.SETTINGS)
                },
                onReportClick = {
                    navController.navigate(Routes.REPORT_DETAIL)
                }
            )
        }

        composable(Routes.SETTINGS) {
            SettingsScreen(
                authViewModel = authViewModel,
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
            ReportDetailScreen(
                report = reportViewModel.selectedReport,
                isAdmin = authViewModel.isAdmin,
                reportViewModel = reportViewModel,
                onDeleted = {
                    navController.popBackStack()
                }
            )
        }
    }
}