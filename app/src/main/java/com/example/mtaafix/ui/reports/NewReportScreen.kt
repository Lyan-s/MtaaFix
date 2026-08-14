package com.example.mtaafix.ui.reports

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.ui.platform.LocalContext
import android.net.Uri
import coil.compose.AsyncImage
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewReportScreen(
    reportViewModel: ReportViewModel = viewModel(),
    onSubmitSuccess: () -> Unit = {}
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf(ReportCategory.POTHOLE) }
    var selectedSeverity by remember { mutableStateOf(Severity.MEDIUM) }
    var locationLabel by remember { mutableStateOf("") }
    var categoryMenuExpanded by remember { mutableStateOf(false) }
    var photoUri by remember { mutableStateOf<Uri?>(null) }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        photoUri = uri
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
            .padding(24.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "New Report",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Title") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Description") },
            minLines = 3,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        ExposedDropdownMenuBox(
            expanded = categoryMenuExpanded,
            onExpandedChange = { categoryMenuExpanded = it }
        ) {
            OutlinedTextField(
                value = selectedCategory.label,
                onValueChange = {},
                readOnly = true,
                label = { Text("Category") },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = categoryMenuExpanded)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(MenuAnchorType.PrimaryNotEditable)
            )

            ExposedDropdownMenu(
                expanded = categoryMenuExpanded,
                onDismissRequest = { categoryMenuExpanded = false }
            ) {
                ReportCategory.entries.forEach { category ->
                    DropdownMenuItem(
                        text = { Text(category.label) },
                        onClick = {
                            selectedCategory = category
                            categoryMenuExpanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Photo", style = MaterialTheme.typography.labelLarge)
        Spacer(modifier = Modifier.height(8.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp)
                .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(12.dp))
                .clickable { imagePickerLauncher.launch("image/*") },
            contentAlignment = Alignment.Center
        ) {
            if (photoUri != null) {
                AsyncImage(
                    model = photoUri,
                    contentDescription = "Selected photo",
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Filled.Add, contentDescription = null)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("Tap to add a photo")
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "How urgent is this?", style = MaterialTheme.typography.labelLarge)
        Spacer(modifier = Modifier.height(8.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Severity.entries.forEach { severity ->
                FilterChip(
                    selected = selectedSeverity == severity,
                    onClick = { selectedSeverity = severity },
                    label = { Text(severity.label) }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = locationLabel,
            onValueChange = { locationLabel = it },
            label = { Text("Location (e.g. street or landmark)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        reportViewModel.errorMessage?.let { message ->
            Text(text = message, color = MaterialTheme.colorScheme.error)
            Spacer(modifier = Modifier.height(8.dp))
        }

        Button(
            onClick = {
                reportViewModel.submitReport(
                    title = title,
                    description = description,
                    category = selectedCategory.label,
                    severity = selectedSeverity.label,
                    locationLabel = locationLabel,
                    photoUri = photoUri
                )
            },
            enabled = !reportViewModel.isSubmitting,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (reportViewModel.isSubmitting) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    color = MaterialTheme.colorScheme.onPrimary
                )
            } else {
                Text("Submit Report")
            }
        }

        if (reportViewModel.submitSuccess) {
            LaunchedEffect(Unit) {
                onSubmitSuccess()
            }
        }
    }
}