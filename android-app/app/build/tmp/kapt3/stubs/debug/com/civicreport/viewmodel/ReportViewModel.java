package com.civicreport.viewmodel;

import android.net.Uri;
import androidx.lifecycle.ViewModel;
import com.civicreport.data.model.Report;
import com.civicreport.data.repository.ApiResult;
import com.civicreport.data.repository.ReportRepository;
import dagger.hilt.android.lifecycle.HiltViewModel;
import kotlinx.coroutines.flow.StateFlow;
import javax.inject.Inject;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000B\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0010\u0006\n\u0002\b\u0006\b\u0007\u0018\u00002\u00020\u0001B\u000f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u000e\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u000fJ\u0006\u0010\u0010\u001a\u00020\rJ\u000e\u0010\u0011\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u000fJ\u0006\u0010\u0012\u001a\u00020\rJ\u0010\u0010\u0013\u001a\u00020\r2\b\u0010\u000e\u001a\u0004\u0018\u00010\u000fJ\u0006\u0010\u0014\u001a\u00020\rJ\u000e\u0010\u0015\u001a\u00020\r2\u0006\u0010\u0016\u001a\u00020\u0017J\u000e\u0010\u0018\u001a\u00020\r2\u0006\u0010\u0019\u001a\u00020\u0017J\u0016\u0010\u001a\u001a\u00020\r2\u0006\u0010\u001b\u001a\u00020\u001c2\u0006\u0010\u001d\u001a\u00020\u001cJ\u000e\u0010\u001e\u001a\u00020\r2\u0006\u0010\u001f\u001a\u00020\u0017J\u000e\u0010 \u001a\u00020\r2\u0006\u0010!\u001a\u00020\u0017R\u0014\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\b\u001a\b\u0012\u0004\u0012\u00020\u00070\t\u00a2\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000bR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\""}, d2 = {"Lcom/civicreport/viewmodel/ReportViewModel;", "Landroidx/lifecycle/ViewModel;", "reportRepository", "Lcom/civicreport/data/repository/ReportRepository;", "(Lcom/civicreport/data/repository/ReportRepository;)V", "_formState", "Lkotlinx/coroutines/flow/MutableStateFlow;", "Lcom/civicreport/viewmodel/ReportFormState;", "formState", "Lkotlinx/coroutines/flow/StateFlow;", "getFormState", "()Lkotlinx/coroutines/flow/StateFlow;", "addPhoto", "", "uri", "Landroid/net/Uri;", "clearError", "removePhoto", "resetForm", "setVoiceNote", "submitReport", "updateCategory", "category", "", "updateDescription", "description", "updateLocation", "latitude", "", "longitude", "updatePriority", "priority", "updateTitle", "title", "app_debug"})
@dagger.hilt.android.lifecycle.HiltViewModel()
public final class ReportViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.civicreport.data.repository.ReportRepository reportRepository = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<com.civicreport.viewmodel.ReportFormState> _formState = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.civicreport.viewmodel.ReportFormState> formState = null;
    
    @javax.inject.Inject()
    public ReportViewModel(@org.jetbrains.annotations.NotNull()
    com.civicreport.data.repository.ReportRepository reportRepository) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.civicreport.viewmodel.ReportFormState> getFormState() {
        return null;
    }
    
    public final void updateTitle(@org.jetbrains.annotations.NotNull()
    java.lang.String title) {
    }
    
    public final void updateDescription(@org.jetbrains.annotations.NotNull()
    java.lang.String description) {
    }
    
    public final void updateCategory(@org.jetbrains.annotations.NotNull()
    java.lang.String category) {
    }
    
    public final void updatePriority(@org.jetbrains.annotations.NotNull()
    java.lang.String priority) {
    }
    
    public final void updateLocation(double latitude, double longitude) {
    }
    
    public final void addPhoto(@org.jetbrains.annotations.NotNull()
    android.net.Uri uri) {
    }
    
    public final void removePhoto(@org.jetbrains.annotations.NotNull()
    android.net.Uri uri) {
    }
    
    public final void setVoiceNote(@org.jetbrains.annotations.Nullable()
    android.net.Uri uri) {
    }
    
    public final void clearError() {
    }
    
    public final void submitReport() {
    }
    
    public final void resetForm() {
    }
}