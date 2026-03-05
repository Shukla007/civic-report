package com.civicreport.data.model

import com.google.gson.annotations.SerializedName

data class Report(
    @SerializedName("_id")
    val id: String? = null,
    val reportId: String = "",
    val title: String = "",
    val description: String = "",
    val category: String = "",
    val priority: String = "medium",
    val status: String = "pending",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val photos: List<String> = emptyList(),
    val voiceNote: String? = null,
    val assignedTo: String? = null,
    val history: List<HistoryEntry> = emptyList(),
    val createdAt: String? = null,
    val updatedAt: String? = null
)

data class HistoryEntry(
    val timestamp: String = "",
    val action: String = "",
    val notes: String? = null
)

data class ReportCreateRequest(
    val title: String,
    val description: String,
    val category: String,
    val priority: String,
    val latitude: Double,
    val longitude: Double
)

data class ReportUpdateRequest(
    val status: String? = null,
    val assignedTo: String? = null,
    val notes: String? = null
)

data class LoginRequest(
    val username: String,
    val password: String
)

data class LoginResponse(
    val success: Boolean,
    val token: String?,
    val message: String?
)

data class AnalyticsData(
    val total: Int = 0,
    val pending: Int = 0,
    val acknowledged: Int = 0,
    val inProgress: Int = 0,
    val resolved: Int = 0,
    val rejected: Int = 0,
    val byCategory: Map<String, Int> = emptyMap(),
    val byPriority: Map<String, Int> = emptyMap()
)
