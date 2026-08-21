package com.example.mtaafix.ui.reports

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Timelapse
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

// ------------------------------------------------------------
// Colors come from MaterialTheme.colorScheme (see ui/theme/Theme.kt)
// and shared status colors from StatusColors.kt — not local hex
// values — so this screen follows the same light/dark theme and
// status-color language as the rest of the app.
// ------------------------------------------------------------

// ============================================================
// ADMIN DASHBOARD
// ============================================================

@Composable
fun AdminDashboardScreen(
    reportViewModel: ReportViewModel = viewModel(),
    onReportClick: (Report) -> Unit = {},
    onViewAllReports: () -> Unit = {},
    onNavigateToSettings: () -> Unit = {}
) {
    val colors = MaterialTheme.colorScheme

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

    Scaffold(
        containerColor = colors.background,
        topBar = {
            AdminTopBar(
                isRefreshing = reportViewModel.isLoadingReports,
                onRefresh = { reportViewModel.fetchAllReports() }
            )
        }
    ) { paddingValues ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 18.dp, bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            // Welcome section
            item {
                Column {
                    Text(
                        text = "Good morning, Admin \uD83D\uDC4B",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = colors.onPrimaryContainer
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Here's what's happening with community reports.",
                        fontSize = 14.sp,
                        color = colors.onSurfaceVariant
                    )
                }
            }

            item {
                Text(
                    text = "Overview",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = colors.onPrimaryContainer
                )
            }

            // Stat cards
            item {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        AdminStatCard(
                            label = "Total Reports",
                            value = total,
                            icon = Icons.Default.Assignment,
                            iconColor = colors.primary,
                            modifier = Modifier.weight(1f)
                        )
                        AdminStatCard(
                            label = "Pending",
                            value = pending,
                            icon = Icons.Default.HourglassTop,
                            iconColor = statusColors("Pending").content,
                            modifier = Modifier.weight(1f)
                        )
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        AdminStatCard(
                            label = "In Progress",
                            value = inProgress,
                            icon = Icons.Default.Timelapse,
                            iconColor = statusColors("In Progress").content,
                            modifier = Modifier.weight(1f)
                        )
                        AdminStatCard(
                            label = "Resolved",
                            value = resolved,
                            icon = Icons.Default.CheckCircle,
                            iconColor = statusColors("Resolved").content,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            item {
                ResolutionCard(resolved = resolved, total = total)
            }

            item {
                Text(
                    text = "Reports by Status",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = colors.onPrimaryContainer
                )
            }

            // Status bars
            item {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    color = colors.surfaceVariant,
                    shadowElevation = 1.dp
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(15.dp)
                    ) {
                        StatusBar(label = "Pending", count = pending, total = total, color = statusColors("Pending").content)
                        StatusBar(label = "Verified", count = verified, total = total, color = statusColors("Verified").content)
                        StatusBar(label = "Assigned", count = assigned, total = total, color = statusColors("Assigned").content)
                        StatusBar(label = "In Progress", count = inProgress, total = total, color = statusColors("In Progress").content)
                        StatusBar(label = "Resolved", count = resolved, total = total, color = statusColors("Resolved").content)
                    }
                }
            }

            // Recent reports header
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Recent Reports",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = colors.onPrimaryContainer
                    )
                    TextButton(onClick = onViewAllReports) {
                        Text(text = "View All", color = colors.primary, fontWeight = FontWeight.Bold)
                    }
                }
            }

            if (reports.isEmpty()) {
                item { EmptyReportsCard() }
            } else {
                items(items = reports.take(5)) { report ->
                    ReportPreviewCard(
                        report = report,
                        onClick = {
                            reportViewModel.selectReport(report)
                            onReportClick(report)
                        }
                    )
                }
            }

            // Admin tools — currently routes to Settings since that's
            // where system-level configuration lives today; repoint
            // this once there's a dedicated admin-tools destination.
            item {
                AdminToolsCard(onOpen = onNavigateToSettings)
            }
        }
    }
}

// ============================================================
// TOP BAR
// ============================================================

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AdminTopBar(
    isRefreshing: Boolean,
    onRefresh: () -> Unit
) {
    val colors = MaterialTheme.colorScheme

    TopAppBar(
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "Mtaa", fontWeight = FontWeight.ExtraBold, fontSize = 23.sp, color = colors.primary)
                Text(text = "Fix", fontWeight = FontWeight.ExtraBold, fontSize = 23.sp, color = colors.secondary)
            }
        },
        actions = {
            IconButton(onClick = onRefresh, enabled = !isRefreshing) {
                if (isRefreshing) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp,
                        color = colors.primary
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Refresh",
                        tint = colors.onPrimaryContainer
                    )
                }
            }

            Box(
                modifier = Modifier
                    .padding(end = 16.dp)
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(colors.primary),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "A", color = colors.onPrimary, fontWeight = FontWeight.Bold)
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = colors.background)
    )
}

// ============================================================
// STAT CARD
// ============================================================

@Composable
private fun AdminStatCard(
    label: String,
    value: Int,
    icon: ImageVector,
    iconColor: Color,
    modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.colorScheme

    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(18.dp),
        color = colors.surfaceVariant,
        shadowElevation = 1.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(iconColor.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = icon, contentDescription = null, tint = iconColor, modifier = Modifier.size(23.dp))
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(text = value.toString(), fontSize = 28.sp, fontWeight = FontWeight.ExtraBold, color = colors.onPrimaryContainer)

            Spacer(modifier = Modifier.height(2.dp))

            Text(text = label, fontSize = 12.sp, color = colors.onSurfaceVariant)
        }
    }
}

// ============================================================
// RESOLUTION CARD
// ============================================================

@Composable
private fun ResolutionCard(resolved: Int, total: Int) {
    val colors = MaterialTheme.colorScheme
    val percentage = if (total > 0) ((resolved.toFloat() / total.toFloat()) * 100).toInt() else 0

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        color = colors.primary
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(text = "Resolution Rate", fontSize = 14.sp, color = colors.onPrimary.copy(alpha = 0.8f))
                    Spacer(modifier = Modifier.height(5.dp))
                    Text(text = "$percentage%", fontSize = 32.sp, fontWeight = FontWeight.ExtraBold, color = colors.onPrimary)
                }

                Box(
                    modifier = Modifier
                        .size(58.dp)
                        .clip(CircleShape)
                        .background(colors.secondary),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = colors.onSecondary,
                        modifier = Modifier.size(30.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            LinearProgressIndicator(
                progress = { (percentage / 100f).coerceIn(0f, 1f) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(50)),
                color = colors.secondary,
                trackColor = colors.onPrimary.copy(alpha = 0.2f)
            )
        }
    }
}

// ============================================================
// STATUS BAR
// ============================================================

@Composable
private fun StatusBar(label: String, count: Int, total: Int, color: Color) {
    val colors = MaterialTheme.colorScheme
    val fraction = if (total > 0) count.toFloat() / total.toFloat() else 0f
    val percentage = if (total > 0) ((count.toFloat() / total.toFloat()) * 100).toInt() else 0

    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(modifier = Modifier.size(9.dp).clip(CircleShape).background(color))
                Spacer(modifier = Modifier.width(9.dp))
                Text(text = label, fontSize = 14.sp, color = colors.onPrimaryContainer, fontWeight = FontWeight.Medium)
            }
            Text(
                text = "$count  •  $percentage%",
                fontSize = 13.sp,
                color = colors.onSurfaceVariant,
                fontWeight = FontWeight.SemiBold
            )
        }

        Spacer(modifier = Modifier.height(7.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(9.dp)
                .clip(RoundedCornerShape(50))
                .background(colors.outlineVariant)
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

// ============================================================
// REPORT PREVIEW
//
// Now wired to the real Report model (title, category, location)
// instead of the "Community Issue" / "MtaaFix Report" placeholder
// text, and clickable through to the report's detail screen.
// ============================================================

@Composable
private fun ReportPreviewCard(report: Report, onClick: () -> Unit) {
    val colors = MaterialTheme.colorScheme
    val statusColorPair = statusColors(report.status)

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        color = colors.surfaceVariant,
        shadowElevation = 1.dp
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(45.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(statusColorPair.container),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = categoryIcon(report.category),
                    contentDescription = null,
                    tint = statusColorPair.content
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = report.title.ifBlank { report.category },
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = colors.onPrimaryContainer,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    text = report.locationLabel.ifBlank { report.category },
                    fontSize = 12.sp,
                    color = colors.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Surface(shape = RoundedCornerShape(50), color = statusColorPair.container) {
                Text(
                    text = report.status,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = statusColorPair.content
                )
            }
        }
    }
}

// ============================================================
// EMPTY REPORTS
// ============================================================

@Composable
private fun EmptyReportsCard() {
    val colors = MaterialTheme.colorScheme

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        color = colors.surfaceVariant
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(30.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.Assignment,
                contentDescription = null,
                modifier = Modifier.size(45.dp),
                tint = colors.primary
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(text = "No reports yet", fontSize = 17.sp, fontWeight = FontWeight.Bold, color = colors.onPrimaryContainer)
            Spacer(modifier = Modifier.height(5.dp))
            Text(text = "Community reports will appear here.", fontSize = 13.sp, color = colors.onSurfaceVariant)
        }
    }
}

// ============================================================
// ADMIN TOOLS
// ============================================================

@Composable
private fun AdminToolsCard(onOpen: () -> Unit) {
    val colors = MaterialTheme.colorScheme

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        color = colors.surfaceVariant
    ) {
        Row(modifier = Modifier.padding(18.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(45.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(colors.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = Icons.Default.Settings, contentDescription = null, tint = colors.onPrimaryContainer)
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(text = "Admin Tools", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = colors.onPrimaryContainer)
                Text(text = "Manage reports and system settings", fontSize = 12.sp, color = colors.onSurfaceVariant)
            }

            TextButton(onClick = onOpen) {
                Text(text = "Open", color = colors.primary, fontWeight = FontWeight.Bold)
            }
        }
    }
}