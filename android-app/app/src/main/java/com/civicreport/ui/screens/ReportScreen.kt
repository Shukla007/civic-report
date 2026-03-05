package com.civicreport.ui.screens

import android.Manifest
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.civicreport.data.api.ApiConstants
import com.civicreport.ui.theme.*
import com.civicreport.viewmodel.ReportViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.location.LocationServices
import java.io.File

@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ReportScreen(
    onReportCreated: (String) -> Unit,
    viewModel: ReportViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val formState by viewModel.formState.collectAsState()
    val scrollState = rememberScrollState()
    
    // Permissions
    val locationPermission = rememberPermissionState(Manifest.permission.ACCESS_FINE_LOCATION)
    val cameraPermission = rememberPermissionState(Manifest.permission.CAMERA)
    
    // Photo picker
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents()
    ) { uris ->
        uris.forEach { uri ->
            viewModel.addPhoto(uri)
        }
    }
    
    // Camera capture
    var tempPhotoUri by remember { mutableStateOf<Uri?>(null) }
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success && tempPhotoUri != null) {
            viewModel.addPhoto(tempPhotoUri!!)
        }
    }
    
    // Get location on mount
    LaunchedEffect(Unit) {
        if (locationPermission.status.isGranted) {
            val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
            try {
                fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                    location?.let {
                        viewModel.updateLocation(it.latitude, it.longitude)
                    }
                }
            } catch (e: SecurityException) {
                // Handle permission denied
            }
        } else {
            locationPermission.launchPermissionRequest()
        }
    }
    
    // Handle success
    LaunchedEffect(formState.isSuccess) {
        if (formState.isSuccess && formState.createdReportId != null) {
            Toast.makeText(
                context,
                "Report submitted! ID: ${formState.createdReportId}",
                Toast.LENGTH_LONG
            ).show()
            viewModel.resetForm()
            onReportCreated(formState.createdReportId!!)
        }
    }
    
    // Handle error
    LaunchedEffect(formState.error) {
        formState.error?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            viewModel.clearError()
        }
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
    ) {
        // Header
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = Primary,
            shadowElevation = 4.dp
        ) {
            Column(
                modifier = Modifier.padding(20.dp)
            ) {
                Text(
                    text = "Report an Issue",
                    style = MaterialTheme.typography.headlineSmall,
                    color = OnPrimary,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Help improve your community",
                    style = MaterialTheme.typography.bodyMedium,
                    color = OnPrimary.copy(alpha = 0.8f)
                )
            }
        }
        
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Title
            OutlinedTextField(
                value = formState.title,
                onValueChange = viewModel::updateTitle,
                label = { Text("Issue Title *") },
                placeholder = { Text("e.g., Pothole on Main Street") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Primary,
                    focusedLabelColor = Primary
                )
            )
            
            // Description
            OutlinedTextField(
                value = formState.description,
                onValueChange = viewModel::updateDescription,
                label = { Text("Description") },
                placeholder = { Text("Provide details about the issue...") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                maxLines = 5,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Primary,
                    focusedLabelColor = Primary
                )
            )
            
            // Category
            var categoryExpanded by remember { mutableStateOf(false) }
            ExposedDropdownMenuBox(
                expanded = categoryExpanded,
                onExpandedChange = { categoryExpanded = it }
            ) {
                OutlinedTextField(
                    value = formState.category,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Category *") },
                    placeholder = { Text("Select a category") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = categoryExpanded) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Primary,
                        focusedLabelColor = Primary
                    )
                )
                
                ExposedDropdownMenu(
                    expanded = categoryExpanded,
                    onDismissRequest = { categoryExpanded = false }
                ) {
                    ApiConstants.CATEGORIES.forEach { category ->
                        DropdownMenuItem(
                            text = { Text(category) },
                            onClick = {
                                viewModel.updateCategory(category)
                                categoryExpanded = false
                            }
                        )
                    }
                }
            }
            
            // Priority
            Text(
                text = "Priority",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Medium
            )
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ApiConstants.PRIORITIES.forEach { priority ->
                    val isSelected = formState.priority == priority
                    val color = when (priority) {
                        "low" -> PriorityLow
                        "medium" -> PriorityMedium
                        "high" -> PriorityHigh
                        "critical" -> PriorityCritical
                        else -> Primary
                    }
                    
                    FilterChip(
                        selected = isSelected,
                        onClick = { viewModel.updatePriority(priority) },
                        label = { Text(priority.replaceFirstChar { it.uppercase() }) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = color.copy(alpha = 0.2f),
                            selectedLabelColor = color
                        )
                    )
                }
            }
            
            // Location
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Surface),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = if (formState.latitude != null) Secondary else OnSurfaceVariant
                    )
                    
                    Spacer(modifier = Modifier.width(12.dp))
                    
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Location",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = if (formState.latitude != null && formState.longitude != null) {
                                "%.6f, %.6f".format(formState.latitude, formState.longitude)
                            } else {
                                "Getting location..."
                            },
                            style = MaterialTheme.typography.bodySmall,
                            color = OnSurfaceVariant
                        )
                    }
                    
                    if (formState.latitude != null) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Location acquired",
                            tint = Secondary
                        )
                    } else {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            strokeWidth = 2.dp
                        )
                    }
                }
            }
            
            // Photos
            Text(
                text = "Photos (${formState.photoUris.size}/5)",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Medium
            )
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Camera button
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .border(2.dp, CardBorder, RoundedCornerShape(8.dp))
                        .clickable {
                            if (cameraPermission.status.isGranted) {
                                val photoFile = File(
                                    context.cacheDir,
                                    "photo_${System.currentTimeMillis()}.jpg"
                                )
                                tempPhotoUri = FileProvider.getUriForFile(
                                    context,
                                    "${context.packageName}.fileprovider",
                                    photoFile
                                )
                                cameraLauncher.launch(tempPhotoUri!!)
                            } else {
                                cameraPermission.launchPermissionRequest()
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.CameraAlt,
                        contentDescription = "Take photo",
                        tint = OnSurfaceVariant
                    )
                }
                
                // Gallery button
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .border(2.dp, CardBorder, RoundedCornerShape(8.dp))
                        .clickable {
                            photoPickerLauncher.launch("image/*")
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.PhotoLibrary,
                        contentDescription = "Choose from gallery",
                        tint = OnSurfaceVariant
                    )
                }
            }
            
            // Photo previews
            if (formState.photoUris.isNotEmpty()) {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(formState.photoUris) { uri ->
                        Box(
                            modifier = Modifier
                                .size(80.dp)
                                .clip(RoundedCornerShape(8.dp))
                        ) {
                            AsyncImage(
                                model = uri,
                                contentDescription = "Photo",
                                modifier = Modifier.fillMaxSize()
                            )
                            
                            IconButton(
                                onClick = { viewModel.removePhoto(uri) },
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .size(24.dp)
                                    .background(Error.copy(alpha = 0.8f), RoundedCornerShape(12.dp))
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Remove",
                                    tint = OnError,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Submit button
            Button(
                onClick = viewModel::submitReport,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                enabled = !formState.isLoading,
                colors = ButtonDefaults.buttonColors(containerColor = Primary),
                shape = RoundedCornerShape(12.dp)
            ) {
                if (formState.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = OnPrimary
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.Send,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Submit Report",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}
