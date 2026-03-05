package com.civicreport.data.repository;

import android.content.Context;
import android.net.Uri;
import com.civicreport.data.api.CivicReportApi;
import com.civicreport.data.model.*;
import dagger.hilt.android.qualifiers.ApplicationContext;
import okhttp3.MultipartBody;
import java.io.File;
import java.io.FileOutputStream;
import javax.inject.Inject;
import javax.inject.Singleton;

@javax.inject.Singleton()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000Z\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0010\u0006\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0010\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0000\b\u0007\u0018\u00002\u00020\u0001B!\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\b\b\u0001\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\u0002\u0010\bJ`\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u000b0\n2\u0006\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\r2\u0006\u0010\u000f\u001a\u00020\r2\u0006\u0010\u0010\u001a\u00020\r2\u0006\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u00122\u000e\b\u0002\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\u00160\u00152\n\b\u0002\u0010\u0017\u001a\u0004\u0018\u00010\u0016H\u0086@\u00a2\u0006\u0002\u0010\u0018J\u001c\u0010\u0019\u001a\b\u0012\u0004\u0012\u00020\u001a0\n2\u0006\u0010\u001b\u001a\u00020\rH\u0086@\u00a2\u0006\u0002\u0010\u001cJ\u001a\u0010\u001d\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u000b0\u00150\nH\u0086@\u00a2\u0006\u0002\u0010\u001eJ\u001c\u0010\u001f\u001a\b\u0012\u0004\u0012\u00020\u000b0\n2\u0006\u0010\u001b\u001a\u00020\rH\u0086@\u00a2\u0006\u0002\u0010\u001cJ$\u0010 \u001a\b\u0012\u0004\u0012\u00020\r0\n2\u0006\u0010!\u001a\u00020\r2\u0006\u0010\"\u001a\u00020\rH\u0086@\u00a2\u0006\u0002\u0010#J\u0014\u0010$\u001a\b\u0012\u0004\u0012\u00020\u001a0\nH\u0086@\u00a2\u0006\u0002\u0010\u001eJ@\u0010%\u001a\b\u0012\u0004\u0012\u00020\u000b0\n2\u0006\u0010\u001b\u001a\u00020\r2\n\b\u0002\u0010&\u001a\u0004\u0018\u00010\r2\n\b\u0002\u0010\'\u001a\u0004\u0018\u00010\r2\n\b\u0002\u0010(\u001a\u0004\u0018\u00010\rH\u0086@\u00a2\u0006\u0002\u0010)J\u0018\u0010*\u001a\u00020+2\u0006\u0010,\u001a\u00020\u00162\u0006\u0010-\u001a\u00020\rH\u0002J\u000e\u0010.\u001a\u00020/H\u0086@\u00a2\u0006\u0002\u0010\u001eR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u00060"}, d2 = {"Lcom/civicreport/data/repository/ReportRepository;", "", "api", "Lcom/civicreport/data/api/CivicReportApi;", "authRepository", "Lcom/civicreport/data/repository/AuthRepository;", "context", "Landroid/content/Context;", "(Lcom/civicreport/data/api/CivicReportApi;Lcom/civicreport/data/repository/AuthRepository;Landroid/content/Context;)V", "createReport", "Lcom/civicreport/data/repository/ApiResult;", "Lcom/civicreport/data/model/Report;", "title", "", "description", "category", "priority", "latitude", "", "longitude", "photoUris", "", "Landroid/net/Uri;", "voiceNoteUri", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;DDLjava/util/List;Landroid/net/Uri;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteReport", "", "reportId", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getAllReports", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getReportById", "login", "username", "password", "(Ljava/lang/String;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "logout", "updateReport", "status", "assignedTo", "notes", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "uriToFile", "Ljava/io/File;", "uri", "fileName", "verifyToken", "", "app_debug"})
public final class ReportRepository {
    @org.jetbrains.annotations.NotNull()
    private final com.civicreport.data.api.CivicReportApi api = null;
    @org.jetbrains.annotations.NotNull()
    private final com.civicreport.data.repository.AuthRepository authRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final android.content.Context context = null;
    
    @javax.inject.Inject()
    public ReportRepository(@org.jetbrains.annotations.NotNull()
    com.civicreport.data.api.CivicReportApi api, @org.jetbrains.annotations.NotNull()
    com.civicreport.data.repository.AuthRepository authRepository, @dagger.hilt.android.qualifiers.ApplicationContext()
    @org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        super();
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object getAllReports(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.civicreport.data.repository.ApiResult<? extends java.util.List<com.civicreport.data.model.Report>>> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object getReportById(@org.jetbrains.annotations.NotNull()
    java.lang.String reportId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.civicreport.data.repository.ApiResult<com.civicreport.data.model.Report>> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object createReport(@org.jetbrains.annotations.NotNull()
    java.lang.String title, @org.jetbrains.annotations.NotNull()
    java.lang.String description, @org.jetbrains.annotations.NotNull()
    java.lang.String category, @org.jetbrains.annotations.NotNull()
    java.lang.String priority, double latitude, double longitude, @org.jetbrains.annotations.NotNull()
    java.util.List<? extends android.net.Uri> photoUris, @org.jetbrains.annotations.Nullable()
    android.net.Uri voiceNoteUri, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.civicreport.data.repository.ApiResult<com.civicreport.data.model.Report>> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object updateReport(@org.jetbrains.annotations.NotNull()
    java.lang.String reportId, @org.jetbrains.annotations.Nullable()
    java.lang.String status, @org.jetbrains.annotations.Nullable()
    java.lang.String assignedTo, @org.jetbrains.annotations.Nullable()
    java.lang.String notes, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.civicreport.data.repository.ApiResult<com.civicreport.data.model.Report>> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object deleteReport(@org.jetbrains.annotations.NotNull()
    java.lang.String reportId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.civicreport.data.repository.ApiResult<kotlin.Unit>> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object login(@org.jetbrains.annotations.NotNull()
    java.lang.String username, @org.jetbrains.annotations.NotNull()
    java.lang.String password, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.civicreport.data.repository.ApiResult<java.lang.String>> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object logout(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.civicreport.data.repository.ApiResult<kotlin.Unit>> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object verifyToken(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Boolean> $completion) {
        return null;
    }
    
    private final java.io.File uriToFile(android.net.Uri uri, java.lang.String fileName) {
        return null;
    }
}