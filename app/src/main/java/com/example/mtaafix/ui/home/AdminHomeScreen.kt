package com.example.mtaafix.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mtaafix.ui.reports.AdminDashboardScreen
import com.example.mtaafix.ui.reports.AdminReportListScreen
import com.example.mtaafix.ui.reports.Report
import com.example.mtaafix.ui.reports.ReportViewModel

@Composable
fun AdminHomeScreen(
    reportViewModel: ReportViewModel = viewModel(),
    onNavigateToSettings: () -> Unit = {},
    onReportClick: (Report) -> Unit = {}
) {
    var selectedTab by remember { mutableIntStateOf(0) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("MtaaFix — Admin") },
                actions = {
                    IconButton(onClick = onNavigateToSettings) {
                        Icon(Icons.Filled.Settings, contentDescription = "Settings")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            TabRow(selectedTabIndex = selectedTab) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = { Text("Reports") }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = { Text("Dashboard") }
                )
            }

            if (selectedTab == 0) {
                AdminReportListScreen(
                    reportViewModel = reportViewModel,
                    onReportClick = onReportClick
                )
            } else {
                AdminDashboardScreen(reportViewModel = reportViewModel)
            }
        }
    }
}