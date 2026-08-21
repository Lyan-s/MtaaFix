package com.example.mtaafix.ui.reports

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Report
import androidx.compose.material.icons.filled.Route
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Category -> icon mapping (matches ReportCategory in ReportsOptions.kt).
 * Shared by ReportListScreen, AdminReportListScreen and NewReportScreen so
 * the same issue always shows the same icon everywhere in the app.
 */
fun categoryIcon(category: String): ImageVector = when (category) {
    "Potholes" -> Icons.Filled.Build
    "Streetlights" -> Icons.Filled.Lightbulb
    "Water leaks" -> Icons.Filled.WaterDrop
    "Garbage" -> Icons.Filled.Delete
    "Roads" -> Icons.Filled.Route
    "Electrical" -> Icons.Filled.Bolt
    else -> Icons.Filled.Report
}

/**
 * Severity -> accent color (matches Severity in ReportsOptions.kt).
 */
@Composable
fun severityColor(severity: String): Color {
    val colors = MaterialTheme.colorScheme
    return when (severity) {
        "High" -> colors.error
        "Medium" -> colors.secondary
        "Low" -> colors.tertiary
        else -> colors.outline
    }
}