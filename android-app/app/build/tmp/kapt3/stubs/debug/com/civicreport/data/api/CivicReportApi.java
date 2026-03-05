package com.civicreport.data.api;

import com.civicreport.data.model.LoginRequest;
import com.civicreport.data.model.LoginResponse;
import com.civicreport.data.model.Report;
import com.civicreport.data.model.ReportUpdateRequest;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Response;
import retrofit2.http.*;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000L\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\bf\u0018\u00002\u00020\u0001Jn\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u00032\b\b\u0001\u0010\u0005\u001a\u00020\u00062\b\b\u0001\u0010\u0007\u001a\u00020\u00062\b\b\u0001\u0010\b\u001a\u00020\u00062\b\b\u0001\u0010\t\u001a\u00020\u00062\b\b\u0001\u0010\n\u001a\u00020\u00062\b\b\u0001\u0010\u000b\u001a\u00020\u00062\u0010\b\u0003\u0010\f\u001a\n\u0012\u0004\u0012\u00020\u000e\u0018\u00010\r2\n\b\u0003\u0010\u000f\u001a\u0004\u0018\u00010\u000eH\u00a7@\u00a2\u0006\u0002\u0010\u0010J(\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u00120\u00032\b\b\u0001\u0010\u0013\u001a\u00020\u00142\b\b\u0001\u0010\u0015\u001a\u00020\u0014H\u00a7@\u00a2\u0006\u0002\u0010\u0016J\u001a\u0010\u0017\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00040\r0\u0003H\u00a7@\u00a2\u0006\u0002\u0010\u0018J\u001e\u0010\u0019\u001a\b\u0012\u0004\u0012\u00020\u00040\u00032\b\b\u0001\u0010\u0013\u001a\u00020\u0014H\u00a7@\u00a2\u0006\u0002\u0010\u001aJ\u001e\u0010\u001b\u001a\b\u0012\u0004\u0012\u00020\u001c0\u00032\b\b\u0001\u0010\u001d\u001a\u00020\u001eH\u00a7@\u00a2\u0006\u0002\u0010\u001fJ\u001e\u0010 \u001a\b\u0012\u0004\u0012\u00020\u00120\u00032\b\b\u0001\u0010\u0015\u001a\u00020\u0014H\u00a7@\u00a2\u0006\u0002\u0010\u001aJ2\u0010!\u001a\b\u0012\u0004\u0012\u00020\u00040\u00032\b\b\u0001\u0010\u0013\u001a\u00020\u00142\b\b\u0001\u0010\u0015\u001a\u00020\u00142\b\b\u0001\u0010\u001d\u001a\u00020\"H\u00a7@\u00a2\u0006\u0002\u0010#J\u001e\u0010$\u001a\b\u0012\u0004\u0012\u00020\u00120\u00032\b\b\u0001\u0010\u0015\u001a\u00020\u0014H\u00a7@\u00a2\u0006\u0002\u0010\u001a\u00a8\u0006%"}, d2 = {"Lcom/civicreport/data/api/CivicReportApi;", "", "createReport", "Lretrofit2/Response;", "Lcom/civicreport/data/model/Report;", "title", "Lokhttp3/RequestBody;", "description", "category", "priority", "latitude", "longitude", "photos", "", "Lokhttp3/MultipartBody$Part;", "voiceNote", "(Lokhttp3/RequestBody;Lokhttp3/RequestBody;Lokhttp3/RequestBody;Lokhttp3/RequestBody;Lokhttp3/RequestBody;Lokhttp3/RequestBody;Ljava/util/List;Lokhttp3/MultipartBody$Part;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteReport", "", "reportId", "", "token", "(Ljava/lang/String;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getAllReports", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getReportById", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "login", "Lcom/civicreport/data/model/LoginResponse;", "request", "Lcom/civicreport/data/model/LoginRequest;", "(Lcom/civicreport/data/model/LoginRequest;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "logout", "updateReport", "Lcom/civicreport/data/model/ReportUpdateRequest;", "(Ljava/lang/String;Ljava/lang/String;Lcom/civicreport/data/model/ReportUpdateRequest;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "verifyToken", "app_debug"})
public abstract interface CivicReportApi {
    
    @retrofit2.http.GET(value = "api/reports")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getAllReports(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super retrofit2.Response<java.util.List<com.civicreport.data.model.Report>>> $completion);
    
    @retrofit2.http.GET(value = "api/reports/{id}")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getReportById(@retrofit2.http.Path(value = "id")
    @org.jetbrains.annotations.NotNull()
    java.lang.String reportId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super retrofit2.Response<com.civicreport.data.model.Report>> $completion);
    
    @retrofit2.http.Multipart()
    @retrofit2.http.POST(value = "api/reports")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object createReport(@retrofit2.http.Part(value = "title")
    @org.jetbrains.annotations.NotNull()
    okhttp3.RequestBody title, @retrofit2.http.Part(value = "description")
    @org.jetbrains.annotations.NotNull()
    okhttp3.RequestBody description, @retrofit2.http.Part(value = "category")
    @org.jetbrains.annotations.NotNull()
    okhttp3.RequestBody category, @retrofit2.http.Part(value = "priority")
    @org.jetbrains.annotations.NotNull()
    okhttp3.RequestBody priority, @retrofit2.http.Part(value = "latitude")
    @org.jetbrains.annotations.NotNull()
    okhttp3.RequestBody latitude, @retrofit2.http.Part(value = "longitude")
    @org.jetbrains.annotations.NotNull()
    okhttp3.RequestBody longitude, @retrofit2.http.Part()
    @org.jetbrains.annotations.Nullable()
    java.util.List<okhttp3.MultipartBody.Part> photos, @retrofit2.http.Part()
    @org.jetbrains.annotations.Nullable()
    okhttp3.MultipartBody.Part voiceNote, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super retrofit2.Response<com.civicreport.data.model.Report>> $completion);
    
    @retrofit2.http.POST(value = "api/admin/login")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object login(@retrofit2.http.Body()
    @org.jetbrains.annotations.NotNull()
    com.civicreport.data.model.LoginRequest request, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super retrofit2.Response<com.civicreport.data.model.LoginResponse>> $completion);
    
    @retrofit2.http.POST(value = "api/admin/logout")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object logout(@retrofit2.http.Header(value = "Authorization")
    @org.jetbrains.annotations.NotNull()
    java.lang.String token, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super retrofit2.Response<kotlin.Unit>> $completion);
    
    @retrofit2.http.GET(value = "api/admin/verify")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object verifyToken(@retrofit2.http.Header(value = "Authorization")
    @org.jetbrains.annotations.NotNull()
    java.lang.String token, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super retrofit2.Response<kotlin.Unit>> $completion);
    
    @retrofit2.http.PATCH(value = "api/reports/{id}")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object updateReport(@retrofit2.http.Path(value = "id")
    @org.jetbrains.annotations.NotNull()
    java.lang.String reportId, @retrofit2.http.Header(value = "Authorization")
    @org.jetbrains.annotations.NotNull()
    java.lang.String token, @retrofit2.http.Body()
    @org.jetbrains.annotations.NotNull()
    com.civicreport.data.model.ReportUpdateRequest request, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super retrofit2.Response<com.civicreport.data.model.Report>> $completion);
    
    @retrofit2.http.DELETE(value = "api/reports/{id}")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object deleteReport(@retrofit2.http.Path(value = "id")
    @org.jetbrains.annotations.NotNull()
    java.lang.String reportId, @retrofit2.http.Header(value = "Authorization")
    @org.jetbrains.annotations.NotNull()
    java.lang.String token, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super retrofit2.Response<kotlin.Unit>> $completion);
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 3, xi = 48)
    public static final class DefaultImpls {
    }
}