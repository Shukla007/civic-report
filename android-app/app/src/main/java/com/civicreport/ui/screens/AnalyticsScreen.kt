package com.civicreport.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.hilt.navigation.compose.hiltViewModel
import com.civicreport.ui.components.*
import com.civicreport.ui.theme.*
import com.civicreport.viewmodel.AnalyticsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnalyticsScreen(
    onBackClick: () -> Unit,
    viewModel: AnalyticsViewModel = hiltViewModel()
) {
    val context = LocalContext.current
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
                Column {
                    Text(
                        text = "Analytics",
                        style = MaterialTheme.typography.titleLarge,
                        color = OnPrimary,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Report statistics overview",
                        style = MaterialTheme.typography.bodySmall,
                        color = OnPrimary.copy(alpha = 0.85f)
                    )
                }
            }
        }

        if (state.isLoading) {
            ShimmerLoadingList(itemCount = 4)
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Overview cards
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        StatCard(
                            title = "Total Reports",
                            value = state.analytics.total.toString(),
                            icon = { Icon(Icons.Default.Assignment, null, tint = Primary) },
                            color = Primary,
                            modifier = Modifier.weight(1f)
                        )
                        StatCard(
                            title = "Resolved",
                            value = state.analytics.resolved.toString(),
                            icon = { Icon(Icons.Default.CheckCircle, null, tint = StatusResolved) },
                            color = StatusResolved,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                // Resolution rate
                item {
                    AnalyticsCard(title = "Resolution Rate") {
                        val resolutionRate = if (state.analytics.total > 0) {
                            (state.analytics.resolved.toFloat() / state.analytics.total) * 100
                        } else 0f

                        LinearProgressIndicator(
                            progress = { resolutionRate / 100f },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(12.dp)
                                .clip(RoundedCornerShape(6.dp)),
                            color = StatusResolved,
                            trackColor = StatusResolved.copy(alpha = 0.15f),
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "%.1f%% of reports resolved".format(resolutionRate),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                // Status distribution
                item {
                    AnalyticsCard(title = "Status Distribution") {
                        val statuses = listOf(
                            "Pending" to state.analytics.pending to StatusPending,
                            "Acknowledged" to state.analytics.acknowledged to StatusAcknowledged,
                            "In Progress" to state.analytics.inProgress to StatusInProgress,
                            "Resolved" to state.analytics.resolved to StatusResolved,
                            "Rejected" to state.analytics.rejected to StatusRejected
                        )

                        statuses.forEach { (pair, color) ->
                            val (label, count) = pair
                            val percentage = if (state.analytics.total > 0) {
                                (count.toFloat() / state.analytics.total) * 100
                            } else 0f

                            AnalyticsBarRow(label, count, percentage, color)
                        }
                    }
                }

                // Priority distribution
                item {
                    AnalyticsCard(title = "Priority Distribution") {
                        val priorities = listOf(
                            "Low" to (state.analytics.byPriority["low"] ?: 0) to PriorityLow,
                            "Medium" to (state.analytics.byPriority["medium"] ?: 0) to PriorityMedium,
                            "High" to (state.analytics.byPriority["high"] ?: 0) to PriorityHigh
                        )

                        priorities.forEach { (pair, color) ->
                            val (label, count) = pair
                            val percentage = if (state.analytics.total > 0) {
                                (count.toFloat() / state.analytics.total) * 100
                            } else 0f

                            AnalyticsBarRow(label, count, percentage, color)
                        }
                    }
                }

                // Category distribution
                item {
                    AnalyticsCard(title = "Category Distribution") {
                        state.analytics.byCategory.entries
                            .sortedByDescending { it.value }
                            .forEach { (category, count) ->
                                val percentage = if (state.analytics.total > 0) {
                                    (count.toFloat() / state.analytics.total) * 100
                                } else 0f

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = category,
                                        style = MaterialTheme.typography.bodyMedium,
                                        modifier = Modifier.weight(1f),
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        text = "$count (%.0f%%)".format(percentage),
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }

                                LinearProgressIndicator(
                                    progress = { percentage / 100f },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(6.dp)
                                        .clip(RoundedCornerShape(3.dp)),
                                    color = MaterialTheme.colorScheme.primary,
                                    trackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f),
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                    }
                }

                item { Spacer(modifier = Modifier.height(32.dp)) }
            }
        }
    }
}

@Composable
private fun AnalyticsCard(
    title: String,
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
                .padding(16.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(16.dp))
            content()
        }
    }
}

@Composable
private fun AnalyticsBarRow(
    label: String,
    count: Int,
    percentage: Float,
    color: androidx.compose.ui.graphics.Color
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.width(100.dp),
            color = MaterialTheme.colorScheme.onSurface
        )
        LinearProgressIndicator(
            progress = { percentage / 100f },
            modifier = Modifier
                .weight(1f)
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp)),
            color = color,
            trackColor = color.copy(alpha = 0.12f),
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = count.toString(),
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.width(40.dp),
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}
