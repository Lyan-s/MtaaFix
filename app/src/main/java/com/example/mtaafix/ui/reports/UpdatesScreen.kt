package com.example.mtaafix.ui.reports

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun UpdatesScreen(
    reportViewModel: ReportViewModel = viewModel()
) {
    LaunchedEffect(Unit) {
        reportViewModel.fetchMyUpdates()
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Updates") }) }
    ) { innerPadding ->
        if (reportViewModel.myUpdates.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Text("No updates yet — you'll see them here when a report's status changes")
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(reportViewModel.myUpdates) { update ->
                    UpdateCard(update)
                }
            }
        }
    }
}

@Composable
private fun UpdateCard(update: StatusUpdate) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surfaceVariant
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "\"${update.reportTitle}\" was marked ${update.newStatus}",
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}