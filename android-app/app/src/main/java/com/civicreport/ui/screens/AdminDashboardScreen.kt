package com.civicreport.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.civicreport.ui.components.*
import com.civicreport.ui.theme.*
import com.civicreport.viewmodel.DashboardViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminDashboardScreen(
    onReportClick: (String) -> Unit,
    onAnalyticsClick: () -> Unit,
    onLogout: () -> Unit,
    viewModel: DashboardViewModel = hiltViewModel()
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
        // Gradient Header
        GradientHeader {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Admin Dashboard",
                        style = MaterialTheme.typography.headlineSmall,
                        color = OnPrimary,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Manage civic reports",
                        style = MaterialTheme.typography.bodyMedium,
                        color = OnPrimary.copy(alpha = 0.85f)
                    )
                }

                Row {
                    IconButton(onClick = onAnalyticsClick) {
                        Icon(Icons.Default.Analytics, "Analytics", tint = OnPrimary)
                    }
                    IconButton(onClick = {
                        viewModel.logout()
                        onLogout()
                    }) {
                        Icon(Icons.Default.Logout, "Logout", tint = OnPrimary)
                    }
                }
            }
        }

        // Stats
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            val totalReports = state.reports.size
            val pendingCount = state.reports.count { it.status == "pending" }
            val inProgressCount = state.reports.count { it.status == "in-progress" }
            val resolvedCount = state.reports.count { it.status == "resolved" }

            StatCard(
                title = "Total", value = totalReports.toString(),
                icon = { Icon(Icons.Default.Assignment, null, tint = Primary) },
                color = Primary, modifier = Modifier.width(100.dp)
            )
            StatCard(
                title = "Pending", value = pendingCount.toString(),
                icon = { Icon(Icons.Default.Schedule, null, tint = StatusPending) },
                color = StatusPending, modifier = Modifier.width(100.dp)
            )
            StatCard(
                title = "In Progress", value = inProgressCount.toString(),
                icon = { Icon(Icons.Default.Build, null, tint = StatusInProgress) },
                color = StatusInProgress, modifier = Modifier.width(100.dp)
            )
            StatCard(
                title = "Resolved", value = resolvedCount.toString(),
                icon = { Icon(Icons.Default.CheckCircle, null, tint = StatusResolved) },
                color = StatusResolved, modifier = Modifier.width(100.dp)
            )
        }

        // Search
        OutlinedTextField(
            value = state.searchQuery,
            onValueChange = viewModel::updateSearchQuery,
            placeholder = { Text("Search by ID or title...") },
            leadingIcon = { Icon(Icons.Default.Search, null) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                focusedLeadingIconColor = MaterialTheme.colorScheme.primary
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Status filters
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val filters = listOf(
                "all" to "All", "pending" to "Pending",
                "acknowledged" to "Acknowledged", "in-progress" to "In Progress",
                "resolved" to "Resolved", "rejected" to "Rejected"
            )

            filters.forEach { (value, label) ->
                FilterChip(
                    selected = state.statusFilter == value,
                    onClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                        viewModel.updateStatusFilter(value)
                    },
                    label = { Text(label) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f),
                        selectedLabelColor = MaterialTheme.colorScheme.primary
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Reports list
        if (state.isLoading) {
            ShimmerLoadingList(itemCount = 4)
        } else if (state.filteredReports.isEmpty()) {
            EmptyStateView(
                icon = Icons.Default.Inbox,
                title = "No reports found",
                subtitle = "Try adjusting your search or filters"
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 100.dp)
            ) {
                items(state.filteredReports) { report ->
                    ReportCard(
                        report = report,
                        onClick = { onReportClick(report.reportId) }
                    )
                }
            }
        }
    }
}
