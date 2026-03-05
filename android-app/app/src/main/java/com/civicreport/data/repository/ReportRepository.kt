package com.civicreport.data.repository

import android.content.Context
import android.net.Uri
import com.civicreport.data.api.CivicReportApi
import com.civicreport.data.model.*
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject
import javax.inject.Singleton

sealed class ApiResult<out T> {
    data class Success<T>(val data: T) : ApiResult<T>()
    data class Error(val message: String) : ApiResult<Nothing>()
    object Loading : ApiResult<Nothing>()
}

@Singleton
class ReportRepository @Inject constructor(
    private val api: CivicReportApi,
    private val authRepository: AuthRepository,
    @ApplicationContext private val context: Context
) {
    
    suspend fun getAllReports(): ApiResult<List<Report>> {
        return try {
            val response = api.getAllReports()
            if (response.isSuccessful) {
                ApiResult.Success(response.body() ?: emptyList())
            } else {
                ApiResult.Error("Failed to fetch reports: ${response.message()}")
            }
        } catch (e: Exception) {
            ApiResult.Error("Network error: ${e.message}")
        }
    }
    
    suspend fun getReportById(reportId: String): ApiResult<Report> {
        return try {
            val response = api.getReportById(reportId)
            if (response.isSuccessful && response.body() != null) {
                ApiResult.Success(response.body()!!)
            } else {
                ApiResult.Error("Report not found")
            }
        } catch (e: Exception) {
            ApiResult.Error("Network error: ${e.message}")
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
            val titleBody = title.toRequestBody("text/plain".toMediaTypeOrNull())
            val descriptionBody = description.toRequestBody("text/plain".toMediaTypeOrNull())
            val categoryBody = category.toRequestBody("text/plain".toMediaTypeOrNull())
            val priorityBody = priority.toRequestBody("text/plain".toMediaTypeOrNull())
            val latitudeBody = latitude.toString().toRequestBody("text/plain".toMediaTypeOrNull())
            val longitudeBody = longitude.toString().toRequestBody("text/plain".toMediaTypeOrNull())
            
            val photoParts = photoUris.mapIndexed { index, uri ->
                val file = uriToFile(uri, "photo_$index.jpg")
                val requestBody = file.asRequestBody("image/*".toMediaTypeOrNull())
                MultipartBody.Part.createFormData("photos", file.name, requestBody)
            }
            
            val voiceNotePart = voiceNoteUri?.let { uri ->
                val file = uriToFile(uri, "voice_note.wav")
                val requestBody = file.asRequestBody("audio/*".toMediaTypeOrNull())
                MultipartBody.Part.createFormData("voiceNote", file.name, requestBody)
            }
            
            val response = api.createReport(
                title = titleBody,
                description = descriptionBody,
                category = categoryBody,
                priority = priorityBody,
                latitude = latitudeBody,
                longitude = longitudeBody,
                photos = photoParts.ifEmpty { null },
                voiceNote = voiceNotePart
            )
            
            if (response.isSuccessful && response.body() != null) {
                ApiResult.Success(response.body()!!)
            } else {
                ApiResult.Error("Failed to create report: ${response.message()}")
            }
        } catch (e: Exception) {
            ApiResult.Error("Network error: ${e.message}")
        }
    }
    
    suspend fun updateReport(
        reportId: String,
        status: String? = null,
        assignedTo: String? = null,
        notes: String? = null
    ): ApiResult<Report> {
        return try {
            val token = authRepository.getToken()
                ?: return ApiResult.Error("Not authenticated")
            
            val response = api.updateReport(
                reportId = reportId,
                token = authRepository.getAuthHeader(token),
                request = ReportUpdateRequest(status, assignedTo, notes)
            )
            
            if (response.isSuccessful && response.body() != null) {
                ApiResult.Success(response.body()!!)
            } else {
                ApiResult.Error("Failed to update report: ${response.message()}")
            }
        } catch (e: Exception) {
            ApiResult.Error("Network error: ${e.message}")
        }
    }
    
    suspend fun deleteReport(reportId: String): ApiResult<Unit> {
        return try {
            val token = authRepository.getToken()
                ?: return ApiResult.Error("Not authenticated")
            
            val response = api.deleteReport(
                reportId = reportId,
                token = authRepository.getAuthHeader(token)
            )
            
            if (response.isSuccessful) {
                ApiResult.Success(Unit)
            } else {
                ApiResult.Error("Failed to delete report: ${response.message()}")
            }
        } catch (e: Exception) {
            ApiResult.Error("Network error: ${e.message}")
        }
    }
    
    suspend fun login(username: String, password: String): ApiResult<String> {
        return try {
            val response = api.login(LoginRequest(username, password))
            if (response.isSuccessful && response.body()?.success == true) {
                val token = response.body()!!.token!!
                authRepository.saveToken(token)
                ApiResult.Success(token)
            } else {
                ApiResult.Error(response.body()?.message ?: "Login failed")
            }
        } catch (e: Exception) {
            ApiResult.Error("Network error: ${e.message}")
        }
    }
    
    suspend fun logout(): ApiResult<Unit> {
        return try {
            val token = authRepository.getToken()
            if (token != null) {
                api.logout(authRepository.getAuthHeader(token))
            }
            authRepository.clearToken()
            ApiResult.Success(Unit)
        } catch (e: Exception) {
            authRepository.clearToken()
            ApiResult.Success(Unit)
        }
    }
    
    suspend fun verifyToken(): Boolean {
        return try {
            val token = authRepository.getToken() ?: return false
            val response = api.verifyToken(authRepository.getAuthHeader(token))
            response.isSuccessful
        } catch (e: Exception) {
            false
        }
    }
    
    private fun uriToFile(uri: Uri, fileName: String): File {
        val inputStream = context.contentResolver.openInputStream(uri)
        val file = File(context.cacheDir, fileName)
        FileOutputStream(file).use { outputStream ->
            inputStream?.copyTo(outputStream)
        }
        inputStream?.close()
        return file
    }
}
