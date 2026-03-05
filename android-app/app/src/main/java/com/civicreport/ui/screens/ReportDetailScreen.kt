package com.civicreport.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.civicreport.data.api.ApiConstants
import com.civicreport.ui.components.*
import com.civicreport.ui.theme.*
import com.civicreport.viewmodel.ReportDetailViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportDetailScreen(
    reportId: String,
    onBackClick: () -> Unit,
    viewModel: ReportDetailViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val state by viewModel.state.collectAsState()

    LaunchedEffect(state.error) {
        state.error?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            viewModel.clearError()
        }
    }

    LaunchedEffect(state.updateSuccess) {
        if (state.updateSuccess) {
            Toast.makeText(context, "Report updated successfully", Toast.LENGTH_SHORT).show()
            viewModel.clearUpdateSuccess()
        }
    }

    // Update dialog
    if (state.showUpdateDialog) {
        AlertDialog(
            onDismissRequest = viewModel::hideUpdateDialog,
            title = { Text("Update Status") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    Text("Select new status:", style = MaterialTheme.typography.bodyMedium)
                    ApiConstants.STATUSES.forEach { status ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = state.selectedStatus == status,
                                onClick = { viewModel.updateSelectedStatus(status) },
                                colors = RadioButtonDefaults.colors(selectedColor = MaterialTheme.colorScheme.primary)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = status.replaceFirstChar { it.uppercase() }.replace("-", " "),
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                    OutlinedTextField(
                        value = state.updateNotes,
                        onValueChange = viewModel::updateNotes,
                        label = { Text("Notes (optional)") },
                        modifier = Modifier.fillMaxWidth(),
                        maxLines = 3,
                        shape = RoundedCornerShape(12.dp)
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = viewModel::updateReport,
                    enabled = !state.isUpdating,
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    if (state.isUpdating) {
                        CircularProgressIndicator(modifier = Modifier.size(20.dp), color = OnPrimary, strokeWidth = 2.dp)
                    } else { Text("Update") }
                }
            },
            dismissButton = { TextButton(onClick = viewModel::hideUpdateDialog) { Text("Cancel") } }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Gradient Header
        GradientHeader {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(Icons.Default.ArrowBack, "Back", tint = OnPrimary)
                }
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Report Details",
                        style = MaterialTheme.typography.titleLarge,
                        color = OnPrimary,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = reportId,
                        style = MaterialTheme.typography.bodySmall,
                        color = OnPrimary.copy(alpha = 0.85f)
                    )
                }
                IconButton(onClick = viewModel::showUpdateDialog) {
                    Icon(Icons.Default.Edit, "Update status", tint = OnPrimary)
                }
            }
        }

        if (state.isLoading) {
            ShimmerLoadingList(itemCount = 3)
        } else if (state.report != null) {
            val report = state.report!!

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Main info card
                item {
                    DetailCard {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = report.reportId,
                                style = MaterialTheme.typography.titleLarge,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Bold
                            )
                            StatusChip(status = report.status)
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = report.title,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        if (report.description.isNotBlank()) {
                            Text(
                                text = report.description,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(top = 8.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))
                        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text("Category", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Text(report.category, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.onSurface)
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                Text("Priority", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                PriorityChip(priority = report.priority)
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.LocationOn, null, Modifier.size(16.dp), tint = MaterialTheme.colorScheme.onSurfaceVariant)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("%.6f, %.6f".format(report.latitude, report.longitude), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }

                        report.createdAt?.let { createdAt ->
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Schedule, null, Modifier.size(16.dp), tint = MaterialTheme.colorScheme.onSurfaceVariant)
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Created: ${formatTimestamp(createdAt)}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                    }
                }

                // Photos
                if (report.photos.isNotEmpty()) {
                    item {
                        DetailCard {
                            Text("Photos (${report.photos.size})", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurface)
                            Spacer(modifier = Modifier.height(12.dp))
                            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                items(report.photos) { photoUrl ->
                                    val fullUrl = "${ApiConstants.BASE_URL.dropLast(1)}$photoUrl"
                                    AsyncImage(
                                        model = fullUrl, contentDescription = "Report photo",
                                        modifier = Modifier.size(120.dp).clip(RoundedCornerShape(12.dp)),
                                        contentScale = ContentScale.Crop
                                    )
                                }
                            }
                        }
                    }
                }

                // Timeline
                if (report.history.isNotEmpty()) {
                    item {
                        DetailCard {
                            Text("History", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurface)
                            Spacer(modifier = Modifier.height(16.dp))
                            report.history.reversed().forEachIndexed { index, entry ->
                                TimelineItem(
                                    title = entry.action,
                                    timestamp = formatTimestamp(entry.timestamp),
                                    notes = entry.notes,
                                    isLast = index == report.history.lastIndex
                                )
                            }
                        }
                    }
                }

                // Update button
                item {
                    Button(
                        onClick = viewModel::showUpdateDialog,
                        modifier = Modifier.fillMaxWidth().height(56.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Icon(Icons.Default.Edit, null, Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Update Status", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                    }
                }

                item { Spacer(modifier = Modifier.height(32.dp)) }
            }
        } else {
            ErrorMessage(message = "Report not found", onRetry = viewModel::loadReport)
        }
    }
}

@Composable
private fun DetailCard(
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .border(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
                .padding(16.dp),
            content = content
        )
    }
}

private fun formatTimestamp(timestamp: String): String {
    return try {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        inputFormat.timeZone = TimeZone.getTimeZone("UTC")
        val date = inputFormat.parse(timestamp)
        val outputFormat = SimpleDateFormat("MMM dd, yyyy 'at' hh:mm a", Locale.getDefault())
        outputFormat.format(date!!)
    } catch (e: Exception) {
        timestamp
    }
}
