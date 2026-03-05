package com.civicreport.data.api

import com.civicreport.data.model.LoginRequest
import com.civicreport.data.model.LoginResponse
import com.civicreport.data.model.Report
import com.civicreport.data.model.ReportUpdateRequest
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface CivicReportApi {
    
    // Public endpoints
    @GET("api/reports")
    suspend fun getAllReports(): Response<List<Report>>
    
    @GET("api/reports/{id}")
    suspend fun getReportById(@Path("id") reportId: String): Response<Report>
    
    @Multipart
    @POST("api/reports")
    suspend fun createReport(
        @Part("title") title: RequestBody,
        @Part("description") description: RequestBody,
        @Part("category") category: RequestBody,
        @Part("priority") priority: RequestBody,
        @Part("latitude") latitude: RequestBody,
        @Part("longitude") longitude: RequestBody,
        @Part photos: List<MultipartBody.Part>? = null,
        @Part voiceNote: MultipartBody.Part? = null
    ): Response<Report>
    
    // Admin endpoints
    @POST("api/admin/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>
    
    @POST("api/admin/logout")
    suspend fun logout(@Header("Authorization") token: String): Response<Unit>
    
    @GET("api/admin/verify")
    suspend fun verifyToken(@Header("Authorization") token: String): Response<Unit>
    
    @PATCH("api/reports/{id}")
    suspend fun updateReport(
        @Path("id") reportId: String,
        @Header("Authorization") token: String,
        @Body request: ReportUpdateRequest
    ): Response<Report>
    
    @DELETE("api/reports/{id}")
    suspend fun deleteReport(
        @Path("id") reportId: String,
        @Header("Authorization") token: String
    ): Response<Unit>
}
