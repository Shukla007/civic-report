package com.civicreport.data.repository

import android.content.Context
import android.net.Uri
import com.civicreport.data.local.ReportDao
import com.civicreport.data.local.ReportEntity
import com.civicreport.data.local.toReport
import com.civicreport.data.model.*
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

sealed class ApiResult<out T> {
    data class Success<T>(val data: T) : ApiResult<T>()
    data class Error(val message: String) : ApiResult<Nothing>()
    object Loading : ApiResult<Nothing>()
}

@Singleton
class ReportRepository @Inject constructor(
    private val reportDao: ReportDao,
    private val authRepository: AuthRepository,
    @ApplicationContext private val context: Context
) {

    suspend fun getAllReports(): ApiResult<List<Report>> {
        return try {
            val entities = reportDao.getAllReports()
            ApiResult.Success(entities.map { it.toReport() })
        } catch (e: Exception) {
            ApiResult.Error("Failed to fetch reports: ${e.message}")
        }
    }

    suspend fun getReportById(reportId: String): ApiResult<Report> {
        return try {
            val entity = reportDao.getReportByReportId(reportId)
            if (entity != null) {
                ApiResult.Success(entity.toReport())
            } else {
                ApiResult.Error("Report not found")
            }
        } catch (e: Exception) {
            ApiResult.Error("Error fetching report: ${e.message}")
        }
    }

    suspend fun createReport(
        title: String,
        description: String,
        category: String,
        priority: String,
        latitude: Double,
        longitude: Double,
        photoUris: List<Uri> = emptyList(),
        voiceNoteUri: Uri? = null
    ): ApiResult<Report> {
        return try {
            val now = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault()).apply {
                timeZone = TimeZone.getTimeZone("UTC")
            }.format(Date())

            val count = reportDao.getReportCount()
            val reportId = "IND-%05d".format(count + 1)
            val id = UUID.randomUUID().toString()

            // Save photos locally
            val savedPhotoPaths = photoUris.mapIndexed { index, uri ->
                saveFileLocally(uri, "report_${id}_photo_$index.jpg")
            }

            // Save voice note locally
            val savedVoiceNote = voiceNoteUri?.let {
                saveFileLocally(it, "report_${id}_voice.wav")
            }

            val historyEntry = HistoryEntry(
                timestamp = now,
                action = "Report created",
                notes = "Report submitted via mobile app"
            )

            val entity = ReportEntity(
                id = id,
                reportId = reportId,
                title = title,
                description = description,
                category = category,
                priority = priority,
                status = "pending",
                latitude = latitude,
                longitude = longitude,
                photos = savedPhotoPaths,
                voiceNote = savedVoiceNote,
                assignedTo = null,
                history = listOf(historyEntry),
                createdAt = now,
                updatedAt = now
            )

            reportDao.insertReport(entity)

            ApiResult.Success(entity.toReport())
        } catch (e: Exception) {
            ApiResult.Error("Failed to create report: ${e.message}")
        }
    }

    suspend fun updateReport(
        reportId: String,
        status: String? = null,
        assignedTo: String? = null,
        notes: String? = null
    ): ApiResult<Report> {
        return try {
            val entity = reportDao.getReportByReportId(reportId)
                ?: return ApiResult.Error("Report not found")

            val now = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault()).apply {
                timeZone = TimeZone.getTimeZone("UTC")
            }.format(Date())

            val actionText = when {
                status != null -> "Status changed to ${status.replaceFirstChar { it.uppercase() }}"
                assignedTo != null -> "Assigned to $assignedTo"
                else -> "Report updated"
            }

            val newHistoryEntry = HistoryEntry(
                timestamp = now,
                action = actionText,
                notes = notes
            )

            val updatedEntity = entity.copy(
                status = status ?: entity.status,
                assignedTo = assignedTo ?: entity.assignedTo,
                history = entity.history + newHistoryEntry,
                updatedAt = now
            )

            reportDao.updateReport(updatedEntity)

            ApiResult.Success(updatedEntity.toReport())
        } catch (e: Exception) {
            ApiResult.Error("Failed to update report: ${e.message}")
        }
    }

    suspend fun deleteReport(reportId: String): ApiResult<Unit> {
        return try {
            reportDao.deleteReportByReportId(reportId)
            ApiResult.Success(Unit)
        } catch (e: Exception) {
            ApiResult.Error("Failed to delete report: ${e.message}")
        }
    }

    suspend fun login(username: String, password: String): ApiResult<String> {
        return try {
            // Local admin authentication - hardcoded credentials for offline mode
            if (username == "admin" && password == "admin123") {
                val token = "local_admin_token_${UUID.randomUUID()}"
                authRepository.saveToken(token)
                ApiResult.Success(token)
            } else {
                ApiResult.Error("Invalid username or password")
            }
        } catch (e: Exception) {
            ApiResult.Error("Login failed: ${e.message}")
        }
    }

    suspend fun logout(): ApiResult<Unit> {
        return try {
            authRepository.clearToken()
            ApiResult.Success(Unit)
        } catch (e: Exception) {
            authRepository.clearToken()
            ApiResult.Success(Unit)
        }
    }

    suspend fun verifyToken(): Boolean {
        return try {
            val token = authRepository.getToken()
            token != null && token.startsWith("local_admin_token_")
        } catch (e: Exception) {
            false
        }
    }

    private fun saveFileLocally(uri: Uri, fileName: String): String {
        val mediaDir = File(context.filesDir, "report_media")
        if (!mediaDir.exists()) mediaDir.mkdirs()

        val file = File(mediaDir, fileName)
        val inputStream = context.contentResolver.openInputStream(uri)
        FileOutputStream(file).use { outputStream ->
            inputStream?.copyTo(outputStream)
        }
        inputStream?.close()
        return file.absolutePath
    }
}
