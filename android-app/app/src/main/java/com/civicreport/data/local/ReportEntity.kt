package com.civicreport.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.civicreport.data.model.HistoryEntry
import com.civicreport.data.model.Report
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

@Entity(tableName = "reports")
@TypeConverters(Converters::class)
data class ReportEntity(
    @PrimaryKey
    val id: String,
    val reportId: String,
    val title: String,
    val description: String,
    val category: String,
    val priority: String = "medium",
    val status: String = "pending",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val photos: List<String> = emptyList(),
    val voiceNote: String? = null,
    val assignedTo: String? = null,
    val history: List<HistoryEntry> = emptyList(),
    val createdAt: String,
    val updatedAt: String
)

class Converters {
    private val gson = Gson()

    @TypeConverter
    fun fromStringList(value: List<String>): String = gson.toJson(value)

    @TypeConverter
    fun toStringList(value: String): List<String> {
        val type = object : TypeToken<List<String>>() {}.type
        return gson.fromJson(value, type) ?: emptyList()
    }

    @TypeConverter
    fun fromHistoryList(value: List<HistoryEntry>): String = gson.toJson(value)

    @TypeConverter
    fun toHistoryList(value: String): List<HistoryEntry> {
        val type = object : TypeToken<List<HistoryEntry>>() {}.type
        return gson.fromJson(value, type) ?: emptyList()
    }
}

fun ReportEntity.toReport(): Report {
    return Report(
        id = id,
        reportId = reportId,
        title = title,
        description = description,
        category = category,
        priority = priority,
        status = status,
        latitude = latitude,
        longitude = longitude,
        photos = photos,
        voiceNote = voiceNote,
        assignedTo = assignedTo,
        history = history,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}

fun Report.toEntity(): ReportEntity {
    return ReportEntity(
        id = id ?: java.util.UUID.randomUUID().toString(),
        reportId = reportId,
        title = title,
        description = description,
        category = category,
        priority = priority,
        status = status,
        latitude = latitude,
        longitude = longitude,
        photos = photos,
        voiceNote = voiceNote,
        assignedTo = assignedTo,
        history = history,
        createdAt = createdAt ?: java.time.Instant.now().toString(),
        updatedAt = updatedAt ?: java.time.Instant.now().toString()
    )
}

