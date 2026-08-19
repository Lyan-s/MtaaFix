package com.example.mtaafix.ui.reports

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

/**
 * Single source of truth for status -> color across the app.
 *
 * Previously each screen (ReportListScreen, AdminReportListScreen,
 * AdminDashboardScreen) had its own status-color logic and they
 * disagreed with each other — some statuses were indistinguishable,
 * others used different colors for the same status depending on
 * which screen you were looking at. This gives every status a
 * consistent container/content color pair, reused everywhere.
 *
 * Progression reads as: neutral (nothing happening yet) -> blue
 * (acknowledged) -> orange (staffed) -> light green (underway) ->
 * solid green (done).
 */
data class StatusColorPair(
    val container: Color,
    val content: Color
)

@Composable
fun statusColors(status: String): StatusColorPair {
    val colors = MaterialTheme.colorScheme
    return when (status) {
        "Pending" -> StatusColorPair(colors.surfaceVariant, colors.onSurfaceVariant)
        "Verified" -> StatusColorPair(colors.primaryContainer, colors.onPrimaryContainer)
        "Assigned" -> StatusColorPair(colors.secondaryContainer, colors.onSecondaryContainer)
        "In Progress" -> StatusColorPair(colors.tertiaryContainer, colors.onTertiaryContainer)
        "Resolved" -> StatusColorPair(colors.tertiary, colors.onTertiary)
        else -> StatusColorPair(colors.surfaceVariant, colors.onSurfaceVariant)
    }
}