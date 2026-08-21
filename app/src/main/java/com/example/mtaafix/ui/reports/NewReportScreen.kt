package com.example.mtaafix.ui.reports

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.content.Intent
import android.location.LocationManager
import android.provider.Settings
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.mtaafix.ui.auth.AnimatedStatusBanner

// ------------------------------------------------------------
// Report submission screen, restyled around card-based sections
// with a photo-first layout, icon-driven category picker, and
// a colored severity selector — closer to a native camera-report
// flow than a plain stacked form.
// ------------------------------------------------------------

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
    var photoUri by remember { mutableStateOf<Uri?>(null) }
    var latitude by remember { mutableStateOf<Double?>(null) }
    var longitude by remember { mutableStateOf<Double?>(null) }
    var locationStatus by remember { mutableStateOf("Location not captured") }
    var showSubmitError by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val colors = MaterialTheme.colorScheme

    fun captureLocation() {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val hasGpsPermission = ContextCompat.checkSelfPermission(
            context, Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        if (!hasGpsPermission) {
            locationStatus = "Location permission not granted"
            return
        }

        val providers = listOf(LocationManager.GPS_PROVIDER, LocationManager.NETWORK_PROVIDER)
        val anyProviderEnabled = providers.any { locationManager.isProviderEnabled(it) }

        if (!anyProviderEnabled) {
            locationStatus = "Location services are off"
            return
        }

        val lastKnown = providers.firstNotNullOfOrNull { provider ->
            try {
                if (locationManager.isProviderEnabled(provider)) {
                    locationManager.getLastKnownLocation(provider)
                } else null
            } catch (e: SecurityException) {
                null
            }
        }

        if (lastKnown != null) {
            latitude = lastKnown.latitude
            longitude = lastKnown.longitude
            locationStatus = "Location captured"
        } else {
            locationStatus = "Couldn't get location — try again outdoors or with a stronger signal"
        }
    }

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            captureLocation()
        } else {
            locationStatus = "Location permission denied"
        }
    }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        photoUri = uri
    }

    LaunchedEffect(reportViewModel.errorMessage) {
        showSubmitError = reportViewModel.errorMessage != null
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.background)
            .imePadding()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 20.dp)
    ) {
        Text(
            text = "Report an Issue",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = colors.onPrimaryContainer
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = "Snap it, tag it, and we'll route it to the right team.",
            style = MaterialTheme.typography.bodyMedium,
            color = colors.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(20.dp))

        // ----------------------------------------------------
        // Photo — the big, camera-forward card up top
        // ----------------------------------------------------
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(if (photoUri == null) colors.primaryContainer else colors.surfaceVariant)
                .clickable { imagePickerLauncher.launch("image/*") },
            contentAlignment = Alignment.Center
        ) {
            if (photoUri != null) {
                AsyncImage(
                    model = photoUri,
                    contentDescription = "Selected photo",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                // Small "change photo" pill overlaid in the corner
                Surface(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(12.dp),
                    shape = RoundedCornerShape(50),
                    color = colors.background.copy(alpha = 0.92f)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Edit,
                            contentDescription = null,
                            tint = colors.primary,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Change photo",
                            style = MaterialTheme.typography.labelMedium,
                            color = colors.primary,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            } else {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .clip(CircleShape)
                            .background(colors.primary),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.CameraAlt,
                            contentDescription = null,
                            tint = colors.onPrimary,
                            modifier = Modifier.size(26.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "Tap to add a photo",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = colors.onPrimaryContainer
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "A clear photo helps it get resolved faster",
                        style = MaterialTheme.typography.bodySmall,
                        color = colors.onPrimaryContainer.copy(alpha = 0.75f)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // ----------------------------------------------------
        // Category — icon chips instead of a dropdown
        // ----------------------------------------------------
        Text(
            text = "What's the issue?",
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.SemiBold,
            color = colors.onPrimaryContainer
        )
        Spacer(modifier = Modifier.height(10.dp))

        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            ReportCategory.entries.forEach { category ->
                CategoryChip(
                    label = category.label,
                    icon = categoryIcon(category.label),
                    selected = selectedCategory == category,
                    onClick = { selectedCategory = category }
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // ----------------------------------------------------
        // Severity — colored priority selector
        // ----------------------------------------------------
        Text(
            text = "How urgent is this?",
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.SemiBold,
            color = colors.onPrimaryContainer
        )
        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Severity.entries.forEach { severity ->
                SeverityOption(
                    label = severity.label,
                    selected = selectedSeverity == severity,
                    color = severityColor(severity.label),
                    onClick = { selectedSeverity = severity },
                    modifier = Modifier.weight(1f)
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // ----------------------------------------------------
        // Title & description
        // ----------------------------------------------------
        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Title") },
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth(),
            colors = fieldColors(colors)
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Description") },
            minLines = 3,
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth(),
            colors = fieldColors(colors)
        )

        Spacer(modifier = Modifier.height(24.dp))

        // ----------------------------------------------------
        // Location card
        // ----------------------------------------------------
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = colors.surfaceVariant,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Filled.LocationOn,
                        contentDescription = null,
                        tint = colors.primary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Location",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.SemiBold,
                        color = colors.onPrimaryContainer
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = locationLabel,
                    onValueChange = { locationLabel = it },
                    label = { Text("Street or landmark") },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth(),
                    colors = fieldColors(colors)
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedButton(
                        onClick = {
                            val hasPermission = ContextCompat.checkSelfPermission(
                                context, Manifest.permission.ACCESS_FINE_LOCATION
                            ) == PackageManager.PERMISSION_GRANTED

                            if (hasPermission) {
                                captureLocation()
                            } else {
                                locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                            }
                        },
                        shape = RoundedCornerShape(50),
                        border = androidx.compose.foundation.BorderStroke(1.dp, colors.primary),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = colors.primary)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.MyLocation,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Attach GPS")
                    }

                    if (latitude != null && longitude != null) {
                        Spacer(modifier = Modifier.width(10.dp))
                        Surface(
                            shape = RoundedCornerShape(50),
                            color = colors.tertiaryContainer
                        ) {
                            Text(
                                text = "%.4f, %.4f".format(latitude, longitude),
                                style = MaterialTheme.typography.labelSmall,
                                color = colors.onTertiaryContainer,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = locationStatus,
                    style = MaterialTheme.typography.bodySmall,
                    color = colors.onSurfaceVariant
                )

                if (locationStatus == "Location services are off") {
                    TextButton(
                        onClick = {
                            context.startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                        },
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Text("Turn on Location", color = colors.primary)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        AnimatedStatusBanner(
            visible = showSubmitError,
            message = reportViewModel.errorMessage ?: "",
            isSuccess = false
        )

        Button(
            onClick = {
                reportViewModel.submitReport(
                    title = title,
                    description = description,
                    category = selectedCategory.label,
                    severity = selectedSeverity.label,
                    locationLabel = locationLabel,
                    photoUri = photoUri,
                    latitude = latitude,
                    longitude = longitude
                )
            },
            enabled = !reportViewModel.isSubmitting,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = colors.primary,
                contentColor = colors.onPrimary
            )
        ) {
            if (reportViewModel.isSubmitting) {
                CircularProgressIndicator(
                    modifier = Modifier.size(22.dp),
                    color = colors.onPrimary,
                    strokeWidth = 2.dp
                )
            } else {
                Text("Submit Report", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        }

        if (reportViewModel.submitSuccess) {
            LaunchedEffect(Unit) {
                onSubmitSuccess()
            }
        }
    }
}

@Composable
private fun CategoryChip(
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    selected: Boolean,
    onClick: () -> Unit
) {
    val colors = MaterialTheme.colorScheme
    val containerColor = if (selected) colors.primary else colors.surfaceVariant
    val contentColor = if (selected) colors.onPrimary else colors.onSurfaceVariant

    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(50),
        color = containerColor
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = contentColor,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.labelLarge,
                color = contentColor,
                fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal
            )
        }
    }
}

@Composable
private fun SeverityOption(
    label: String,
    selected: Boolean,
    color: androidx.compose.ui.graphics.Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.colorScheme
    val borderColor = if (selected) color else colors.outline

    Surface(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(14.dp),
        color = if (selected) color.copy(alpha = 0.15f) else colors.surfaceVariant,
        border = androidx.compose.foundation.BorderStroke(if (selected) 2.dp else 1.dp, borderColor)
    ) {
        Column(
            modifier = Modifier.padding(vertical = 14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(12.dp)
                    .clip(CircleShape)
                    .background(color)
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal,
                color = colors.onPrimaryContainer
            )
        }
    }
}

@Composable
private fun fieldColors(colors: ColorScheme) = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = colors.primary,
    unfocusedBorderColor = colors.outline,
    focusedContainerColor = colors.surfaceVariant,
    unfocusedContainerColor = colors.surfaceVariant,
    cursorColor = colors.primary
)