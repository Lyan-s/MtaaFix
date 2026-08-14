package com.example.mtaafix.ui.reports

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

private val statusStages = listOf("Pending", "Verified", "Assigned", "In Progress", "Resolved")

@Composable
fun ReportDetailScreen(report: Report?) {
    if (report == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Report not found")
        }
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState())
    ) {
        AsyncImage(
            model = report.photoUrl,
            contentDescription = report.title,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(RoundedCornerShape(12.dp))
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = report.title, style = MaterialTheme.typography.headlineSmall)

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "${report.category} • ${report.severity}",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = report.description, style = MaterialTheme.typography.bodyLarge)

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "📍 ${report.locationLabel}",
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(text = "Status", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(12.dp))

        StatusTracker(currentStatus = report.status)
    }
}

@Composable
private fun StatusTracker(currentStatus: String) {
    val currentIndex = statusStages.indexOf(currentStatus).coerceAtLeast(0)

    Column {
        statusStages.forEachIndexed { index, stage ->
            val isDone = index <= currentIndex
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = if (isDone) "✅" else "⬜",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = stage,
                    style = MaterialTheme.typography.bodyLarge,
                    color = if (isDone) {
                        MaterialTheme.colorScheme.onSurface
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    }
                )
            }
            if (index < statusStages.lastIndex) {
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}