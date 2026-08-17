package com.example.mtaafix.ui.reports

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun AdminDashboardScreen(
    reportViewModel: ReportViewModel = viewModel()
) {
    LaunchedEffect(Unit) {
        reportViewModel.fetchAllReports()
    }

    val reports = reportViewModel.allReports
    val total = reports.size
    val pending = reports.count { it.status == "Pending" }
    val verified = reports.count { it.status == "Verified" }
    val assigned = reports.count { it.status == "Assigned" }
    val inProgress = reports.count { it.status == "In Progress" }
    val resolved = reports.count { it.status == "Resolved" }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            StatCard(label = "Total", value = total, modifier = Modifier.weight(1f))
            StatCard(label = "Pending", value = pending, modifier = Modifier.weight(1f))
        }
        Spacer(modifier = Modifier.height(12.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            StatCard(label = "In Progress", value = inProgress, modifier = Modifier.weight(1f))
            StatCard(label = "Resolved", value = resolved, modifier = Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.height(24.dp))
        Text(text = "Reports by status", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(12.dp))

        StatusBar(label = "Pending", count = pending, total = total, color = MaterialTheme.colorScheme.secondaryContainer)
        StatusBar(label = "Verified", count = verified, total = total, color = MaterialTheme.colorScheme.primaryContainer)
        StatusBar(label = "Assigned", count = assigned, total = total, color = MaterialTheme.colorScheme.primaryContainer)
        StatusBar(label = "In Progress", count = inProgress, total = total, color = MaterialTheme.colorScheme.primaryContainer)
        StatusBar(label = "Resolved", count = resolved, total = total, color = MaterialTheme.colorScheme.tertiaryContainer)
    }
}

@Composable
private fun StatCard(label: String, value: Int, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surfaceVariant
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = value.toString(), style = MaterialTheme.typography.headlineMedium)
            Text(text = label, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
private fun StatusBar(
    label: String,
    count: Int,
    total: Int,
    color: androidx.compose.ui.graphics.Color
) {
    val fraction = if (total > 0) count.toFloat() / total else 0f

    Column(modifier = Modifier.padding(bottom = 12.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = label, style = MaterialTheme.typography.bodyMedium)
            Text(text = count.toString(), style = MaterialTheme.typography.bodyMedium)
        }
        Spacer(modifier = Modifier.height(4.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(10.dp)
                .clip(RoundedCornerShape(50))
                .background(MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(fraction.coerceIn(0f, 1f))
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(50))
                    .background(color)
            )
        }
    }
}