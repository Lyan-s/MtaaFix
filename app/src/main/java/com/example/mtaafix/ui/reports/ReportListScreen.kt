package com.example.mtaafix.ui.reports

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

private val statusFilters = listOf("All", "Pending", "Verified", "Assigned", "In Progress", "Resolved")

@Composable
fun ReportListScreen(
    reportViewModel: ReportViewModel = viewModel(),
    onReportClick: (Report) -> Unit = {}
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf("All") }

    LaunchedEffect(Unit) {
        reportViewModel.fetchMyReports()
    }

    val filteredReports = reportViewModel.myReports.filter { report ->
        val matchesFilter = selectedFilter == "All" || report.status == selectedFilter
        val matchesSearch = searchQuery.isBlank() ||
                report.title.contains(searchQuery, ignoreCase = true) ||
                report.category.contains(searchQuery, ignoreCase = true)
        matchesFilter && matchesSearch
    }

    Column(modifier = Modifier.fillMaxSize()) {
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Search your reports") },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        )

        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(statusFilters) { filter ->
                FilterChip(
                    selected = selectedFilter == filter,
                    onClick = { selectedFilter = filter },
                    label = { Text(filter) }
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        when {
            reportViewModel.isLoadingReports -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }

            reportViewModel.myReports.isEmpty() -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("You haven't reported anything yet — tap + to get started")
                }
            }

            filteredReports.isEmpty() -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No reports match your search or filter")
                }
            }

            else -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(filteredReports) { report ->
                        ReportCard(
                            report = report,
                            onClick = {
                                reportViewModel.selectReport(report)
                                onReportClick(report)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ReportCard(report: Report, onClick: () -> Unit) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surfaceVariant
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = report.title,
                    style = MaterialTheme.typography.titleMedium
                )
                StatusPill(status = report.status)
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "${report.category} • ${report.severity}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun StatusPill(status: String) {
    val colors = statusColors(status)
    Surface(
        shape = RoundedCornerShape(50),
        color = colors.container
    ) {
        Text(
            text = status,
            style = MaterialTheme.typography.labelSmall,
            color = colors.content,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
        )
    }
}