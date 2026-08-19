package com.example.mtaafix.ui.reports

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage

private val statusStages = listOf("Pending", "Verified", "Assigned", "In Progress", "Resolved")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportDetailScreen(
    report: Report?,
    isAdmin: Boolean = false,
    reportViewModel: ReportViewModel = viewModel(),
    onDeleted: () -> Unit = {}
) {
    if (report == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Report not found")
        }
        return
    }

    var statusMenuExpanded by remember { mutableStateOf(false) }
    var pendingStatus by remember(report.status) { mutableStateOf(report.status) }
    var showDeleteDialog by remember { mutableStateOf(false) }

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

        Spacer(modifier = Modifier.height(16.dp))

        val currentUserId = com.google.firebase.auth.FirebaseAuth.getInstance().currentUser?.uid
        val hasUpvoted = currentUserId != null && report.upvotedBy.contains(currentUserId)

        OutlinedButton(
            onClick = { reportViewModel.toggleUpvote(report.id) },
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = if (hasUpvoted) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
            )
        ) {
            Icon(
                imageVector = Icons.Filled.CheckCircle,
                contentDescription = null,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(if (hasUpvoted) "You confirmed this (${report.upvotes})" else "Me too (${report.upvotes})")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(text = "Status", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(12.dp))

        StatusTracker(currentStatus = report.status)

        if (!isAdmin) {
            Spacer(modifier = Modifier.height(24.dp))
            OutlinedButton(
                onClick = { showDeleteDialog = true },
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = MaterialTheme.colorScheme.error
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Delete Report")
            }
        }

        if (showDeleteDialog) {
            AlertDialog(
                onDismissRequest = { showDeleteDialog = false },
                title = { Text("Delete this report?") },
                text = { Text("This can't be undone.") },
                confirmButton = {
                    TextButton(onClick = {
                        showDeleteDialog = false
                        reportViewModel.deleteReport(report.id, onDeleted)
                    }) {
                        Text("Delete", color = MaterialTheme.colorScheme.error)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDeleteDialog = false }) {
                        Text("Cancel")
                    }
                }
            )
        }

        if (isAdmin) {
            Spacer(modifier = Modifier.height(24.dp))
            Text(text = "Update Status (Admin)", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))

            ExposedDropdownMenuBox(
                expanded = statusMenuExpanded,
                onExpandedChange = { statusMenuExpanded = it }
            ) {
                OutlinedTextField(
                    value = pendingStatus,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("New status") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = statusMenuExpanded)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                )

                ExposedDropdownMenu(
                    expanded = statusMenuExpanded,
                    onDismissRequest = { statusMenuExpanded = false }
                ) {
                    statusStages.forEach { stage ->
                        DropdownMenuItem(
                            text = { Text(stage) },
                            onClick = {
                                pendingStatus = stage
                                statusMenuExpanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = { reportViewModel.updateReportStatus(report.id, pendingStatus) },
                enabled = pendingStatus != report.status,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save Status")
            }
        }
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