package com.example.mtaafix.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mtaafix.ui.reports.Report
import com.example.mtaafix.ui.reports.ReportListScreen
import com.example.mtaafix.ui.reports.ReportViewModel

@Composable
fun HomeScreen(
    reportViewModel: ReportViewModel = viewModel(),
    onNavigateToSettings: () -> Unit = {},
    onNavigateToNewReport: () -> Unit = {},
    onReportClick: (Report) -> Unit = {}
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("MtaaFix") },
                actions = {
                    IconButton(onClick = onNavigateToSettings) {
                        Icon(Icons.Filled.Settings, contentDescription = "Settings")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onNavigateToNewReport) {
                Icon(Icons.Filled.Add, contentDescription = "Report an issue")
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            ReportListScreen(
                reportViewModel = reportViewModel,
                onReportClick = onReportClick
            )
        }
    }
}