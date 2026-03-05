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
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.civicreport.data.api.ApiConstants
import com.civicreport.ui.components.*
import com.civicreport.ui.theme.*
import com.civicreport.viewmodel.TrackViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrackScreen(
    viewModel: TrackViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current
    val state by viewModel.state.collectAsState()

    LaunchedEffect(state.error) {
        state.error?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            viewModel.clearError()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Header
        GradientHeader {
            Column(modifier = Modifier.padding(20.dp)) {
                Text("Track Your Report", style = MaterialTheme.typography.headlineSmall, color = OnPrimary, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(4.dp))
                Text("Enter your Report ID to check status", style = MaterialTheme.typography.bodyMedium, color = OnPrimary.copy(alpha = 0.85f))
            }
        }

        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            // Search
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = state.searchQuery,
                    onValueChange = viewModel::updateSearchQuery,
                    placeholder = { Text("Enter Report ID (e.g., IND-00001)") },
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    leadingIcon = { Icon(Icons.Default.Search, null) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        focusedLeadingIconColor = MaterialTheme.colorScheme.primary
                    )
                )
                Button(
                    onClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        viewModel.searchReport()
                    },
                    enabled = !state.isLoading,
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                    shape = RoundedCornerShape(12.dp),
                    contentPadding = PaddingValues(16.dp)
                ) {
                    if (state.isLoading) CircularProgressIndicator(Modifier.size(24.dp), color = OnPrimary, strokeWidth = 2.dp)
                    else Icon(Icons.Default.Search, "Search")
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Report details
            if (state.report != null) {
                val report = state.report!!
                LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    // Header card
                    item {
                        TrackCard {
                            Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween, Alignment.CenterVertically) {
                                Text(report.reportId, style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                                StatusChip(status = report.status)
                            }
                            Spacer(Modifier.height(12.dp))
                            Text(report.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurface)
                            if (report.description.isNotBlank()) {
                                Text(report.description, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(top = 8.dp))
                            }
                            Spacer(Modifier.height(12.dp))
                            Row(Modifier.fillMaxWidth(), Arrangement.spacedBy(16.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Category, null, Modifier.size(16.dp), tint = MaterialTheme.colorScheme.onSurfaceVariant)
                                    Spacer(Modifier.width(4.dp))
                                    Text(report.category, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                                PriorityChip(priority = report.priority)
                            }
                        }
                    }

                    // Photos
                    if (report.photos.isNotEmpty()) {
                        item {
                            TrackCard {
                                Text("Photos", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurface)
                                Spacer(Modifier.height(12.dp))
                                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    items(report.photos) { photoUrl ->
                                        AsyncImage(
                                            model = "${ApiConstants.BASE_URL.dropLast(1)}$photoUrl",
                                            contentDescription = "Report photo",
                                            modifier = Modifier.size(100.dp).clip(RoundedCornerShape(12.dp)),
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
                            TrackCard {
                                Text("Status Timeline", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurface)
                                Spacer(Modifier.height(16.dp))
                                report.history.forEachIndexed { index, entry ->
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
                    item { Spacer(Modifier.height(32.dp)) }
                }
            } else if (!state.isLoading && state.searchQuery.isBlank()) {
                EmptyStateView(
                    icon = Icons.Default.Search,
                    title = "Enter a Report ID to track",
                    subtitle = "Search using IDs like IND-00001"
                )
            }
        }
    }
}

@Composable
private fun TrackCard(content: @Composable ColumnScope.() -> Unit) {
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
