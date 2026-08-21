package com.example.mtaafix.ui.reports

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Inbox
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.SearchOff
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import kotlin.math.max

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

    val colors = MaterialTheme.colorScheme

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.background)
    ) {

        // --------------------------------------------------------
        // Header
        // --------------------------------------------------------
        Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp)) {
            Text(
                text = "My Reports",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = colors.onPrimaryContainer
            )
            Spacer(modifier = Modifier.height(2.dp))
            val count = reportViewModel.myReports.size
            Text(
                text = if (count == 0) "Nothing reported yet" else "$count report${if (count == 1) "" else "s"} so far",
                style = MaterialTheme.typography.bodyMedium,
                color = colors.onSurfaceVariant
            )
        }

        // --------------------------------------------------------
        // Search
        // --------------------------------------------------------
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Search your reports") },
            leadingIcon = {
                Icon(Icons.Outlined.Search, contentDescription = null, tint = colors.onSurfaceVariant)
            },
            singleLine = true,
            shape = RoundedCornerShape(50),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = colors.primary,
                unfocusedBorderColor = colors.outline,
                focusedContainerColor = colors.surfaceVariant,
                unfocusedContainerColor = colors.surfaceVariant,
                cursorColor = colors.primary
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        // --------------------------------------------------------
        // Filters
        // --------------------------------------------------------
        LazyRow(
            contentPadding = PaddingValues(horizontal = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(statusFilters) { filter ->
                val selected = selectedFilter == filter
                FilterChip(
                    selected = selected,
                    onClick = { selectedFilter = filter },
                    label = { Text(filter) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = colors.primary,
                        selectedLabelColor = colors.onPrimary
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        when {
            reportViewModel.isLoadingReports -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = colors.primary)
                }
            }

            reportViewModel.myReports.isEmpty() -> {
                EmptyState(
                    icon = Icons.Filled.Inbox,
                    title = "No reports yet",
                    subtitle = "Spotted a pothole or a broken streetlight? Tap + to report it."
                )
            }

            filteredReports.isEmpty() -> {
                EmptyState(
                    icon = Icons.Filled.SearchOff,
                    title = "No matches",
                    subtitle = "Nothing fits that search or filter — try clearing one."
                )
            }

            else -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
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
                    item { Spacer(modifier = Modifier.height(8.dp)) }
                }
            }
        }
    }
}

// ------------------------------------------------------------
// Report card
//
// Redesigned from a flat gray box into something with real
// visual hierarchy: a category icon (or photo thumbnail if the
// report has one), a colored severity accent bar so urgency is
// scannable at a glance, and status/upvotes/time as supporting
// detail rather than everything competing at the same weight.
// ------------------------------------------------------------

@Composable
private fun ReportCard(report: Report, onClick: () -> Unit) {
    val colors = MaterialTheme.colorScheme
    val accent = severityColor(report.severity)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = colors.surfaceVariant),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            // Severity accent bar
            Box(
                modifier = Modifier
                    .width(5.dp)
                    .fillMaxHeight()
                    .background(accent)
            )

            Row(
                modifier = Modifier
                    .weight(1f)
                    .padding(14.dp),
                verticalAlignment = Alignment.Top
            ) {
                // Photo thumbnail if available, otherwise a category icon
                if (report.photoUrl.isNotBlank()) {
                    AsyncImage(
                        model = report.photoUrl,
                        contentDescription = report.category,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(56.dp)
                            .clip(RoundedCornerShape(12.dp))
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(colors.primaryContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = categoryIcon(report.category),
                            contentDescription = report.category,
                            tint = colors.onPrimaryContainer,
                            modifier = Modifier.size(26.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        Text(
                            text = report.title.ifBlank { report.category },
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = colors.onPrimaryContainer,
                            modifier = Modifier.weight(1f)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        StatusPill(status = report.status)
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "${report.category} • ${report.severity} severity",
                        style = MaterialTheme.typography.bodySmall,
                        color = colors.onSurfaceVariant
                    )

                    if (report.locationLabel.isNotBlank()) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Filled.LocationOn,
                                contentDescription = null,
                                tint = colors.onSurfaceVariant,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(2.dp))
                            Text(
                                text = report.locationLabel,
                                style = MaterialTheme.typography.bodySmall,
                                color = colors.onSurfaceVariant,
                                maxLines = 1
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = relativeTime(report.createdAt),
                            style = MaterialTheme.typography.labelSmall,
                            color = colors.onSurfaceVariant
                        )

                        if (report.upvotes > 0) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Filled.ThumbUp,
                                    contentDescription = null,
                                    tint = colors.secondary,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(3.dp))
                                Text(
                                    text = "${report.upvotes}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = colors.secondary,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }
                }
            }
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

@Composable
private fun EmptyState(icon: ImageVector, title: String, subtitle: String) {
    val colors = MaterialTheme.colorScheme
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(horizontal = 40.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .clip(CircleShape)
                    .background(colors.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = colors.onPrimaryContainer,
                    modifier = Modifier.size(32.dp)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = colors.onPrimaryContainer
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = colors.onSurfaceVariant,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }
    }
}

// Category and severity helpers now live in ReportVisuals.kt so the
// same mapping is shared with NewReportScreen and AdminReportListScreen.
// ------------------------------------------------------------
// Simple relative time, no external date library needed
// ------------------------------------------------------------

private fun relativeTime(timestampMs: Long): String {
    val diffMs = max(0L, System.currentTimeMillis() - timestampMs)
    val minutes = diffMs / 60_000
    val hours = minutes / 60
    val days = hours / 24

    return when {
        minutes < 1 -> "Just now"
        minutes < 60 -> "${minutes}m ago"
        hours < 24 -> "${hours}h ago"
        days < 7 -> "${days}d ago"
        else -> "${days / 7}w ago"
    }
}